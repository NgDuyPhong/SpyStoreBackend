package com.apa.amazonsearch.items.representation_models;

import com.apa.amazonsearch.items.models.ItemDetail;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.hateoas.RepresentationModel;

@Data
@NoArgsConstructor
public class ProductResponse extends RepresentationModel<ProductResponse> {
    private String id;

    String productId;
    String title;
    String searchDataId;
    public String mainImage;
    public Integer ratingsTotal;
    public Integer bestSellerRank;
    public String dateFirstAvailable;
    public String manufacturer;
    private String productUrl;
    Boolean favorite;
    String asin;
    String price;
    String rankText;

    public ProductResponse(ItemDetail product) {
        BeanUtils.copyProperties(product, this);
    }
}
