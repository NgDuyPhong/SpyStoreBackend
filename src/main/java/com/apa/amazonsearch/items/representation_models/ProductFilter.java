package com.apa.amazonsearch.items.representation_models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductFilter {
    Integer rank;
    Integer dateWithin;
    Integer review;
}
