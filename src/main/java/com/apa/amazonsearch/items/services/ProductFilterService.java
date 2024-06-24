package com.apa.amazonsearch.items.services;

import com.apa.amazonsearch.items.models.ItemDetail;
import com.apa.amazonsearch.items.models.PagedItemDetailCollection;
import com.apa.amazonsearch.items.representation_models.ProductFilterRequest;
import com.apa.amazonsearch.items.representation_models.ProductResultFilter;

import java.io.IOException;
import java.util.List;

public interface ProductFilterService {
    PagedItemDetailCollection findProductsFromAmazon(ProductFilterRequest productFilterRequest) throws IOException;

    List<ItemDetail> findFilteredProducts(String searchId);

    ItemDetail favoriteItem(String itemId);

    ItemDetail unfavoriteItem(String itemId);

    void deleteAllResults(String id);

    List<ItemDetail> findFilteredProducts(ProductResultFilter productResultFilter);
}
