package colfume.domain.perfume.model.repository;

import colfume.domain.perfume.model.entity.Color;
import colfume.domain.perfume.model.entity.Hashtag;
import colfume.domain.perfume.model.entity.Perfume;
import colfume.dto.SearchDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static colfume.dto.PerfumeDto.*;
import static colfume.enums.ColorType.*;
import static colfume.enums.SearchCondition.*;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class PerfumeRepositoryTest {

    @Autowired
    PerfumeRepository perfumeRepository;

    @Test
    @Commit
    void findSimplePerfumeList() throws Exception {
        //given

        // volume : 10 ~ 300
        // price : 1000 ~ 30000
        for (int i = 1; i <= 10; i++) {
            Hashtag tag = new Hashtag("#tag1");
            Color color = new Color(RED);
            Perfume perfume = Perfume.create("perfume" + i, i * 10, i * 1000, null, null, null, null, "image", i, List.of(tag), List.of(color));
            perfumeRepository.save(perfume);
            Thread.sleep(10);
        }
        for (int i = 11; i <= 20; i++) {
            Hashtag tag = new Hashtag("#tag2");
            Color color = new Color(YELLOW);
            Perfume perfume = Perfume.create("perfume" + i, i * 10, i * 1000, null, null, null, null, "image", i, List.of(tag), List.of(color));
            perfumeRepository.save(perfume);
            Thread.sleep(10);
        }
        for (int i = 21; i <= 30; i++) {
            Hashtag tag = new Hashtag("#tag3");
            Color color = new Color(GREEN);
            Perfume perfume = Perfume.create("perfume" + i, i * 10, i * 1000, null, null, null, null, "image", i, List.of(tag), List.of(color));
            perfumeRepository.save(perfume);
            Thread.sleep(10);
        }

        SearchDto searchDto = new SearchDto(50, 250, null, null, List.of(GREEN), POPULARITY);
        PageRequest pageable = PageRequest.ofSize(30);

        //when
        Page<PerfumeSimpleResponseDto> result = perfumeRepository.findSimplePerfumeList(searchDto, pageable);

        //then
        assertThat(result.getNumberOfElements()).isEqualTo(5);
    }
}