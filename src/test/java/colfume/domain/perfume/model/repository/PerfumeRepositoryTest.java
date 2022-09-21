package colfume.domain.perfume.model.repository;

import colfume.domain.perfume.model.entity.Color;
import colfume.domain.perfume.model.entity.Hashtag;
import colfume.domain.perfume.model.entity.Perfume;
import colfume.dto.SortDto;
import colfume.enums.SortCondition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static colfume.dto.PerfumeDto.*;
import static colfume.enums.ColorType.*;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class PerfumeRepositoryTest {

    @Autowired
    PerfumeRepository perfumeRepository;

    @BeforeEach
    void beforeEach() throws Exception {
        // volume : 10 ~ 300
        // price : 1000 ~ 30000
        for (int i = 1; i <= 10; i++) {
            Hashtag tag = new Hashtag("#tag1");
            Color color = new Color(RED);
            Perfume perfume = Perfume.create("perfume" + i, i * 10, i * 1000, null, null, null, "test perfume1", "image", i, List.of(tag), List.of(color));
            perfumeRepository.save(perfume);
            Thread.sleep(10);
        }
        for (int i = 11; i <= 20; i++) {
            Hashtag tag = new Hashtag("#tag2");
            Color color = new Color(YELLOW);
            Perfume perfume = Perfume.create("perfume" + i, i * 10, i * 1000, null, null, null, "test perfume2", "image", i, List.of(tag), List.of(color));
            perfumeRepository.save(perfume);
            Thread.sleep(10);
        }
        for (int i = 21; i <= 30; i++) {
            Hashtag tag = new Hashtag("#tag3");
            Color color = new Color(GREEN);
            Perfume perfume = Perfume.create("perfume" + i, i * 10, i * 1000, null, null, null, "test perfume3", "image", i, List.of(tag), List.of(color));
            perfumeRepository.save(perfume);
            Thread.sleep(10);
        }
    }

    @Test
    void searchByKeywordOrderByCreated() throws Exception {
        //given
        PageRequest pageable = PageRequest.ofSize(30);

        //when
        Page<PerfumeSimpleResponseDto> result = perfumeRepository.searchByKeywordOrderByCreated("tag1", pageable);

        //then
        assertThat(result.getNumberOfElements()).isEqualTo(10);
    }

    @Test
    void searchByKeywordOrderByAccuracy() throws Exception {
        //given
        PageRequest pageable = PageRequest.ofSize(30);

        //when
        Page<PerfumeSimpleResponseDto> result = perfumeRepository.searchByKeywordOrderByAccuracy("tag1", pageable);

        //then
        assertThat(result.getNumberOfElements()).isEqualTo(10);
    }

    @Test
    void findSimplePerfumeList() throws Exception {
        //given
        SortDto sortDto = new SortDto(50, 250, null, null, List.of(GREEN), SortCondition.POPULARITY);
        PageRequest pageable = PageRequest.ofSize(30);

        //when
        Page<PerfumeSimpleResponseDto> result = perfumeRepository.sortSimplePerfumeList(sortDto, pageable);

        //then
        assertThat(result.getNumberOfElements()).isEqualTo(5);
    }
}