package com.apa.amazonsearch.page_reader;

import com.apa.amazonsearch.error_data.models.ErrorData;
import com.apa.amazonsearch.items.models.*;
import com.apa.amazonsearch.items.representation_models.ProductFilterRequest;
import com.apa.amazonsearch.items.threads.ProductThread;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@Slf4j
public class AmazonPageReader {
    String pageUrl;

    public ItemDetail readItemDetail(ItemSearchResult itemSearchResult) throws IOException {
        String productUrlFromScrappingAPI = itemSearchResult.getProductUrlFromScrappingAPI();
        Connection conn = Jsoup.connect(productUrlFromScrappingAPI).timeout(60 * 1000);

        ItemDetail itemDetail = null;

        int i = 0;
        while ((i< 5) && (itemDetail == null || itemDetail.getAsin() == null)) {
            i += 1;
            Document doc = getDocument(conn);
            String result = doc.body().html();
            Document amazonHtml = Jsoup.parse(result);
            Element productPage = amazonHtml.getElementsByTag("body").get(0);
            itemDetail = new ItemDetail(productPage, itemSearchResult);
        }


        return itemDetail.getAsin() != null ? itemDetail : null;
    }

    public ItemSearchResultCollection readSearchResultPage(String amazonPage) throws IOException {
        //Connecting to the web page
        Connection conn = Jsoup.connect(amazonPage);
        //executing the get request
        Document doc = getDocument(conn);

        if (doc == null) {
            return null;
        }

        //Retrieving the contents (body) of the web page
        String result = doc.body().html();
        Document amazonHtml = Jsoup.parse(result);
        Element searchResultPage = amazonHtml.getElementsByTag("body").get(0);

        ItemSearchResultCollection itemSearchResultCollection = new ItemSearchResultCollection(searchResultPage);

        return itemSearchResultCollection;
    }

    private Document getDocument(Connection conn) throws IOException {
        Document doc = null;

        int i = 0;
        while (doc == null && i < 10) {
            i += 1;
            try {
                doc = conn.get();
            } catch (Exception e) {
                doc = null;
            }
        }

        return doc;
    }

    public PagedItemDetailCollection readSearchResultPagination(String amazonPage) throws IOException {
        WebScrappingApi webScrappingApi = new WebScrappingApi();

        //Connecting to the web page
        Connection conn = Jsoup.connect(webScrappingApi.getUrl(amazonPage));
        //executing the get request
        Document doc = getDocument(conn);

        if (doc == null) {
            return null;
        }

        //Retrieving the contents (body) of the web page
        String result = doc.body().html();
        Document amazonHtml = Jsoup.parse(result);
        Element searchResultPage = amazonHtml.getElementsByTag("body").get(0);

        PagedItemDetailCollection itemDetailCollection = new PagedItemDetailCollection(amazonPage, searchResultPage);

        return itemDetailCollection;
    }

    public PagedItemDetailCollection readAllProductsInOnePage(PagedItemDetailCollection pagedItemDetailCollection, int pageIndex, ProductFilterRequest productFilterRequest, List<ProductScanHistory> productScanHistories) throws IOException {
        ExecutorService executorService = Executors.newFixedThreadPool(10);

        ItemSearchResultCollection itemSearchResultCollection = readSearchResultPage(pagedItemDetailCollection.getPagination().getAmazonSearchPage(pageIndex));

        if (itemSearchResultCollection == null) {
            ErrorData errorData = new ErrorData();
            errorData.setId(UUID.randomUUID().toString());
            errorData.setPageUrl(pagedItemDetailCollection.getPagination().getAmazonOriginalPageAtPage(pageIndex));
            pagedItemDetailCollection.addErrorPage(errorData);
            return pagedItemDetailCollection;
        }

        log.info("Search page " + itemSearchResultCollection.getItemSearchResults().size());

        itemSearchResultCollection.filterOutExistItems(productScanHistories);

        log.info("Search page filter" + itemSearchResultCollection.getItemSearchResults().size());

        itemSearchResultCollection.getItemSearchResults().forEach(itemSearchResult -> {
            itemSearchResult.setSearchDataId(pagedItemDetailCollection.getSearchData().getId());
        });

        for (ItemSearchResult itemSearchResult : itemSearchResultCollection.getItemSearchResults()) {
            executorService.execute(new ProductThread(itemSearchResult, pagedItemDetailCollection));
        }

        executorService.shutdown();

        while (!executorService.isTerminated()) {
            // empty body
        }

        log.info(" Start adding item from history");

        pagedItemDetailCollection.addItemHistoryMatchConditionToItems(productScanHistories, productFilterRequest);

        log.info("Finish page " + pagedItemDetailCollection.getItemDetails().size());

        return pagedItemDetailCollection;
    }
}
