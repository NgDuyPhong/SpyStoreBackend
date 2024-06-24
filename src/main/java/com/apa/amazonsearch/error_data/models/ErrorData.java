package com.apa.amazonsearch.error_data.models;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
public class ErrorData {
    @Id
    @Column(name = "id", nullable = false)
    private String id;

    String pageUrl;

    String userId;

    String productId;
}
