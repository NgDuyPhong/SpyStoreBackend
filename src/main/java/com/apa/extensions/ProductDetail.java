package com.apa.extensions;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@Data
@NoArgsConstructor
@Entity(name = "product_detail")
@Slf4j
public class ProductDetail {
    @Id
    @Column(name = "id", nullable = false)
    private String id;

    private String username;

    String productId;
    String asin;

    @Column( length = 100000 )
    String title;

    private String mainImage;

    private String reviewsText;

    private Double review;

    private Long numberOfReviews = 0L;

    private String rankText;

    private String climateRankText;

    private Integer rank;

    private Integer climateRank;

    private String dateFirstAvailable;

    private Long addedDate;

    private String brand;

    private Boolean isClimateRank = false;

    private String manufacturer;
    @Column( length = 100000 )
    private String productUrl;
    Boolean favorite;

    @SneakyThrows
    public boolean matchSearch(SearchRequest searchRequest) {
        boolean match = true;

        if (searchRequest.getHigherThanRank() != null) {
            match = match && this.rank >= searchRequest.getHigherThanRank();
        }

        if (searchRequest.getLessThanRank() != null) {
            match = match && this.rank < searchRequest.getLessThanRank();
        }

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        if (searchRequest.getAfterDate() != null && !"".equals(searchRequest.getAfterDate())) {
            match = match && this.addedDate >= dateFormat.parse(searchRequest.getAfterDate()).getTime();
        }

        if (searchRequest.getBeforeDate() != null && !"".equals(searchRequest.getBeforeDate())) {
            match = match && this.addedDate < dateFormat.parse(searchRequest.getBeforeDate()).getTime();
        }

        if (this.numberOfReviews != null && searchRequest.getHigherThanReview() != null) {
            match = match && this.numberOfReviews >= searchRequest.getHigherThanReview();
        }

        if (this.numberOfReviews != null && searchRequest.getLessThanReview() != null) {
            match = match && this.numberOfReviews < searchRequest.getLessThanReview();
        }

        return match;
    }

    public boolean isClimateRank() {
        return Boolean.TRUE.equals(this.isClimateRank);
    }
}



