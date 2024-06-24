package com.apa.amazonsearch.items.repositories;

import com.apa.amazonsearch.items.models.ItemDetail;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ItemDetailRepository extends CrudRepository<ItemDetail, String> {
    List<ItemDetail> findBySearchDataId(String searchId);

    List<ItemDetail> findBySearchDataIdIn(List<String> searchIds);

    List<ItemDetail> findBySearchDataIdInAndUserId(List<String> searchIds, String userId);

    List<ItemDetail> findByUserId(String userId);

    void deleteByUserId(String id);
}
