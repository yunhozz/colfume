package colfume.domain.perfume.service;

import colfume.domain.perfume.dto.request.PerfumeRequestDto;
import colfume.domain.perfume.model.entity.Perfume;
import colfume.domain.perfume.model.repository.PerfumeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PerfumeService {

    private final PerfumeRepository perfumeRepository;

    @Transactional
    public Long createPerfume(PerfumeRequestDto perfumeRequestDto) {
        PerfumeConverter converter = new PerfumeConverter();
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
                .orElseThrow(PerfumeNotFoundException::new);
    }
}