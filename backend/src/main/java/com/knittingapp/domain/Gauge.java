package com.knittingapp.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

/**
 * 게이지 정보 (10cm x 10cm 기준)
 */
@Embeddable
public class Gauge {
    @Column(name = "gauge_stitches")
    private Integer stitches; // 코 수
    
    @Column(name = "gauge_rows")
    private Integer rows;     // 단 수
    
    // Getter/Setter
    public Integer getStitches() { return stitches; }
    public Integer getRows() { return rows; }
    public void setStitches(Integer stitches) { this.stitches = stitches; }
    public void setRows(Integer rows) { this.rows = rows; }
}
