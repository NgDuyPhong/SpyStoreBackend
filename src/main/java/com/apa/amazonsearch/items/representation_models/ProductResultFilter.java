package com.apa.amazonsearch.items.representation_models;

import com.fasterxml.jackson.databind.type.CollectionType;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.util.List;

@Data
@NoArgsConstructor
public class ProductResultFilter {
    String searchText;

    List<String> searchIds;

    String userId;
}
