package com.apa.amazonsearch.search_processing.models;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
@NoArgsConstructor
public class SearchProcessing {
    @Id
    @Column(name = "id", nullable = false)
    private String id;

    String searchId;

    Integer page;

    Integer maxPage;

    String userId;
}
