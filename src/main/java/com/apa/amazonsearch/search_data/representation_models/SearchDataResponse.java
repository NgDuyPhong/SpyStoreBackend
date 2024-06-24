package com.apa.amazonsearch.search_data.representation_models;

import com.apa.amazonsearch.search_data.models.SearchData;
import com.apa.amazonsearch.search_processing.models.SearchProcessing;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Data
public class SearchDataResponse extends SearchData {
    Integer page;

    Integer maxPage;

    public SearchDataResponse(SearchData searchData, List<SearchProcessing> searchProcesses) {
        this.setId(searchData.getId());
        this.setSearchQuery(searchData.getSearchQuery());

        if (searchProcesses.size() == 0) {
            this.page = 0;
            this.maxPage = 0;
            return;
        }

        this.page = searchProcesses
            .stream()
            .filter(searchProcessing -> searchProcessing.getSearchId().equalsIgnoreCase(searchData.getId()))
            .mapToInt(SearchProcessing::getPage)
            .max().getAsInt();

        List<SearchProcessing> searchProcessesOfSearchData = searchProcesses
            .stream()
            .filter(searchProcessing -> searchProcessing.getSearchId().equalsIgnoreCase(searchData.getId()))
            .toList();

        if (searchProcessesOfSearchData.size() > 0) {
            this.maxPage = searchProcessesOfSearchData
                .stream()
                .mapToInt(SearchProcessing::getMaxPage).max().getAsInt();
        } else {
            this.maxPage = 0;
        }
    }
}
