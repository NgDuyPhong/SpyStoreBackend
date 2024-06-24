package com.apa.amazonsearch.items.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.apa.amazonsearch.items.models.SpyStore;
import com.apa.amazonsearch.items.repositories.SpyStoreRepository;
import com.apa.amazonsearch.items.representation_models.SpyStoreDeleteRequest;
import com.apa.amazonsearch.items.services.SpyStoreServiceImpl;
import com.apa.amazonsearch.search_data.representation_models.SearchDataResponse;
import com.apa.users.models.User;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(value = "/spy-store")
@RequiredArgsConstructor
@Slf4j
public class SpyStoreController {
	private final SpyStoreRepository spyStoreRepository;
	private final SpyStoreServiceImpl spyStoreService;

	@ResponseStatus(HttpStatus.OK)
	@RequestMapping(value = "add", method = RequestMethod.POST)
	@CrossOrigin
	public ResponseEntity<SpyStore> addSpyStore(@RequestBody SpyStore spyStore) {
		SpyStore savedSpyStore = spyStoreService.addSpyStore(spyStore);
		return ResponseEntity.ok(savedSpyStore);
	}

	@ResponseStatus(HttpStatus.OK)
	@RequestMapping(value = "all/{username}", method = RequestMethod.GET)
	@CrossOrigin
	public ResponseEntity<List<SpyStore>> getAllSpyStores(@PathVariable String username) {
		List<SpyStore> spyStores = spyStoreService.getAllStoresByUsername(username);
		return ResponseEntity.ok(spyStores);
	}

	@ResponseStatus(HttpStatus.OK)
	@RequestMapping(value = "{id}", method = RequestMethod.GET)
	@CrossOrigin
	public ResponseEntity<SpyStore> getSpyStoreById(@PathVariable Long id) {
		SpyStore spyStore = spyStoreRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("SpyStore not found with id: " + id));
		return ResponseEntity.ok(spyStore);
	}
	
	@ResponseStatus(HttpStatus.OK)
	@RequestMapping(value = "{id}", method = RequestMethod.DELETE)
	@CrossOrigin
	public ResponseEntity<Void> deleteSpyStoreById(@PathVariable Long id) {
	    spyStoreRepository.findById(id)
	            .orElseThrow(() -> new RuntimeException("SpyStore not found with id: " + id));
	    spyStoreRepository.deleteById(id);
	    return ResponseEntity.noContent().build();
	}

	@ResponseStatus(HttpStatus.OK)
	@RequestMapping(value = "{id}", method = RequestMethod.PUT)
	@CrossOrigin
	public ResponseEntity<SpyStore> updateUser(@PathVariable Long id, @RequestBody SpyStore updatedSpyStore) {
		SpyStore existingSpyStore = spyStoreRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("SpyStore not found with id: " + id));
		existingSpyStore.setName(updatedSpyStore.getName());
		existingSpyStore.setUrl(updatedSpyStore.getUrl());
		existingSpyStore.setCurrentPage(updatedSpyStore.getCurrentPage());
		existingSpyStore.setMaxPage(updatedSpyStore.getMaxPage());

		SpyStore savedSpyStore = spyStoreRepository.save(existingSpyStore);
		return ResponseEntity.ok(savedSpyStore);
	}

	@CrossOrigin
	@ResponseStatus(HttpStatus.OK)
	@RequestMapping(value = "updateMaxPage", method = RequestMethod.PUT)
	public ResponseEntity<List<SpyStore>> updateMaxPageByUserId(@RequestBody String userId, @RequestBody int maxPage) {
		List<SpyStore> updatedSpyStores = spyStoreService.updateMaxPageByUserId(userId, maxPage);
		return ResponseEntity.ok(updatedSpyStores);
	}
	
	@CrossOrigin
	@ResponseStatus(HttpStatus.OK)
	@RequestMapping(value = "run", method = RequestMethod.POST)
	public ResponseEntity<List<SpyStore>> runSpyStore(@RequestBody SpyStoreDeleteRequest request) {
		List<SpyStore> spyStore = spyStoreService.runSpyStore(request.getUsername(), request.getMaxPage());
		return ResponseEntity.ok(spyStore);
	}
	
	@CrossOrigin
	@ResponseStatus(HttpStatus.OK)
	@RequestMapping(value = "getStores/{id}", method = RequestMethod.GET)
	public ResponseEntity<List<SpyStore>> getAllStores(@PathVariable String id) {
		List<SpyStore> spyStore = spyStoreService.getAllStores(id);
		return ResponseEntity.ok(spyStore);
	}

}
