package colfume.domain.perfume.service;

import colfume.api.dto.perfume.PerfumeRequestDto;
import colfume.domain.perfume.model.entity.Hashtag;
import colfume.domain.perfume.model.entity.Perfume;
import colfume.domain.perfume.model.repository.HashtagRepository;
import colfume.domain.perfume.model.repository.PerfumeRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static colfume.common.enums.ColorType.GREEN;
import static colfume.common.enums.ColorType.RED;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class PerfumeServiceTest {

    @Autowired
    private PerfumeService perfumeService;

    @Autowired
    private PerfumeRepository perfumeRepository;

    @Autowired
    private HashtagRepository hashtagRepository;

    @Test
    void createPerfume() throws Exception {
        //given
        PerfumeRequestDto perfumeRequestDto = createPerfumeDto("test", 30, 50000, List.of("m1", "m2"), List.of("s1", "s2"), List.of("n1", "n2"), "test perfume");

        //when
        Long perfumeId = perfumeService.createPerfume(perfumeRequestDto, List.of("#hashtag1", "#hashtag2", "#hashtag3"), List.of(RED, GREEN));
        Perfume perfume = perfumeRepository.findById(perfumeId).get();
        List<Hashtag> tags = hashtagRepository.findAll();

        //then
        assertThat(perfume).isNotNull();
        assertThat(tags.size()).isEqualTo(3);
    }

    @Test
    void updateInfo() throws Exception {
        //given
        PerfumeRequestDto perfumeRequestDto = createPerfumeDto("test", 30, 50000, List.of("m1", "m2"), List.of("s1", "s2"), List.of("n1", "n2"), "test perfume");
        Long perfumeId = perfumeService.createPerfume(perfumeRequestDto, List.of("#hashtag1", "#hashtag2", "#hashtag3"), List.of(RED, GREEN));

        //when
        perfumeService.updateInfo(perfumeId, "update", 30000, "update");
        Perfume perfume = perfumeRepository.findById(perfumeId).get();

        //then
        assertThat(perfume).isNotNull();
        assertThat(perfume.getName()).isEqualTo("update");
        assertThat(perfume.getDescription()).isEqualTo("update");
    }

    @Test
    void updateImage() throws Exception {
        //given
        PerfumeRequestDto perfumeRequestDto = createPerfumeDto("test", 30, 50000, List.of("m1", "m2"), List.of("s1", "s2"), List.of("n1", "n2"), "test perfume");
        Long perfumeId = perfumeService.createPerfume(perfumeRequestDto, List.of("#hashtag1", "#hashtag2", "#hashtag3"), List.of(RED, GREEN));

        //when
        perfumeService.updateImage(perfumeId, "update-url");
        Perfume perfume = perfumeRepository.findById(perfumeId).get();

        //then
        assertThat(perfume).isNotNull();
        assertThat(perfume.getImageUrl()).isEqualTo("update-url");
    }

    @Test
    void deletePerfume() throws Exception {
        //given
        PerfumeRequestDto perfumeRequestDto = createPerfumeDto("test", 30, 50000, List.of("m1", "m2"), List.of("s1", "s2"), List.of("n1", "n2"), "test perfume");
        Long perfumeId = perfumeService.createPerfume(perfumeRequestDto, List.of("#hashtag1", "#hashtag2", "#hashtag3"), List.of(RED, GREEN));

        //when
        perfumeService.deletePerfume(perfumeId);
        List<Perfume> perfumes = perfumeRepository.findAll();

        //then
        assertThat(perfumes.size()).isEqualTo(0);
    }

    private PerfumeRequestDto createPerfumeDto(String name, int volume, int price, List<String> moods, List<String> styles, List<String> notes, String description) {
        return PerfumeRequestDto.builder()
                .name(name)
                .volume(volume)
                .price(price)
                .moods(moods)
                .styles(styles)
                .notes(notes)
                .description(description)
                .build();
    }
}