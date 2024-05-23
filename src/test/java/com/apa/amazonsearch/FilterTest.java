package com.apa.amazonsearch;

import com.apa.extensions.ProductDetail;
import com.apa.extensions.SearchRequest;
import com.apa.extensions.interactors.ProductSearcher;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FilterTest {
    @Test
    public void Test_Filter_Data() throws ParseException {
        // given
        List<ProductDetail> productDetails = new ArrayList<>();
        productDetails.add(buildProduct(
            "product-1",
            "December 30, 2022",
            4.3,
            200
        ));
        productDetails.add(buildProduct(
            "product-2",
            "December 30, 2023",
            4D,
            50
        ));
        productDetails.add(buildProduct(
            "product-3",
            "December 30, 2024",
            3D,
            1000
        ));

        SearchRequest searchRequest = new SearchRequest();
        searchRequest.setAfterDate("2022-12-31");
        searchRequest.setBeforeDate("2024-12-31");

        // when
        ProductSearcher productSearcher = new ProductSearcher(productDetails);
        List<ProductDetail> results = productSearcher.search(searchRequest);

        // then
        Assertions.assertEquals(2, results.size());
    }

    private ProductDetail buildProduct(
        String id,
        String dateFirstAvailable,
        Double review,
        Integer rank
    ) throws ParseException {
        ProductDetail productDetail = new ProductDetail();

        productDetail.setId(id);
        productDetail.setProductId(id);
        productDetail.setDateFirstAvailable(dateFirstAvailable);
        productDetail.setAddedDate(getDateAvailableUnixTime(dateFirstAvailable));
        productDetail.setReview(review);
        productDetail.setRank(rank);

        return productDetail;
    }

    private Long getDateAvailableUnixTime(String dateStr) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("MMMMM dd, yyyy HH:mm:ss");
        if (!"N/A".equalsIgnoreCase(dateStr) && dateStr != null) {
            Date date = format.parse(dateStr + " 00:00:00");
            return date.getTime();
        }

        return null;
    }
}
