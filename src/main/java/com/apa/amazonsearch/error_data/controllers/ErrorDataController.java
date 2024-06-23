package com.apa.amazonsearch.error_data.controllers;

import com.apa.amazonsearch.error_data.models.ErrorData;
import com.apa.amazonsearch.error_data.services.ErrorDataService;
import com.apa.users.models.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ErrorDataController {
    private final ErrorDataService errorDataService = null;

    @GetMapping("/v1/errors")
    @CrossOrigin
    public ResponseEntity<List<ErrorData>> getErrors() {
        List<ErrorData> errors = errorDataService.getErrors(getUser());

        return new ResponseEntity(errors, HttpStatus.OK);
    }

    private User getUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
