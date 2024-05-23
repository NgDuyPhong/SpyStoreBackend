package com.apa.users.repositories;

import com.apa.users.models.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserRepository extends CrudRepository<User, String> {
    List<User> findAll();
    User findFirstByUsername(String username);
}
