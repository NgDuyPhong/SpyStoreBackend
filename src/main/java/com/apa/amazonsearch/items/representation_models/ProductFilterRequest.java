package com.apa.amazonsearch.items.representation_models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProductFilterRequest {
    String searchQuery;
    String category;
    Integer maxPageScan;

    Integer firstRank;
    Integer firstDateWithin;
    Integer firstReview;

    Integer secondRank;
    Integer secondDateWithin;
    Integer secondReview;

    String userId;

    @JsonIgnore
    public ProductFilter getFirstFilter() {
        return new ProductFilter(firstRank, firstDateWithin, firstReview);
    }

    @JsonIgnore
    public ProductFilter getSecondFilter() {
        return new ProductFilter(secondRank, secondDateWithin, secondReview);
    }

    @JsonIgnore
    public String getAmazonOriginalPage() {
        String query = searchQuery.replace(" ", "+");

        if (this.category == null) {
            this.category = "fashion";
        }

        return "https://www.amazon.com/s?k=" + query + "&i=" + this.category;
    }
}
