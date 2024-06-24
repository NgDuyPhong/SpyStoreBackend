package com.apa.amazonsearch.items.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@Slf4j
public class ItemSearchResultCollection {
    List<ItemSearchResult> itemSearchResults = new ArrayList<>();

    public ItemSearchResultCollection(Element searchResultPage) {
        Elements products = searchResultPage.getElementsByAttributeValue("data-component-type", "s-search-result");

        List<ItemSearchResult> itemSearchResults = products
            .stream()
            .filter(this::isNotSponsor)
            .map(this::buildItemSearchResult)
            .collect(Collectors.toList());

        this.itemSearchResults = itemSearchResults;
    }

    private boolean isNotSponsor(Element product) {
        if (product == null) return false;
        String html = product.html();
        return !html.contains("Sponsored");
    }

    private ItemSearchResult buildItemSearchResult(Element productResult) {
        Element productImage = productResult.getElementsByAttributeValue("data-component-type", "s-product-image").first();

        ItemSearchResult itemSearchResult = new ItemSearchResult();
        String productUrl = productImage.firstElementChild().attr("href");
        itemSearchResult.setProductUrl("https://amazon.com" + productUrl);

        String[] productUrlInfos = productUrl.split("/");

        if (productUrlInfos.length > 3) {
            itemSearchResult.setProductId(productUrlInfos[3]);
        } else {
            log.info(" what happen" + productUrl);
        }

        itemSearchResult.setId(UUID.randomUUID().toString());

        return itemSearchResult;
    }

    public void filterOutExistItems(List<ProductScanHistory> productScanHistories) {
        this.itemSearchResults = this.itemSearchResults
            .stream()
            .filter(itemSearchResult -> !isItemExist(productScanHistories, itemSearchResult))
            .collect(Collectors.toList());
    }

    private boolean isItemExist(List<ProductScanHistory> productScanHistories, ItemSearchResult itemSearchResult) {
        return productScanHistories
            .stream()
            .anyMatch(productScanHistory -> productScanHistory.getProductId().equalsIgnoreCase(itemSearchResult.getProductId()));
    }
}
