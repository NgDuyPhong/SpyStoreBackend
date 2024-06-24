package com.apa.amazonsearch.items.models;

import com.apa.amazonsearch.items.representation_models.ProductFilter;
import com.apa.amazonsearch.items.representation_models.ProductFilterRequest;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@Data
@NoArgsConstructor
@Entity(name = "item_detail")
@Slf4j
public class ItemDetail {
    @Id
    @Column(name = "id", nullable = false)
    private String id;

    String productId;
    String asin;

    @Column( length = 100000 )
    String title;
    String searchDataId;
    public String mainImage;
    public Integer ratingsTotal;
    public Integer bestSellerRank;
    public String rankText;
    public String dateFirstAvailable;
    public String manufacturer;
    @Column( length = 100000 )
    private String productUrl;
    Boolean favorite;

    String userId;

    public ItemDetail(Element productPage, ItemSearchResult itemSearchResult) {
        if (productPage == null) {
            return;
        }

        this.id = UUID.randomUUID().toString();
        this.productId = itemSearchResult.getProductId();

        Element itemData = productPage.getElementById("detailBulletsWrapper_feature_div");

        if (itemData != null) {
            this.productUrl = itemSearchResult.getProductUrl();

            setDateFirstAvailable(itemData);

            setBestSellerRank(itemData);

            setManufacturer(itemData);

            setASIN(itemData);

            setRatingTotal(itemData);

            setMainImage(productPage);

            setProductTitle(productPage);
        } else {
            itemData = productPage.getElementById("productDetails_detailBullets_sections1");

            if (itemData != null) {
                this.productUrl = itemSearchResult.getProductUrl();
                setDateFirstAvailableTablet(itemData);
                setASINTablet(itemData);
                setBestSellerRankTablet(itemData);
                setProductTitle(productPage);
                setMainImage(productPage);

                setManufacturerTablet(productPage);
                setRatingTotalTablet(productPage);
            }
        }
    }

    public ItemDetail(ProductScanHistory productScanHistory) {
        this.id = UUID.randomUUID().toString();
        this.productId = productScanHistory.getProductId();
        this.userId = productScanHistory.getUserId();
        this.asin = productScanHistory.getAsin();
        this.bestSellerRank = productScanHistory.getBestSellerRank();
        this.dateFirstAvailable = productScanHistory.getDateFirstAvailable();
        this.manufacturer = productScanHistory.getManufacturer();
        this.ratingsTotal = productScanHistory.getRatingsTotal();
        this.searchDataId = productScanHistory.getSearchDataId();
        this.mainImage = productScanHistory.getMainImage();
        this.productUrl = productScanHistory.getProductUrl();
        this.title = productScanHistory.getTitle();
        this.rankText = productScanHistory.getRankText();
    }

    private void setRatingTotalTablet(Element productPage) {
        Element reviewData = productPage.getElementById("acrCustomerReviewText");
        if (reviewData == null) {
            return;
        }

        String reviewStr = reviewData.text();
        reviewStr = reviewStr.split(" ")[0];
        reviewStr = reviewStr.replace(",", "");
        this.ratingsTotal = Integer.valueOf(reviewStr);
    }

    private void setManufacturerTablet(Element productPage) {
        Element techElement = productPage.getElementById("productDetails_techSpec_section_1");

        if (techElement == null) {
            return;
        }

        Elements rows = techElement.getElementsByTag("tr");

        Element asinRow = rows.stream().filter(row -> row.firstElementChild().text().toLowerCase().contains("Manufacturer".toLowerCase())).findFirst().orElse(null);

        if (asinRow != null) {
            Elements columns = asinRow.getElementsByTag("td");
            Element manufacturerColumn = columns.stream().findFirst().orElse(null);

            if (manufacturerColumn != null) {
                this.manufacturer = manufacturerColumn.text();
            }
        }
    }

    private void setASINTablet(Element itemData) {
        Elements rows = itemData.getElementsByTag("tr");

        Element asinRow = rows.stream().filter(row -> row.firstElementChild().text().toLowerCase().contains("ASIN".toLowerCase())).findFirst().orElse(null);

        if (asinRow != null) {
            Elements columns = asinRow.getElementsByTag("td");
            Element asinColumn = columns.stream().findFirst().orElse(null);

            if (asinColumn != null) {
                this.asin = asinColumn.text();
            }
        }
    }

