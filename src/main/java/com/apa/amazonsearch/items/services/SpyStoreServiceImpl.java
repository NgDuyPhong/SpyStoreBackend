package com.apa.amazonsearch.items.services;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.apa.amazonsearch.items.models.SpyStore;
import com.apa.amazonsearch.items.repositories.SpyStoreRepository;
import com.apa.users.models.User;
import com.apa.users.repositories.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class SpyStoreServiceImpl implements SpyStoreService {
	private final SpyStoreRepository spyStoreRepository;

	private final UserRepository userRepository;

	@Transactional
	public List<SpyStore> updateMaxPageByUserId(String userId, int maxPage) {
		List<SpyStore> spyStores = spyStoreRepository.findByUserId(userId);
		if (spyStores.isEmpty()) {
			throw new RuntimeException("SpyStore not found for user with id: " + userId);
		}

		for (SpyStore spyStore : spyStores) {
			spyStore.setMaxPage(maxPage);
		}

		return (List<SpyStore>) spyStoreRepository.saveAll(spyStores);
	}
	
	@Transactional
	public List<SpyStore> runSpyStore(String userName, int maxPage) {
		User user = userRepository.findFirstByUsername(userName);
		if (user == null) {
			throw new RuntimeException("User not found");
		}
		List<SpyStore> spyStores = spyStoreRepository.findByUserId(user.getId());
		
		for (SpyStore spyStore : spyStores) {
			spyStore.setMaxPage(maxPage);
			spyStore.setCurrentPage(0);
		}
		
		List<SpyStore> spyRes = (List<SpyStore>) spyStoreRepository.saveAll(spyStores);

		return spyRes;
	}
	
	@Transactional
	public List<SpyStore> getAllStoresByUsername(String userName) {
		User user = userRepository.findFirstByUsername(userName);
		if (user == null) {
			throw new RuntimeException("User not found");
		}
		List<SpyStore> spyStores = spyStoreRepository.findByUserId(user.getId());
		if (spyStores.size() <= 0) {
			return null;
		}
		return spyStores;
	}
	
	@Transactional
	public List<SpyStore> getAllStores(String userId) {
		List<SpyStore> spyStores = spyStoreRepository.findByUserId(userId);
		if (spyStores.size() <= 0) {
			return null;
		}
		return spyStores;
	}

	@Transactional
	public SpyStore addSpyStore(SpyStore spyStore) {
		User user = userRepository.findFirstByUsername(spyStore.getUser().getUsername());
		if (user == null) {
			throw new RuntimeException("User not found");
		}

		// Thiết lập người dùng cho spyStore
		spyStore.setUser(user);

		// Lưu SpyStore
		return spyStoreRepository.save(spyStore);
	}

}
