package colfume.domain.perfume.service;

import colfume.domain.perfume.model.entity.Hashtag;
import colfume.domain.perfume.model.entity.Perfume;
import colfume.domain.perfume.model.repository.PerfumeRepository;
import colfume.enums.ErrorCode;
import colfume.exception.PerfumeNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static colfume.dto.PerfumeDto.*;

@Service
@Transactional
@RequiredArgsConstructor
public class PerfumeService {

    private final PerfumeRepository perfumeRepository;

    public Long createPerfume(PerfumeRequestDto perfumeDto, List<String> tags) {
        List<Hashtag> hashtags = new ArrayList<>();
        tags.forEach(tag -> hashtags.add(new Hashtag(tag)));

        Perfume perfume = Perfume.create(
                perfumeDto.getName(),
                perfumeDto.getVolume(),
                perfumeDto.getPrice(),
                perfumeDto.getColors(),
                perfumeDto.getMoods(),
                perfumeDto.getStyles(),
                perfumeDto.getNotes(),
                perfumeDto.getDescription(),
                perfumeDto.getImageUrl(),
                hashtags
        );

        return perfumeRepository.save(perfume).getId(); // auto persist : hashtag
    }

    public void updateInfo(Long perfumeId, UpdateInfoRequestDto updateInfoDto) {
        Perfume perfume = findPerfume(perfumeId);
        perfume.updateInfo(updateInfoDto.getName(), updateInfoDto.getPrice(), updateInfoDto.getDescription());
    }

    public void updateImage(Long perfumeId, String imageUrl) {
        Perfume perfume = findPerfume(perfumeId);
        perfume.updateImage(imageUrl);
    }

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

    @Transactional(readOnly = true)
    private Perfume findPerfume(Long perfumeId) {
        return perfumeRepository.findById(perfumeId)
                .orElseThrow(() -> new PerfumeNotFoundException(ErrorCode.PERFUME_NOT_FOUND));
    }
}
