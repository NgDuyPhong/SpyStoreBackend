package com.apa.users.data_initialization;

import com.apa.users.models.User;
import com.apa.users.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UserDataLoader implements ApplicationRunner {
	private final UserRepository userRepository;

	@Autowired
	public UserDataLoader(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public void run(ApplicationArguments args) {
		User existingUser = userRepository.findFirstByUsername("admin");
		if (existingUser == null) {
			User user = User.builder().id(UUID.randomUUID().toString()).username("admin").password("password").build();
			userRepository.save(user);
		}

	}
}
