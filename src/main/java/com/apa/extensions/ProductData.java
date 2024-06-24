package com.apa.extensions;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.parameters.P;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Data
@NoArgsConstructor
@Slf4j
public class ProductData {
    private String rank;
    private String reviews;
    private List<Category> categories;
    private String asin;
    private String brand;
    private String added;
    private String title;
    private String image;
    private String productUrl;

    @SneakyThrows
    public ProductDetail toItemDetail() {
        ProductDetail itemDetail = new ProductDetail();

        itemDetail.setId(UUID.randomUUID().toString());
        itemDetail.setProductId(this.asin);
        itemDetail.setAsin(this.asin);
        itemDetail.setTitle(this.title);

        initRankOfItemDetail(itemDetail);

        itemDetail.setMainImage(this.image);

        if (this.reviews == null || this.reviews.toLowerCase().contains("n/a")) {
            itemDetail.setReviewsText(this.reviews);
        } else if (!"".equals(this.reviews)) {
            Double review = Double.valueOf(this.reviews.substring(0, 3)
                .replace(",", "."));
            itemDetail.setReview(review);

            String numberOfReviewStr = this.reviews.substring(5, this.reviews.length() - 1)
                    .replace(",", "")
                    .replace(".", "")
                    .replace("[", "")
                    .replace("]", "");
            Long numOfReviews = Long.valueOf(numberOfReviewStr);
            itemDetail.setNumberOfReviews(numOfReviews);
            itemDetail.setReviewsText(this.reviews);
        }

        itemDetail.setDateFirstAvailable(this.added);
        itemDetail.setAddedDate(this.getDateAvailableUnixTime());
        itemDetail.setProductUrl(this.productUrl);
        itemDetail.setBrand(this.brand);

        return itemDetail;
    }

    private void initRankOfItemDetail(ProductDetail itemDetail) {
        if (this.rank.toLowerCase().contains("n/a")) {
            itemDetail.setRankText(this.rank);
        } else if (!"".equals(this.rank)) {
            String rankIntegerStr = this.rank.substring(1).replace("\n", "")
                .replace(",", "")
                .replace(".", "");
            Integer rank = Integer.valueOf(rankIntegerStr);
            itemDetail.setRank(rank);
            itemDetail.setRankText(this.rank);
        }

        if (this.categories.stream().anyMatch(category -> category.getCategory().toLowerCase().contains("climate"))) {
            Integer climateRank = itemDetail.getRank();
            itemDetail.setRank(climateRank * 10);
            itemDetail.setRankText(formatNumber(itemDetail.getRank()));
            itemDetail.setIsClimateRank(true);
            itemDetail.setClimateRank(climateRank);
            itemDetail.setClimateRankText(this.rank);
        }
    }

    private Long getDateAvailableUnixTime() throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("MMMMM dd, yyyy HH:mm:ss");
        if (!"N/A".equalsIgnoreCase(this.added) && this.added != null) {
            Date date = format.parse(this.added + " 00:00:00");
            return date.getTime();
        }

        return null;
    }

    public static String formatNumber(int number) {
        return String.format(Locale.US, "# %,d", number);
    }

    public static class Category {
        private String category;
        private String rank;

        public Category(String category, String rank) {
            this.category = category;
            this.rank = rank;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public String getRank() {
            return rank;
        }

        public void setRank(String rank) {
            this.rank = rank;
        }
    }
}

