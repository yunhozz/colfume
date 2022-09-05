package colfume.api;

import colfume.domain.perfume.service.PerfumeService;
import lombok.RequiredArgsConstructor;
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

    @GetMapping("/perfumes/{perfumeId}")
    public ResponseEntity<PerfumeResponseDto> getPerfume(@PathVariable String perfumeId) {
        return ResponseEntity.ok(perfumeService.findPerfumeDto(Long.valueOf(perfumeId)));
    }

    @GetMapping("/perfumes")
    public ResponseEntity<List<PerfumeResponseDto>> getPerfumes() {
        return ResponseEntity.ok(perfumeService.findPerfumeDtoList());
    }

    @PostMapping("/perfumes")
    public ResponseEntity<Long> createPerfume(@Valid @RequestBody PerfumeRequestDto perfumeRequestDto, @RequestParam List<String> tags) {
        return ResponseEntity.ok(perfumeService.createPerfume(perfumeRequestDto, tags));
    }

    @PatchMapping("/perfumes/info")
    public ResponseEntity<Void> updatePerfumeInfo(@RequestParam String perfumeId, @Valid @RequestBody UpdateInfoRequestDto updateInfoRequestDto) {
        perfumeService.updateInfo(Long.valueOf(perfumeId), updateInfoRequestDto.getName(), updateInfoRequestDto.getPrice(), updateInfoRequestDto.getDescription());
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/perfumes/image")
    public ResponseEntity<Void> updatePerfumeImage(@RequestParam String perfumeId, @RequestParam(required = false) String imageUrl) {
        perfumeService.updateImage(Long.valueOf(perfumeId), imageUrl);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/perfumes")
    public ResponseEntity<Void> deletePerfume(@RequestParam String perfumeId) {
        perfumeService.deletePerfume(Long.valueOf(perfumeId));
        return ResponseEntity.noContent().build();
    }
}
