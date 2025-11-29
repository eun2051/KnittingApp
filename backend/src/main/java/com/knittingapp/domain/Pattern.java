package com.knittingapp.domain;

import jakarta.persistence.*;

/**
 * 도안 정보 Entity
 */
@Entity
public class Pattern {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pattern_seq")
    @SequenceGenerator(name = "pattern_seq", sequenceName = "pattern_id_seq", allocationSize = 1)
    private Long id;
    private String name; // 도안 이름
    private String imageUrl; // 도안 이미지 URL
    private String description; // 설명
    
    @Column(name = "pattern_pdf_url")
    private String pdfUrl; // 도안 PDF URL
    
    @Column(name = "pattern_link_url")
    private String linkUrl; // 도안 링크 URL (외부 웹사이트 등)
    
    // Getter/Setter

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getImageUrl() { return imageUrl; }
    public String getDescription() { return description; }
    public String getPdfUrl() { return pdfUrl; }
    public String getLinkUrl() { return linkUrl; }
    
    public void setId(Long id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public void setDescription(String description) { this.description = description; }
    public void setPdfUrl(String pdfUrl) { this.pdfUrl = pdfUrl; }
    public void setLinkUrl(String linkUrl) { this.linkUrl = linkUrl; }
}
