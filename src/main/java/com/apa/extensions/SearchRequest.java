package com.apa.extensions;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class SearchRequest {
    Integer higherThanRank;
    String afterDate;
    Double higherThanReview;

    Integer lessThanRank;
    String beforeDate;
    Double lessThanReview;
}
