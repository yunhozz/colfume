package colfume.api;

import colfume.domain.evaluation.service.EvaluationService;
import colfume.domain.member.service.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.util.List;

import static colfume.dto.EvaluationDto.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class EvaluationApiController {

    private final EvaluationService evaluationService;

    @GetMapping("/evaluations")
    public ResponseEntity<List<EvaluationResponseDto>> getEvaluationList(@RequestParam String perfumeId) {
        return ResponseEntity.ok(evaluationService.findEvaluationDtoListByPerfumeId(Long.valueOf(perfumeId)));
    }

    @PostMapping("/evaluations")
    public ResponseEntity<Long> createEvaluation(@AuthenticationPrincipal UserDetailsImpl userDetails, @Valid @RequestBody EvaluationRequestDto evaluationRequestDto,
                                                 @RequestParam String perfumeId) {
        return new ResponseEntity<>(evaluationService.createEvaluation(evaluationRequestDto, userDetails.getId(), Long.valueOf(perfumeId)), HttpStatus.CREATED);
    }

    @PatchMapping("/evaluations")
    public ResponseEntity<Void> updateContent(@AuthenticationPrincipal UserDetailsImpl userDetails, @Valid @RequestBody EvaluationRequestDto evaluationRequestDto,
                                              @RequestParam String evaluationId) {
        evaluationService.updateContent(Long.valueOf(evaluationId), userDetails.getId(), evaluationRequestDto.getContent());
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/evaluations")
    public ResponseEntity<Void> deleteEvaluation(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestParam String evaluationId) {
        evaluationService.deleteEvaluation(Long.valueOf(evaluationId), userDetails.getId());
        return ResponseEntity.noContent().build();
    }
}