package com.apa.amazonsearch.search_data.controllers;

import com.apa.amazonsearch.search_data.models.SearchData;
import com.apa.amazonsearch.search_data.repositories.SearchDataRepository;
import com.apa.amazonsearch.search_data.representation_models.SearchDataResponse;
import com.apa.amazonsearch.search_processing.models.SearchProcessing;
import com.apa.amazonsearch.search_processing.repositories.SearchProcessingRepository;
import com.apa.users.models.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/amazon-search-history")
@RequiredArgsConstructor
@Slf4j
public class SearchDataController {
    private final SearchDataRepository searchDataRepository;
    private final SearchProcessingRepository searchProcessingRepository;

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "", method = RequestMethod.GET)
    @CrossOrigin
    public ResponseEntity<SearchDataResponse> findSearchHistory() {
        List<SearchData> searchDatas = searchDataRepository.findByUserId(getUser().getId());

        List<String> searchIds = searchDatas.stream().map(SearchData::getId).collect(Collectors.toList());
        List<SearchProcessing> searchProcessings = searchProcessingRepository.findBySearchIdIn(searchIds);

        List<SearchDataResponse> results = searchDatas.stream().map(searchData -> new SearchDataResponse(searchData, searchProcessings)).collect(Collectors.toList());

        return new ResponseEntity(results, HttpStatus.OK);
    }

    private User getUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
