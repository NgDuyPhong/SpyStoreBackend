package com.apa.amazonsearch.items.repositories;

import com.apa.amazonsearch.items.models.ProductScanHistory;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ProductScanHistoryRepository extends CrudRepository<ProductScanHistory, String> {
    List<ProductScanHistory> findByUserId(String userId);

    void deleteByUserId(String id);
}
