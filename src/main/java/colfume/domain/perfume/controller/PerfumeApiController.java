package colfume.domain.perfume.controller;

import colfume.common.dto.Response;
import colfume.common.enums.SearchCondition;
import colfume.domain.perfume.dto.request.PerfumeRequestDto;
import colfume.domain.perfume.dto.request.SearchRequestDto;
import colfume.domain.perfume.dto.request.SortRequestDto;
import colfume.domain.perfume.dto.request.UpdateInfoRequestDto;
import colfume.domain.perfume.model.repository.PerfumeRepository;
import colfume.domain.perfume.service.PerfumeService;
import colfume.oauth.model.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    public Response getPerfume(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable String id) {
        return Response.success(perfumeRepository.findPerfumeById(Long.valueOf(id), userPrincipal.getId()), HttpStatus.OK);
    }

    @GetMapping
    public Response getPerfumes(@AuthenticationPrincipal UserPrincipal userPrincipal, @RequestParam(required = false) Long perfumeId, @PageableDefault(size = 10) Pageable pageable) {
        return Response.success(perfumeRepository.findSimplePerfumePage(perfumeId, userPrincipal.getId(), pageable), HttpStatus.OK);
    }

    @PostMapping("/sort")
    public Response getSortedPerfumes(@AuthenticationPrincipal UserPrincipal userPrincipal, @RequestBody SortRequestDto sortRequestDto, @RequestParam(required = false) Long perfumeId, Pageable pageable) {
        return Response.success(perfumeRepository.sortSimplePerfumePage(sortRequestDto, perfumeId, userPrincipal.getId(), pageable), HttpStatus.CREATED);
    }

    @PostMapping("/search")
    public Response searchPerfumes(@AuthenticationPrincipal UserPrincipal userPrincipal, @RequestBody SearchRequestDto searchRequestDto, @RequestParam(required = false) Long perfumeId, Pageable pageable) {
        if (searchRequestDto.getCondition() == SearchCondition.LATEST) {
            return Response.success(perfumeRepository.searchByKeywordOrderByCreated(searchRequestDto.getKeyword(), perfumeId, userPrincipal.getId(), pageable), HttpStatus.CREATED);
        }
        if (searchRequestDto.getCondition() == SearchCondition.ACCURACY) {
            return Response.success(perfumeRepository.searchByKeywordOrderByAccuracy(searchRequestDto.getKeyword(), perfumeId, userPrincipal.getId(), pageable), HttpStatus.CREATED);
        }
        return null;
    }

    @Secured("ROLE_ADMIN")
    @PostMapping
    public Response createPerfume(@Valid @RequestBody PerfumeRequestDto perfumeRequestDto) {
        return Response.success(perfumeService.createPerfume(perfumeRequestDto), HttpStatus.CREATED);
    }

    @Secured("ROLE_ADMIN")
    @PatchMapping("/{id}/info")
    public Response updatePerfumeInfo(@PathVariable String id, @Valid @RequestBody UpdateInfoRequestDto updateInfoRequestDto) {
        perfumeService.updateInfo(Long.valueOf(id), updateInfoRequestDto.getName(), updateInfoRequestDto.getPrice(), updateInfoRequestDto.getDescription());
        return Response.success(HttpStatus.CREATED);
    }

    @Secured("ROLE_ADMIN")
    @PatchMapping("/{id}/image")
    public Response updatePerfumeImage(@PathVariable String id, @RequestParam(required = false) String imageUrl) {
        perfumeService.updateImage(Long.valueOf(id), imageUrl);
        return Response.success(HttpStatus.CREATED);
    }

    @Secured("ROLE_ADMIN")
    @DeleteMapping("/{id}")
    public Response deletePerfume(@PathVariable String id) {
        perfumeService.deletePerfume(Long.valueOf(id));
        return Response.success(HttpStatus.NO_CONTENT);
    }
}