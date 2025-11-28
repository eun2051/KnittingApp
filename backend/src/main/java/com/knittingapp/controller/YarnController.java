package com.knittingapp.controller;

import com.knittingapp.dto.YarnRequestDTO;
import com.knittingapp.dto.YarnResponseDTO;
import com.knittingapp.service.YarnService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * 실 API 컨트롤러
 */
@RestController
@RequestMapping("/api/yarns")
public class YarnController {
    private final YarnService yarnService;
    public YarnController(YarnService yarnService) {
        this.yarnService = yarnService;
    }
    @PostMapping
    public YarnResponseDTO createYarn(@RequestBody YarnRequestDTO dto) {
        return yarnService.createYarn(dto);
    }
    @GetMapping
    public List<YarnResponseDTO> getAllYarns() {
        return yarnService.getAllYarns();
    }
    @GetMapping("/{id}")
    public YarnResponseDTO getYarn(@PathVariable Long id) {
        return yarnService.getYarn(id);
    }
    @PutMapping("/{id}")
    public YarnResponseDTO updateYarn(@PathVariable Long id, @RequestBody YarnRequestDTO dto) {
        return yarnService.updateYarn(id, dto);
    }
    @DeleteMapping("/{id}")
    public void deleteYarn(@PathVariable Long id) {
        yarnService.deleteYarn(id);
    }
}
