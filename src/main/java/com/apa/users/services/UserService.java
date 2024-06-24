package com.apa.users.services;

import com.apa.users.models.User;

public interface UserService {
    User findUserByUsername(String username);
}
