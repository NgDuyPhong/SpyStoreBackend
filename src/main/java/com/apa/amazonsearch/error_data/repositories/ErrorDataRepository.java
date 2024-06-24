package com.apa.amazonsearch.error_data.repositories;

import com.apa.amazonsearch.error_data.models.ErrorData;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ErrorDataRepository extends CrudRepository<ErrorData, String> {
    List<ErrorData> findByUserId(String id);

    void deleteByUserId(String id);
}
