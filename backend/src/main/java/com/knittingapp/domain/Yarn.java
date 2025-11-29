package com.knittingapp.domain;

import jakarta.persistence.*;

/**
 * 실 정보 Entity
 */
@Entity
public class Yarn {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "yarn_seq")
    @SequenceGenerator(name = "yarn_seq", sequenceName = "yarn_id_seq", allocationSize = 1)
    private Long id;
    private String name; // 실 이름
    private String brand; // 제조사
    private String color; // 색상
    private String material; // 소재
    // Getter/Setter 생략

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getBrand() { return brand; }
    public String getColor() { return color; }
    public String getMaterial() { return material; }
    public void setId(Long id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setBrand(String brand) { this.brand = brand; }
    public void setColor(String color) { this.color = color; }
    public void setMaterial(String material) { this.material = material; }
}
