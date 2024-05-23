package com.apa.amazonsearch.items.threads;

import com.apa.amazonsearch.error_data.models.ErrorData;
import com.apa.amazonsearch.items.models.ItemDetail;
import com.apa.amazonsearch.items.models.ItemSearchResult;
import com.apa.amazonsearch.items.models.PagedItemDetailCollection;
import com.apa.amazonsearch.page_reader.AmazonPageReader;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

@Data
@NoArgsConstructor
@Slf4j
public class ProductThread implements Runnable {
    ItemSearchResult itemSearchResult;
    PagedItemDetailCollection pagedItemDetailCollection;

    public ProductThread(ItemSearchResult itemSearchResult, PagedItemDetailCollection pagedItemDetailCollection) {
        this.itemSearchResult = itemSearchResult;
        this.pagedItemDetailCollection = pagedItemDetailCollection;
    }

    @SneakyThrows
    @Override
    public void run() {
        ItemDetail itemDetail = new AmazonPageReader().readItemDetail(itemSearchResult);

        if (itemDetail != null) {
            itemDetail.setSearchDataId(itemSearchResult.getSearchDataId());

            if (itemDetail.getProductId() != null) {
                this.pagedItemDetailCollection.getItemDetails().add(itemDetail);
            }
        } else {
            ErrorData errorData = new ErrorData();
            errorData.setProductId(itemSearchResult.getProductId());
            errorData.setId(UUID.randomUUID().toString());
            errorData.setPageUrl(itemSearchResult.getProductUrl());
            this.pagedItemDetailCollection.addErrorPage(errorData);
        }
    }
}
