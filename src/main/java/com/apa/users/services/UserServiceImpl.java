package com.apa.users.services;

import com.apa.users.models.User;
import com.apa.users.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public User findUserByUsername(String username) {
        return userRepository.findFirstByUsername(username);
    }
}
