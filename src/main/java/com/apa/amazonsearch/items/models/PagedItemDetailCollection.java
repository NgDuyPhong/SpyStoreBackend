package com.apa.amazonsearch.items.models;

import com.apa.amazonsearch.error_data.models.ErrorData;
import com.apa.amazonsearch.items.representation_models.ProductFilterRequest;
import com.apa.amazonsearch.search_data.models.SearchData;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class PagedItemDetailCollection {
    List<ItemDetail> itemDetails = new ArrayList<>();
    Pagination pagination = new Pagination();
    SearchData searchData;
    List<ErrorData> errors = new ArrayList<>();

    public PagedItemDetailCollection(String amazonPage, Element searchResultPage) {
        Elements pages = searchResultPage.getElementsByClass("s-pagination-item");

        if (pages.size() < 2) {
            Pagination pagination = new Pagination();
            pagination.setTotalPages(1);
            pagination.setPage(1);
            pagination.setPageUrl(amazonPage);
            this.pagination = pagination;
        } else {
            Element element = pages.get(pages.size() - 2);
            String lastPage = element.text();
            Pagination pagination = new Pagination();
            pagination.setTotalPages(Integer.valueOf(lastPage));
            pagination.setPage(1);
            pagination.setPageUrl(amazonPage);
            this.pagination = pagination;
        }
    }

    public void filterMatchCondition(ProductFilterRequest filterRequest) {
        this.itemDetails = this.itemDetails
            .stream()
            .filter(item -> item.matchCondition(filterRequest))
            .collect(Collectors.toList());
    }

    public void initUser(String userId) {
        this.itemDetails.forEach(itemDetail -> {
            itemDetail.setUserId(userId);
        });

        this.errors.forEach(errorData -> {
            errorData.setUserId(userId);
        });
    }

    public void addErrorPage(ErrorData errorData) {
        this.errors.add(errorData);
    }

    public void addItemHistoryMatchConditionToItems(List<ProductScanHistory> productScanHistories, ProductFilterRequest productFilterRequest) {
        List<ProductScanHistory> productsHistoryMatchCondition = productScanHistories
            .stream()
            .filter(productScanHistory -> productScanHistory.matchCondition(productFilterRequest) && !this.isExistInList(productScanHistory))
            .toList();

        List<ItemDetail> itemDetailsMatchCondition = productsHistoryMatchCondition
            .stream()
            .map(ItemDetail::new)
            .toList();

        this.itemDetails.addAll(itemDetailsMatchCondition);
    }

    private boolean isExistInList(ProductScanHistory productScanHistory) {
        return this.itemDetails
            .stream()
            .anyMatch(item -> item.isExist(productScanHistory));
    }
}
