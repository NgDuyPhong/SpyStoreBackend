package com.apa.amazonsearch.search_data.models;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@NoArgsConstructor
@Entity
public class SearchData {
    @Id
    @Column(name = "id", nullable = false)
    private String id;

    String searchQuery;

    String userId;
}
