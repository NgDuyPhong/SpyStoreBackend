package com.apa.extensions;

import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
public interface ProductDetailRepository extends CrudRepository<ProductDetail, String> {
    List<ProductDetail> findAll();

    List<ProductDetail> findByUsername(String username);

    void deleteByUsername(String username);
}
