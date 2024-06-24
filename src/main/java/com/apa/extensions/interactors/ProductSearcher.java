package com.apa.extensions.interactors;

import com.apa.extensions.ProductDetail;
import com.apa.extensions.SearchRequest;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ProductSearcher {
    List<ProductDetail> productDetails;

    public ProductSearcher(List<ProductDetail> productDetails) {
        this.productDetails = productDetails;
    }

    public List<ProductDetail> search(SearchRequest searchRequest) {
        List<ProductDetail> distinctProducts = productDetails
            .stream()
            .filter(productDetail -> productDetail.getRank() != null)
            .filter(distinctByKey(ProductDetail::getProductId))
            .toList();

        List<ProductDetail> results = distinctProducts
            .stream()
            .filter(productDetail -> productDetail.matchSearch(searchRequest))
            .collect(Collectors.toList());

        return results;
    }

    public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Set<Object> seen = ConcurrentHashMap.newKeySet();
        return t -> seen.add(keyExtractor.apply(t));
    }
}
