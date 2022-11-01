package colfume.api;

import colfume.api.dto.Response;
import colfume.api.dto.perfume.PerfumeRequestDto;
import colfume.api.dto.perfume.UpdateInfoRequestDto;
import colfume.domain.perfume.model.repository.PerfumeRepository;
import colfume.domain.perfume.service.PerfumeService;
import colfume.api.dto.perfume.SearchDto;
import colfume.api.dto.perfume.SortDto;
import colfume.common.enums.ColorType;
import colfume.common.enums.SearchCondition;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/perfumes")
@RequiredArgsConstructor
public class PerfumeApiController {

    private final PerfumeService perfumeService;
    private final PerfumeRepository perfumeRepository;

    @GetMapping("/{id}")
    public Response getPerfume(@PathVariable String id) {
        return Response.success(perfumeService.findPerfumeDto(Long.valueOf(id)), HttpStatus.OK);
    }

    @GetMapping
    public Response getPerfumes() {
        return Response.success(perfumeService.findPerfumeDtoList(), HttpStatus.OK);
    }

    @PostMapping("/search")
    public Response searchPerfume(@Valid @RequestBody SearchDto searchDto, Pageable pageable) {
        if (searchDto.getCondition() == SearchCondition.LATEST) {
            return Response.success(perfumeRepository.searchByKeywordOrderByCreated(searchDto.getKeyword(), pageable), HttpStatus.OK);
        }
        if (searchDto.getCondition() == SearchCondition.ACCURACY) {
            return Response.success(perfumeRepository.searchByKeywordOrderByAccuracy(searchDto.getKeyword(), pageable), HttpStatus.OK);
        }
        return null;
    }

    @PostMapping("/sort")
    public Response sortPerfumes(@RequestBody SortDto sortDto, Pageable pageable) {
        return Response.success(perfumeRepository.sortSimplePerfumeList(sortDto, pageable), HttpStatus.OK);
    }

    @PostMapping
    public Response createPerfume(@Valid @RequestBody PerfumeRequestDto perfumeRequestDto, @RequestParam List<String> tags, @RequestParam List<ColorType> colorTypes) {
        return Response.success(perfumeService.createPerfume(perfumeRequestDto, tags, colorTypes), HttpStatus.OK);
    }

    @PatchMapping("/{id}/info")
    public Response updatePerfumeInfo(@PathVariable String id, @Valid @RequestBody UpdateInfoRequestDto updateInfoRequestDto) {
        perfumeService.updateInfo(Long.valueOf(id), updateInfoRequestDto.getName(), updateInfoRequestDto.getPrice(), updateInfoRequestDto.getDescription());
        return Response.success(HttpStatus.CREATED);
    }

    @PatchMapping("/{id}/image")
    public Response updatePerfumeImage(@PathVariable String id, @RequestParam(required = false) String imageUrl) {
        perfumeService.updateImage(Long.valueOf(id), imageUrl);
        return Response.success(HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public Response deletePerfume(@PathVariable String id) {
        perfumeService.deletePerfume(Long.valueOf(id));
        return Response.success(HttpStatus.NO_CONTENT);
    }
}