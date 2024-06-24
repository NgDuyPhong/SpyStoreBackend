package com.apa.amazonsearch.items.representation_models;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SpyStoreDeleteRequest {
    String username;
    int maxPage;
}
