package com.knittingapp.service;

import com.knittingapp.domain.Yarn;
import com.knittingapp.dto.YarnRequestDTO;
import com.knittingapp.dto.YarnResponseDTO;
import com.knittingapp.repository.YarnRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 실 서비스
 */
@Service
public class YarnService {
    private final YarnRepository yarnRepository;
    public YarnService(YarnRepository yarnRepository) {
        this.yarnRepository = yarnRepository;
    }
    public YarnResponseDTO createYarn(YarnRequestDTO dto) {
        Yarn yarn = new Yarn();
        yarn.setName(dto.name());
        yarn.setBrand(dto.brand());
        yarn.setColor(dto.color());
        yarn.setMaterial(dto.material());
        Yarn saved = yarnRepository.save(yarn);
        return toResponseDTO(saved);
    }
    public List<YarnResponseDTO> getAllYarns() {
        return yarnRepository.findAll().stream()
            .map(this::toResponseDTO)
            .collect(Collectors.toList());
    }
    public YarnResponseDTO getYarn(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("실 ID는 null일 수 없습니다.");
        }
        Yarn yarn = yarnRepository.findById(id).orElseThrow();
        return toResponseDTO(yarn);
    }
    public YarnResponseDTO updateYarn(Long id, YarnRequestDTO dto) {
        if (id == null) {
            throw new IllegalArgumentException("실 ID는 null일 수 없습니다.");
        }
        Yarn yarn = yarnRepository.findById(id).orElseThrow();
        yarn.setName(dto.name());
        yarn.setBrand(dto.brand());
        yarn.setColor(dto.color());
        yarn.setMaterial(dto.material());
        Yarn saved = yarnRepository.save(yarn);
        return toResponseDTO(saved);
    }
    public void deleteYarn(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("실 ID는 null일 수 없습니다.");
        }
        yarnRepository.deleteById(id);
    }
    private YarnResponseDTO toResponseDTO(Yarn yarn) {
        return new YarnResponseDTO(
            yarn.getId(),
            yarn.getName(),
            yarn.getBrand(),
            yarn.getColor(),
            yarn.getMaterial()
        );
    }
}
