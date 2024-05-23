package com.apa.amazonsearch.items.services;

import com.apa.amazonsearch.error_data.repositories.ErrorDataRepository;
import com.apa.amazonsearch.items.models.ItemDetail;
import com.apa.amazonsearch.items.models.PagedItemDetailCollection;
import com.apa.amazonsearch.items.models.ProductScanHistory;
import com.apa.amazonsearch.items.repositories.ItemDetailRepository;
import com.apa.amazonsearch.items.repositories.ProductScanHistoryRepository;
import com.apa.amazonsearch.items.representation_models.ProductFilterRequest;
import com.apa.amazonsearch.items.representation_models.ProductResultFilter;
import com.apa.amazonsearch.page_reader.AmazonPageReader;
import com.apa.amazonsearch.search_data.models.SearchData;
import com.apa.amazonsearch.search_data.repositories.SearchDataRepository;
import com.apa.amazonsearch.search_processing.models.SearchProcessing;
import com.apa.amazonsearch.search_processing.repositories.SearchProcessingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductFilterServiceImpl implements ProductFilterService {
    private final ItemDetailRepository itemDetailRepository;
    private final SearchDataRepository searchDataRepository;
    private final SearchProcessingRepository searchProcessingRepository;
    private final ProductScanHistoryRepository productScanHistoryRepository;
    private final ErrorDataRepository errorDataRepository;

    @Override
    public PagedItemDetailCollection findProductsFromAmazon(ProductFilterRequest productFilterRequest) throws IOException {
        String amazonOriginalPage = productFilterRequest.getAmazonOriginalPage();

        AmazonPageReader amazonPageReader = new AmazonPageReader();
        PagedItemDetailCollection pagedItemDetailCollection = amazonPageReader.readSearchResultPagination(amazonOriginalPage);

        if (pagedItemDetailCollection == null || pagedItemDetailCollection.getPagination().getTotalPages() == null) {
            log.info("First page error");
            return pagedItemDetailCollection;
        }

        SearchData searchData = getSearchData(productFilterRequest);
        pagedItemDetailCollection.setSearchData(searchData);

        if (productFilterRequest.getMaxPageScan() != null && pagedItemDetailCollection.getPagination().getTotalPages() > productFilterRequest.getMaxPageScan()) {
            pagedItemDetailCollection.getPagination().setTotalPages(productFilterRequest.getMaxPageScan());
        }

        Integer totalPages = pagedItemDetailCollection.getPagination().getTotalPages();

        for (int pageIndex = 0; pageIndex < pagedItemDetailCollection.getPagination().getTotalPages(); pageIndex ++ ) {
            PagedItemDetailCollection products = amazonPageReader.readAllProductsInOnePage(
                pagedItemDetailCollection,
                pageIndex,
                productFilterRequest,
                getProductScanHistories(productFilterRequest)
            );

            products.initUser(productFilterRequest.getUserId());

            saveItemScanHistory(products, productFilterRequest);

            saveItemsMatchCondition(productFilterRequest, products);

            saveSearchProcessing(searchData, pageIndex, totalPages);

            saveErrorPage(products);
        }

        log.info("Finish search");

        return pagedItemDetailCollection;
    }

    private void saveErrorPage(PagedItemDetailCollection products) {
        errorDataRepository.saveAll(products.getErrors());
    }

    private List<ProductScanHistory> getProductScanHistories(ProductFilterRequest productFilterRequest) {
        return productScanHistoryRepository.findByUserId(productFilterRequest.getUserId());
    }

    private void saveItemScanHistory(PagedItemDetailCollection products, ProductFilterRequest productFilterRequest) {
        List<ProductScanHistory> scanHistories = products.getItemDetails()
            .stream()
            .map(product -> new ProductScanHistory(product, productFilterRequest))
            .collect(Collectors.toList());

        productScanHistoryRepository.saveAll(scanHistories);
    }

    private void saveItemsMatchCondition(ProductFilterRequest productFilterRequest, PagedItemDetailCollection products) {
        products.filterMatchCondition(productFilterRequest);
        itemDetailRepository.saveAll(products.getItemDetails());
    }

    private void saveSearchProcessing(SearchData searchData, int pageIndex, Integer totalPages) {
        SearchProcessing searchProcessing = new SearchProcessing();
        searchProcessing.setSearchId(searchData.getId());
        searchProcessing.setMaxPage(totalPages);
        searchProcessing.setPage(pageIndex + 1);
        searchProcessing.setId(UUID.randomUUID().toString());
        searchProcessing.setUserId(searchData.getUserId());
        searchProcessingRepository.save(searchProcessing);
    }

    @Override
    public List<ItemDetail> findFilteredProducts(String searchId) {
        return itemDetailRepository.findBySearchDataId(searchId);
    }

    @Override
    public ItemDetail favoriteItem(String itemId) {
        ItemDetail itemDetail = itemDetailRepository.findById(itemId).orElse(null);
        itemDetail.setFavorite(true);
        return itemDetailRepository.save(itemDetail);
    }

    @Override
    public ItemDetail unfavoriteItem(String itemId) {
        ItemDetail itemDetail = itemDetailRepository.findById(itemId).orElse(null);
        itemDetail.setFavorite(false);
        return itemDetailRepository.save(itemDetail);
    }

    @Override
    @Transactional
    public void deleteAllResults(String id) {
        itemDetailRepository.deleteByUserId(id);
        searchProcessingRepository.deleteByUserId(id);
        searchDataRepository.deleteByUserId(id);
        productScanHistoryRepository.deleteByUserId(id);
        errorDataRepository.deleteByUserId(id);
    }

    @Override
    public List<ItemDetail> findFilteredProducts(ProductResultFilter productResultFilter) {
        return itemDetailRepository.findBySearchDataIdInAndUserId(productResultFilter.getSearchIds(), productResultFilter.getUserId());
    }

    private SearchData getSearchData(ProductFilterRequest productFilterRequest) {
        SearchData searchData = new SearchData();
        searchData.setSearchQuery(productFilterRequest.getSearchQuery());
        searchData.setId(UUID.randomUUID().toString());
        searchData.setUserId(productFilterRequest.getUserId());

        searchDataRepository.save(searchData);
        return searchData;
    }
}
