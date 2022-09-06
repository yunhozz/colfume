package colfume.api;

import colfume.domain.perfume.model.repository.PerfumeRepository;
import colfume.domain.perfume.service.PerfumeService;
import colfume.enums.Color;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static colfume.dto.PerfumeDto.*;
import static org.hamcrest.Matchers.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class PerfumeApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PerfumeService perfumeService;

    @Autowired
    private PerfumeRepository perfumeRepository;

    @Test
    @WithMockUser
    @DisplayName("GET /api/perfumes/{perfumeId}")
    void getPerfume() throws Exception {
        //given
        PerfumeRequestDto perfumeRequestDto1 = createPerfumeDto("test1", 30, 10000, List.of(Color.RED, Color.ORANGE), List.of("m1", "m2"), List.of("s1", "s2"), List.of("n1", "n2"), "test perfume1");
        PerfumeRequestDto perfumeRequestDto2 = createPerfumeDto("test2", 50, 20000, List.of(Color.YELLOW, Color.GREEN), List.of("m3", "m4"), List.of("s3", "s4"), List.of("n3", "n4"), "test perfume2");
        PerfumeRequestDto perfumeRequestDto3 = createPerfumeDto("test3", 80, 30000, List.of(Color.RED, Color.YELLOW), List.of("m5", "m6"), List.of("s5", "s6"), List.of("n5", "n6"), "test perfume3");
        Long perfumeId1 = perfumeService.createPerfume(perfumeRequestDto1, List.of("#hashtag1", "#hashtag2", "#hashtag3"));
        Long perfumeId2 = perfumeService.createPerfume(perfumeRequestDto2, List.of("#hashtag1", "#hashtag2", "#hashtag3"));
        Long perfumeId3 = perfumeService.createPerfume(perfumeRequestDto3, List.of("#hashtag1", "#hashtag2", "#hashtag3"));

        //when
        ResultActions result = mockMvc.perform(RestDocumentationRequestBuilders.get("/api/perfumes/{perfumeId}", perfumeId1)
                .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("id").value(perfumeId1))
                .andExpect(jsonPath("name").value("test1"))
                .andExpect(jsonPath("$.colors").isArray())
                .andExpect(jsonPath("$.colors", hasSize(2)))
                .andExpect(jsonPath("$.colors", hasItems("RED", "ORANGE")))
                .andExpect(jsonPath("description").value("test perfume1"));
    }

    @Test
    @WithMockUser
    @DisplayName("GET /api/perfumes")
    void getPerfumes() throws Exception {
        //given
        PerfumeRequestDto perfumeRequestDto1 = createPerfumeDto("test1", 30, 10000, List.of(Color.RED, Color.ORANGE), List.of("m1", "m2"), List.of("s1", "s2"), List.of("n1", "n2"), "test perfume1");
        PerfumeRequestDto perfumeRequestDto2 = createPerfumeDto("test2", 50, 20000, List.of(Color.YELLOW, Color.GREEN), List.of("m3", "m4"), List.of("s3", "s4"), List.of("n3", "n4"), "test perfume2");
        PerfumeRequestDto perfumeRequestDto3 = createPerfumeDto("test3", 80, 30000, List.of(Color.RED, Color.YELLOW), List.of("m5", "m6"), List.of("s5", "s6"), List.of("n5", "n6"), "test perfume3");
        Long perfumeId1 = perfumeService.createPerfume(perfumeRequestDto1, List.of("#hashtag1", "#hashtag2", "#hashtag3"));
        Long perfumeId2 = perfumeService.createPerfume(perfumeRequestDto2, List.of("#hashtag1", "#hashtag2", "#hashtag3"));
        Long perfumeId3 = perfumeService.createPerfume(perfumeRequestDto3, List.of("#hashtag1", "#hashtag2", "#hashtag3"));

        //when
        ResultActions result = mockMvc.perform(get("/api/perfumes")
                .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(perfumeId1))
                .andExpect(jsonPath("$[0].name").value("test1"))
                .andExpect(jsonPath("$[0].colors", hasItems("RED", "ORANGE")))
                .andExpect(jsonPath("$[0].description").value("test perfume1"))
                .andExpect(jsonPath("$[1].id").value(perfumeId2))
                .andExpect(jsonPath("$[1].name").value("test2"))
                .andExpect(jsonPath("$[1].colors", hasItems("YELLOW", "GREEN")))
                .andExpect(jsonPath("$[1].description").value("test perfume2"))
                .andExpect(jsonPath("$[2].id").value(perfumeId3))
                .andExpect(jsonPath("$[2].name").value("test3"))
                .andExpect(jsonPath("$[2].colors", hasItems("RED", "YELLOW")))
                .andExpect(jsonPath("$[2].description").value("test perfume3"));
    }

    @Test
    @WithMockUser
    @DisplayName("POST /api/perfumes")
    void createPerfume() throws Exception {
        //given
        PerfumeRequestDto perfumeRequestDto = new PerfumeRequestDto("test", 50, 70000, List.of(Color.RED, Color.ORANGE), List.of("m1", "m2"), List.of("s1", "s2"), List.of("n1", "n2"), "test", null);

        //when
        ResultActions result = mockMvc.perform(post("/api/perfumes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(perfumeRequestDto))
                .param("tags", "#hashtag1", "#hashtag2", "#hashtag3")
        );

        //then
        result.andExpect(status().isCreated())
                .andExpect(content().string("1"));
    }

    @Test
    @WithMockUser
    @DisplayName("PATCH /api/perfumes/info")
    void updatePerfumeInfo() throws Exception {
        //given
        PerfumeRequestDto perfumeRequestDto = new PerfumeRequestDto("test", 50, 70000, List.of(Color.RED, Color.ORANGE), List.of("m1", "m2"), List.of("s1", "s2"), List.of("n1", "n2"), "test", null);
        Long perfumeId = perfumeService.createPerfume(perfumeRequestDto, List.of("#hashtag1", "#hashtag2", "#hashtag3"));
        UpdateInfoRequestDto updateInfoRequestDto = new UpdateInfoRequestDto("update", 50000, "update");

        //when
        ResultActions result = mockMvc.perform(patch("/api/perfumes/info")
                .contentType(MediaType.APPLICATION_JSON)
                .param("perfumeId", String.valueOf(perfumeId))
                .content(objectMapper.writeValueAsString(updateInfoRequestDto))
        );

        //then
        result.andExpect(status().isCreated());
    }

    @Test
    @WithMockUser
    @DisplayName("PATCH /api/perfumes/image")
    void updatePerfumeImage() throws Exception {
        //given
        PerfumeRequestDto perfumeRequestDto = new PerfumeRequestDto("test", 50, 70000, List.of(Color.RED, Color.ORANGE), List.of("m1", "m2"), List.of("s1", "s2"), List.of("n1", "n2"), "test", null);
        Long perfumeId = perfumeService.createPerfume(perfumeRequestDto, List.of("#hashtag1", "#hashtag2", "#hashtag3"));

        //when
        ResultActions result = mockMvc.perform(patch("/api/perfumes/image")
                .contentType(MediaType.APPLICATION_JSON)
                .param("perfumeId", String.valueOf(perfumeId))
                .param("imageUrl", "update-image-url")
        );

        //then
        result.andExpect(status().isCreated());
    }

    @Test
    @WithMockUser
    @DisplayName("DELETE /api/perfumes")
    void deletePerfume() throws Exception {
        //given
        PerfumeRequestDto perfumeRequestDto = new PerfumeRequestDto("test", 50, 70000, List.of(Color.RED, Color.ORANGE), List.of("m1", "m2"), List.of("s1", "s2"), List.of("n1", "n2"), "test", null);
        Long perfumeId = perfumeService.createPerfume(perfumeRequestDto, List.of("#hashtag1", "#hashtag2", "#hashtag3"));

        //when
        ResultActions result = mockMvc.perform(delete("/api/perfumes")
                .contentType(MediaType.APPLICATION_JSON)
                .param("perfumeId", String.valueOf(perfumeId))
        );

        //then
        result.andExpect(status().isNoContent());
    }

    private PerfumeRequestDto createPerfumeDto(String name, int volume, int price, List<Color> colors, List<String> moods, List<String> styles, List<String> notes, String description) {
        return PerfumeRequestDto.builder()
                .name(name)
                .volume(volume)
                .price(price)
                .colors(colors)
                .moods(moods)
                .styles(styles)
                .notes(notes)
                .description(description)
                .build();
    }
}