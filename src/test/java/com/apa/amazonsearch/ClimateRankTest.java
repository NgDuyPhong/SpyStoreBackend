package com.apa.amazonsearch;

import com.apa.extensions.ProductData;
import com.apa.extensions.ProductDetail;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class ClimateRankTest {

    @Test
    public void Test_Climate_Rank() {
        // given
        ProductData productData = new ProductData();
        ArrayList<ProductData.Category> categories = new ArrayList<>();
        categories.add(new ProductData.Category("Climate Pledge Friendly", "# 551,973"));
        productData.setCategories(categories);

        productData.setRank("#\n551,973");

        // when
        ProductDetail productDetail = productData.toItemDetail();

        // then
        Assertions.assertTrue(productDetail.isClimateRank());
        Assertions.assertEquals(5519730, productDetail.getRank());
        Assertions.assertEquals("# 5,519,730", productDetail.getRankText());
        Assertions.assertEquals(551973, productDetail.getClimateRank());
    }
}
