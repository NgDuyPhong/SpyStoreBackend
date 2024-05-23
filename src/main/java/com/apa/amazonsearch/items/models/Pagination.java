package com.apa.amazonsearch.items.models;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.net.URLEncoder;

@Data
@NoArgsConstructor
public class Pagination {
    Integer page;
    Integer totalPages;
    String pageUrl;

    public String getAmazonSearchPage(int pageIndex) {
        String amazonOriginalPage = getAmazonOriginalPageAtPage(pageIndex);

        String encodedUrl = URLEncoder.encode(amazonOriginalPage);
        WebScrappingApi webScrappingApi = new WebScrappingApi();
        String url = webScrappingApi.getUrl(encodedUrl);

        return url;
    }

    public String getAmazonOriginalPageAtPage(int pageIndex) {
        return this.pageUrl + "&page=" + (pageIndex + 1);
    }
}
