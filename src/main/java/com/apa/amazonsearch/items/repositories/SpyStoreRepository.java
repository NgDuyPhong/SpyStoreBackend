package com.apa.amazonsearch.items.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.apa.amazonsearch.items.models.SpyStore;


public interface SpyStoreRepository  extends CrudRepository<SpyStore, Long>{
	List<SpyStore> findAll();
	List<SpyStore> findByUserId(String userId);
}
