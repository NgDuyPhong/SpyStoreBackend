package com.apa.amazonsearch.read_page;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ReadPageTest {
    @Test
    public void test_1() {
        // given
        String reviewText = "5.0 (3)";

        // when
        Integer numOfReviews = Integer.valueOf(reviewText.substring(5, reviewText.length() - 1));

        // then
        Assertions.assertEquals(3, numOfReviews);
    }

    @Test
    public void test_2() {
        // given
        String reviewText = "4.6 (48)";

        // when
        Integer numOfReviews = Integer.valueOf(reviewText.substring(5, reviewText.length() - 1));

        // then
        Assertions.assertEquals(48, numOfReviews);
    }

    @Test
    public void test_3() {
        // given
        String reviewText = "4.8 (397)";

        // when
        Integer numOfReviews = Integer.valueOf(reviewText.substring(5, reviewText.length() - 1));

        // then
        Assertions.assertEquals(397, numOfReviews);
    }
}
