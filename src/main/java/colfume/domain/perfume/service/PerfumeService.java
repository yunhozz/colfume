package colfume.domain.perfume.service;

import colfume.common.enums.ErrorCode;
import colfume.domain.perfume.dto.request.PerfumeRequestDto;
import colfume.domain.perfume.model.entity.Perfume;
import colfume.domain.perfume.model.repository.PerfumeRepository;
import colfume.domain.perfume.service.converter.PerfumeConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PerfumeService {

    private final PerfumeRepository perfumeRepository;
    private final PerfumeConverter converter;

    @Transactional
    public Long createPerfume(PerfumeRequestDto perfumeRequestDto) {
        Perfume perfume = converter.convertToEntity(perfumeRequestDto);
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

    private Perfume findPerfume(Long perfumeId) {
        return perfumeRepository.findById(perfumeId)
                .orElseThrow(() -> new PerfumeNotFoundException(ErrorCode.PERFUME_NOT_FOUND));
    }
}