    private void setDateFirstAvailableTablet(Element itemData) {
        Elements rows = itemData.getElementsByTag("tr");

        Element asinRow = rows.stream().filter(row -> row.firstElementChild().text().toLowerCase().contains("Date First Available".toLowerCase())).findFirst().orElse(null);

        if (asinRow != null) {
            Elements columns = asinRow.getElementsByTag("td");
            Element dateFirstAvailableColumn = columns.stream().findFirst().orElse(null);

            if (dateFirstAvailableColumn != null) {
                this.dateFirstAvailable = dateFirstAvailableColumn.text();
            }
        }
    }
    private void setBestSellerRankTablet(Element itemData) {
        Elements rows = itemData.getElementsByTag("tr");

        Element asinRow = rows.stream().filter(row -> row.firstElementChild().text().toLowerCase().contains("Best Sellers Rank".toLowerCase())).findFirst().orElse(null);

        if (asinRow != null) {
            Elements columns = asinRow.getElementsByTag("td");
            Element bestSellerRankColumn = columns.stream().findFirst().orElse(null);

            if (bestSellerRankColumn != null) {
                String bestSellerRankStr = bestSellerRankColumn.text();
                this.rankText = bestSellerRankStr;
                bestSellerRankStr = bestSellerRankStr.replace("#", "");
                bestSellerRankStr = bestSellerRankStr.replace(",", "");
                bestSellerRankStr = bestSellerRankStr.split(" ")[0];
                this.bestSellerRank = Integer.valueOf(bestSellerRankStr);
            }
        }
    }

    private void setASIN(Element itemData) {
        Element productInformation = itemData.getElementById("detailBullets_feature_div");
        Elements listDetails = productInformation.getElementsByClass("a-list-item");

        Element asinElement = listDetails
            .stream()
            .filter(detail -> detail.firstElementChild() != null
                && detail.firstElementChild().text().toLowerCase().contains("ASIN".toLowerCase()))
            .findFirst()
            .orElse(null);

        if (asinElement != null) {
            this.asin = asinElement.lastElementChild().text();
        }
    }

    private void setProductTitle(Element productPage) {
        Element productTitle = productPage.getElementById("productTitle");
        String text = productTitle.text();
        this.title = text;
    }

    private void setMainImage(Element productPage) {
        Element landingImage = productPage.getElementById("landingImage");
        String src = landingImage.attr("src");
        this.mainImage = src;
    }

    private void setRatingTotal(Element itemData) {
        Element reviewData = itemData.getElementById("acrCustomerReviewText");
        if (reviewData == null) {
            return;
        }

        String reviewStr = reviewData.text();
        reviewStr = reviewStr.split(" ")[0];
        reviewStr = reviewStr.replace(",", "");
        this.ratingsTotal = Integer.valueOf(reviewStr);
    }

    private void setDateFirstAvailable(Element itemData) {
        Element productInformation = itemData.getElementById("detailBullets_feature_div");
        Elements listDetails = productInformation.getElementsByClass("a-list-item");

        Element dateFirstAvailableElement = listDetails
            .stream()
            .filter(detail -> detail.firstElementChild() != null
                && detail.firstElementChild().text().toLowerCase().contains("Date first available".toLowerCase()))
            .findFirst()
            .orElse(null);

        if (dateFirstAvailableElement != null) {
            this.dateFirstAvailable = dateFirstAvailableElement.lastElementChild().text();
        }
    }

    private void setManufacturer(Element itemData) {
        Element productInformation = itemData.getElementById("detailBullets_feature_div");
        Elements listDetails = productInformation.getElementsByClass("a-list-item");

        Element manufacturerElement = listDetails
            .stream()
            .filter(detail -> detail.firstElementChild() != null
                && detail.firstElementChild().text().toLowerCase().contains("Manufacturer".toLowerCase()))
            .findFirst()
            .orElse(null);

        if (manufacturerElement != null) {
            this.manufacturer = manufacturerElement.lastElementChild().text();
        }
    }

    private void setBestSellerRank(Element itemData) {
        Elements spanElements = itemData.getElementsByClass("a-list-item");
        Element bestSellerRankElement = spanElements.stream().filter(node -> node.outerHtml().toLowerCase().contains("Best Sellers Rank:".toLowerCase()))
            .findFirst().orElse(null);

        if (bestSellerRankElement == null) {
            return;
        }

        String bestSellerRankStr = bestSellerRankElement.childNodes().get(2).outerHtml();
        this.rankText = bestSellerRankStr;

        bestSellerRankStr = bestSellerRankStr.replace("#", "");
        bestSellerRankStr = bestSellerRankStr.replace(",", "");
        bestSellerRankStr = bestSellerRankStr.split(" ")[1];
        this.bestSellerRank = Integer.valueOf(bestSellerRankStr);
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

    public boolean isExist(ProductScanHistory productScanHistory) {
        return this.getProductId().equalsIgnoreCase(productScanHistory.getProductId()) || Boolean.TRUE.equals(productScanHistory.existInItemList);
    }
}



