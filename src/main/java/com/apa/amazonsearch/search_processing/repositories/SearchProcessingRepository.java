package com.apa.amazonsearch.search_processing.repositories;

import com.apa.amazonsearch.search_processing.models.SearchProcessing;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SearchProcessingRepository extends CrudRepository<SearchProcessing, String> {
    List<SearchProcessing> findBySearchIdIn(List<String> searchIds);

    void deleteByUserId(String id);
}
