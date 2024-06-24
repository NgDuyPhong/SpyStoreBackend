package com.apa.amazonsearch.search_data.repositories;

import com.apa.amazonsearch.search_data.models.SearchData;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SearchDataRepository extends CrudRepository<SearchData, String> {
    List<SearchData> findAll();

    List<SearchData> findByUserId(String id);

    void deleteByUserId(String id);
}
