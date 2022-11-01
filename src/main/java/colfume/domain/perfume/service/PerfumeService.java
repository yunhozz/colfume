package colfume.domain.perfume.service;

import colfume.api.dto.perfume.PerfumeRequestDto;
import colfume.domain.perfume.model.entity.Color;
import colfume.domain.perfume.model.entity.Hashtag;
import colfume.domain.perfume.model.entity.Perfume;
import colfume.domain.perfume.model.repository.PerfumeRepository;
import colfume.domain.perfume.service.dto.PerfumeResponseDto;
import colfume.common.enums.ColorType;
import colfume.common.enums.ErrorCode;
import colfume.domain.perfume.service.exception.PerfumeNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PerfumeService {

    private final PerfumeRepository perfumeRepository;

    @Transactional
    public Long createPerfume(PerfumeRequestDto perfumeRequestDto, List<String> tags, List<ColorType> colorTypes) {
        List<Hashtag> hashtags = new ArrayList<>();
        tags.forEach(tag -> hashtags.add(new Hashtag(tag)));

        List<Color> colors = new ArrayList<>();
        colorTypes.forEach(colorType -> colors.add(new Color(colorType)));

        Perfume perfume = Perfume.create(
                perfumeRequestDto.getName(),
                perfumeRequestDto.getVolume(),
                perfumeRequestDto.getPrice(),
                perfumeRequestDto.getMoods(),
                perfumeRequestDto.getStyles(),
                perfumeRequestDto.getNotes(),
                perfumeRequestDto.getDescription(),
                perfumeRequestDto.getImageUrl(),
                hashtags,
                colors
        );

        return perfumeRepository.save(perfume).getId(); // auto persist : hashtags, colors
    }

    @Transactional
    public void updateInfo(Long perfumeId, String name, int price, String description) {
        Perfume perfume = findPerfume(perfumeId);
        perfume.updateInfo(name, price, description);
    }

    @Transactional
    public void updateImage(Long perfumeId, String imageUrl) {
        Perfume perfume = findPerfume(perfumeId);
        perfume.updateImage(imageUrl);
    }

    @Transactional
    public void deletePerfume(Long perfumeId) {
        Perfume perfume = findPerfume(perfumeId);
        perfumeRepository.delete(perfume);
    }

    @Transactional(readOnly = true)
    public PerfumeResponseDto findPerfumeDto(Long perfumeId) {
        return new PerfumeResponseDto(findPerfume(perfumeId));
    }

    @Transactional(readOnly = true)
    public List<PerfumeResponseDto> findPerfumeDtoList() {
        return perfumeRepository.findAll().stream()
                .map(PerfumeResponseDto::new)
                .collect(Collectors.toList());
    }

    private Perfume findPerfume(Long perfumeId) {
        return perfumeRepository.findById(perfumeId)
                .orElseThrow(() -> new PerfumeNotFoundException(ErrorCode.PERFUME_NOT_FOUND));
    }
}