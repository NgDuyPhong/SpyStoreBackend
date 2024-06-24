package com.apa.amazonsearch.items.models;

import com.apa.amazonsearch.items.representation_models.ProductFilter;
import com.apa.amazonsearch.items.representation_models.ProductFilterRequest;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.BeanUtils;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@Data
@NoArgsConstructor
@Entity(name = "product_scan_history")
public class ProductScanHistory {
    @Id
    @Column(name = "id", nullable = false)
    private String id;

    String productId;

    String userId;

    String asin;

    public Integer ratingsTotal;
    public Integer bestSellerRank;
    public String rankText;
    public String dateFirstAvailable;
    public String manufacturer;

    String searchDataId;
    public String mainImage;

    @Column( length = 100000 )
    String title;

    @Column( length = 100000 )
    private String productUrl;

    Boolean existInItemList = false;

    public ProductScanHistory(ItemDetail item, ProductFilterRequest productFilterRequest) {
        this.id = UUID.randomUUID().toString();
        this.productId = item.getProductId();
        this.userId = item.getUserId();
        this.asin = item.getAsin();
        this.bestSellerRank = item.getBestSellerRank();
        this.rankText = item.getRankText();
        this.dateFirstAvailable = item.getDateFirstAvailable();
        this.manufacturer = item.getManufacturer();
        this.ratingsTotal = item.getRatingsTotal();
        this.searchDataId = item.getSearchDataId();
        this.mainImage = item.getMainImage();
        this.productUrl = item.getProductUrl();
        this.title = item.getTitle();

        this.existInItemList = this.matchCondition(productFilterRequest);
    }

    @SneakyThrows
    public boolean matchCondition(ProductFilterRequest filterRequest) {
        if (filterRequest.getSecondFilter() != null && filterRequest.getSecondFilter().getRank() != null) {
            return isFilterValid(filterRequest.getFirstFilter()) || isFilterValid(filterRequest.getSecondFilter());
        }

        return isFilterValid(filterRequest.getFirstFilter());
    }

    private boolean isFilterValid(ProductFilter filterRequest) throws ParseException {
        ProductFilter firstFilter = filterRequest;

        boolean isRankValid = true;
        if (firstFilter.getRank() != null) {
            isRankValid = this.bestSellerRank != null && this.bestSellerRank > 0 && this.bestSellerRank <= firstFilter.getRank();
        }

        boolean isDateValid = true;
        if (firstFilter.getDateWithin() != null) {
            isDateValid = this.dateFirstAvailable  == null ||  (getDateAvailableUnixTime() - getDateWithinUnixTime(firstFilter) >= 0);
        }

        boolean isReviewValid = true;
        if (firstFilter.getReview() != null) {
            isReviewValid =  this.ratingsTotal  == null ||  this.ratingsTotal <= firstFilter.getReview();
        }

        return isRankValid && isDateValid && isReviewValid;
    }

    @SneakyThrows
    private long getDateAvailableUnixTime() {
        SimpleDateFormat format = new SimpleDateFormat("MMMMM dd, yyyy HH:mm:ss");
        Date date = format.parse(this.dateFirstAvailable + " 00:00:00");
        long timestamp = date.getTime();
        return timestamp;
    }

    private long getDateWithinUnixTime(ProductFilter productFilter) {
        long today = new Date().getTime();

        long milliseconds = Long.valueOf(productFilter.getDateWithin()) * 24l * 60l * 60l * 1000l;
        Long dateWithinMilliseconds = Long.valueOf(milliseconds);
        return today - dateWithinMilliseconds;
    }
}
