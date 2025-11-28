package com.knittingapp.domain;

import jakarta.persistence.*;

/**
 * 바늘 정보 Entity
 */
@Entity
public class Needle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String type; // 대바늘/코바늘
    private Double size; // mm 단위

    public Long getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public Double getSize() {
        return size;
    }

    public void setId(Long id) { this.id = id; }
    public void setType(String type) { this.type = type; }
    public void setSize(Double size) { this.size = size; }
}
