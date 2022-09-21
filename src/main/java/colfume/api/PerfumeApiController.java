package colfume.api;

import colfume.domain.perfume.model.repository.PerfumeRepository;
import colfume.domain.perfume.service.PerfumeService;
import colfume.dto.SearchDto;
import colfume.dto.SortDto;
import colfume.enums.ColorType;
import colfume.enums.SearchCondition;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static colfume.dto.PerfumeDto.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PerfumeApiController {

    private final PerfumeService perfumeService;
    private final PerfumeRepository perfumeRepository;

    @GetMapping("/perfumes/{perfumeId}")
    public ResponseEntity<PerfumeResponseDto> getPerfume(@PathVariable String perfumeId) {
        return ResponseEntity.ok(perfumeService.findPerfumeDto(Long.valueOf(perfumeId)));
    }

    @GetMapping("/perfumes")
    public ResponseEntity<List<PerfumeResponseDto>> getPerfumes() {
        return ResponseEntity.ok(perfumeService.findPerfumeDtoList());
    }

    @GetMapping("/perfumes/search")
    public ResponseEntity<Page<PerfumeSimpleResponseDto>> searchPerfume(@Valid @RequestBody SearchDto searchDto, Pageable pageable) {
        if (searchDto.getCondition() == SearchCondition.LATEST) {
            return ResponseEntity.ok(perfumeRepository.searchByKeywordOrderByCreated(searchDto.getKeyword(), pageable));
        }
        if (searchDto.getCondition() == SearchCondition.ACCURACY) {
            return ResponseEntity.ok(perfumeRepository.searchByKeywordOrderByAccuracy(searchDto.getKeyword(), pageable));
        }
        return null;
    }

    @GetMapping("/perfumes/sort")
    public ResponseEntity<Page<PerfumeSimpleResponseDto>> sortPerfumes(@RequestBody SortDto sortDto, Pageable pageable) {
        return ResponseEntity.ok(perfumeRepository.sortSimplePerfumeList(sortDto, pageable));
    }

    @PostMapping("/perfumes")
    public ResponseEntity<Long> createPerfume(@Valid @RequestBody PerfumeRequestDto perfumeRequestDto, @RequestParam List<String> tags, @RequestParam List<ColorType> colorTypes) {
        return new ResponseEntity<>(perfumeService.createPerfume(perfumeRequestDto, tags, colorTypes), HttpStatus.CREATED);
    }

    @PatchMapping("/perfumes/info")
    public ResponseEntity<Void> updatePerfumeInfo(@RequestParam String perfumeId, @Valid @RequestBody UpdateInfoRequestDto updateInfoRequestDto) {
        perfumeService.updateInfo(Long.valueOf(perfumeId), updateInfoRequestDto.getName(), updateInfoRequestDto.getPrice(), updateInfoRequestDto.getDescription());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PatchMapping("/perfumes/image")
    public ResponseEntity<Void> updatePerfumeImage(@RequestParam String perfumeId, @RequestParam(required = false) String imageUrl) {
        perfumeService.updateImage(Long.valueOf(perfumeId), imageUrl);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/perfumes")
    public ResponseEntity<Void> deletePerfume(@RequestParam String perfumeId) {
        perfumeService.deletePerfume(Long.valueOf(perfumeId));
        return ResponseEntity.noContent().build();
    }
}