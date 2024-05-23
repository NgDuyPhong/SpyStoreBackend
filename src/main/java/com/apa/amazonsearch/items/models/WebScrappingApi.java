package com.apa.amazonsearch.items.models;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.net.URLEncoder;

@Data
@NoArgsConstructor
public class WebScrappingApi {
    public String getUrl(String amazonOriginalPage) {
        String apiKey = "ZklgQFDrjgeCOl1eVsrlWDkVPpYj9E6S";
        String encodedUrl = URLEncoder.encode(amazonOriginalPage);
        String amazonPage = "https://api.webscrapingapi.com/v1?api_key=" + apiKey + "&device=desktop&country=us" +  "&url=" + encodedUrl + "&proxy_type=residential";
        return amazonPage;
    }
}
