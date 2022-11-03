package colfume.api;

import colfume.api.dto.Response;
import colfume.api.dto.perfume.PerfumeRequestDto;
import colfume.api.dto.perfume.SearchDto;
import colfume.api.dto.perfume.SortDto;
import colfume.api.dto.perfume.UpdateInfoRequestDto;
import colfume.common.enums.SearchCondition;
import colfume.domain.perfume.model.repository.PerfumeRepository;
import colfume.domain.perfume.service.PerfumeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

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
    public Response getPerfumes(@RequestParam(required = false) Long perfumeId, @PageableDefault(size = 10) Pageable pageable) {
        return Response.success(perfumeRepository.findSimplePerfumePage(perfumeId, pageable), HttpStatus.OK);
    }

    @PostMapping("/sort")
    public Response getSortedPerfumes(@RequestBody SortDto sortDto, @RequestParam(required = false) Long perfumeId, Pageable pageable) {
        return Response.success(perfumeRepository.sortSimplePerfumePage(sortDto, perfumeId, pageable), HttpStatus.CREATED);
    }

    @PostMapping("/search")
    public Response searchPerfumes(@RequestBody SearchDto searchDto, @RequestParam(required = false) Long perfumeId, Pageable pageable) {
        if (searchDto.getCondition() == SearchCondition.LATEST) {
            return Response.success(perfumeRepository.searchByKeywordOrderByCreated(searchDto.getKeyword(), perfumeId, pageable), HttpStatus.CREATED);
        }
        if (searchDto.getCondition() == SearchCondition.ACCURACY) {
            return Response.success(perfumeRepository.searchByKeywordOrderByAccuracy(searchDto.getKeyword(), perfumeId, pageable), HttpStatus.CREATED);
        }
        return null;
    }

    @PostMapping
    public Response createPerfume(@Valid @RequestBody PerfumeRequestDto perfumeRequestDto) {
        return Response.success(perfumeService.createPerfume(perfumeRequestDto), HttpStatus.CREATED);
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