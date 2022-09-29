package colfume.api;

import colfume.api.dto.Response;
import colfume.domain.perfume.model.repository.PerfumeRepository;
import colfume.domain.perfume.service.PerfumeService;
import colfume.dto.SearchDto;
import colfume.dto.SortDto;
import colfume.enums.ColorType;
import colfume.enums.SearchCondition;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
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
    public Response getPerfume(@PathVariable String perfumeId) {
        return Response.success(perfumeService.findPerfumeDto(Long.valueOf(perfumeId)), HttpStatus.OK);
    }

    @GetMapping("/perfumes")
    public Response getPerfumes() {
        return Response.success(perfumeService.findPerfumeDtoList(), HttpStatus.OK);
    }

    @PostMapping("/perfumes/search")
    public Response searchPerfume(@Valid @RequestBody SearchDto searchDto, Pageable pageable) {
        if (searchDto.getCondition() == SearchCondition.LATEST) {
            return Response.success(perfumeRepository.searchByKeywordOrderByCreated(searchDto.getKeyword(), pageable), HttpStatus.OK);
        }
        if (searchDto.getCondition() == SearchCondition.ACCURACY) {
            return Response.success(perfumeRepository.searchByKeywordOrderByAccuracy(searchDto.getKeyword(), pageable), HttpStatus.OK);
        }
        return null;
    }

    @PostMapping("/perfumes/sort")
    public Response sortPerfumes(@RequestBody SortDto sortDto, Pageable pageable) {
        return Response.success(perfumeRepository.sortSimplePerfumeList(sortDto, pageable), HttpStatus.OK);
    }

    @PostMapping("/perfumes")
    public Response createPerfume(@Valid @RequestBody PerfumeRequestDto perfumeRequestDto, @RequestParam List<String> tags, @RequestParam List<ColorType> colorTypes) {
        return Response.success(perfumeService.createPerfume(perfumeRequestDto, tags, colorTypes), HttpStatus.OK);
    }

    @PatchMapping("/perfumes/info")
    public Response updatePerfumeInfo(@RequestParam String perfumeId, @Valid @RequestBody UpdateInfoRequestDto updateInfoRequestDto) {
        perfumeService.updateInfo(Long.valueOf(perfumeId), updateInfoRequestDto.getName(), updateInfoRequestDto.getPrice(), updateInfoRequestDto.getDescription());
        return Response.success(HttpStatus.CREATED);
    }

    @PatchMapping("/perfumes/image")
    public Response updatePerfumeImage(@RequestParam String perfumeId, @RequestParam(required = false) String imageUrl) {
        perfumeService.updateImage(Long.valueOf(perfumeId), imageUrl);
        return Response.success(HttpStatus.CREATED);
    }

    @DeleteMapping("/perfumes")
    public Response deletePerfume(@RequestParam String perfumeId) {
        perfumeService.deletePerfume(Long.valueOf(perfumeId));
        return Response.success(HttpStatus.NO_CONTENT);
    }
}