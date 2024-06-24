package com.apa.users.controllers;

import com.apa.amazonsearch.search_data.representation_models.SearchDataResponse;
import com.apa.configs.JwtResponse;
import com.apa.configs.JwtTokenUtil;
import com.apa.users.models.User;
import com.apa.users.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping(value = "/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserRepository userRepository;
    private final JwtTokenUtil jwtTokenUtil;

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "", method = RequestMethod.POST)
    @CrossOrigin
    public ResponseEntity<SearchDataResponse> addUser(@RequestBody User user) {
        user.setId(UUID.randomUUID().toString());
        userRepository.save(user);
        return this.fetchUsers();
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
    @CrossOrigin
    public ResponseEntity<SearchDataResponse> login(@RequestBody User user) {
        User existUser = userRepository.findFirstByUsername(user.getUsername());

        if (existUser == null || !existUser.getPassword().equalsIgnoreCase(user.getPassword())) {
            return new ResponseEntity(null, HttpStatus.UNPROCESSABLE_ENTITY);
        }

        String token = jwtTokenUtil.generateToken(user);

        EntityModel<JwtResponse> result = EntityModel.of(new JwtResponse(token));

        if (existUser.isAdmin()) {
            Link fetchUsersLink = linkTo(methodOn(UserController.class).fetchUsers()).withRel("fetch_users");
            result.add(fetchUsersLink);
        }

        result.add(Link.of(existUser.getUsername(), "username"));

        return new ResponseEntity(result, HttpStatus.OK);
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/revoke-token", method = RequestMethod.POST)
    @CrossOrigin
    public ResponseEntity<SearchDataResponse> revokeToken(@RequestBody String jwtToken) {
        String username = jwtTokenUtil.getUsernameFromToken(jwtToken);

        User existUser = userRepository.findFirstByUsername(username);

        String token = jwtTokenUtil.generateToken(existUser);

        EntityModel<JwtResponse> result = EntityModel.of(new JwtResponse(token));

        if (existUser.isAdmin()) {
            Link fetchUsersLink = linkTo(methodOn(UserController.class).fetchUsers()).withRel("fetch_users");
            result.add(fetchUsersLink);
        }

        result.add(Link.of(username, "username"));

        return new ResponseEntity(result, HttpStatus.OK);
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "", method = RequestMethod.PUT)
    @CrossOrigin
    public ResponseEntity<SearchDataResponse> updateUser(@RequestBody User user) {
        userRepository.save(user);
        return new ResponseEntity(user, HttpStatus.OK);
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "", method = RequestMethod.DELETE)
    @CrossOrigin
    public ResponseEntity<SearchDataResponse> deleteUser(@RequestBody User user) {
        userRepository.delete(user);

        return this.fetchUsers();
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "", method = RequestMethod.GET)
    @CrossOrigin
    public ResponseEntity<SearchDataResponse> fetchUsers() {
        List<User> users = userRepository.findAll();

        CollectionModel<User> result = CollectionModel.of(users);

        Link addUserLink = linkTo(methodOn(UserController.class).addUser(null)).withRel("add_user");
        result.add(addUserLink);

        Link updateUserLink = linkTo(methodOn(UserController.class).updateUser(null)).withRel("update_user");
        result.add(updateUserLink);

        Link deleteUserLink = linkTo(methodOn(UserController.class).deleteUser(null)).withRel("delete_user");
        result.add(deleteUserLink);

        return new ResponseEntity(result, HttpStatus.OK);
    }
}
