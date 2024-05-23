package com.apa.amazonsearch.items.models;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ItemSearchResult {
    String id;
    String productId;
    String productUrl;
    String searchDataId;

    public String getProductUrlFromScrappingAPI() {
        return new WebScrappingApi().getUrl(this.productUrl);
    }
}
