package colfume.api;

import colfume.api.dto.Response;
import colfume.domain.evaluation.model.repository.EvaluationRepository;
import colfume.domain.evaluation.service.EvaluationService;
import colfume.oauth.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static colfume.dto.EvaluationDto.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class EvaluationApiController {

    private final EvaluationService evaluationService;
    private final EvaluationRepository evaluationRepository;

    @GetMapping("/evaluations")
    public Response getEvaluationListByPerfume(@RequestParam String perfumeId) {
        return Response.success(evaluationRepository.findEvaluationListByPerfumeId(Long.valueOf(perfumeId)), HttpStatus.OK);
    }

    @PostMapping("/evaluation")
    public Response createEvaluation(@AuthenticationPrincipal UserPrincipal user, @Valid @RequestBody EvaluationRequestDto evaluationRequestDto, @RequestParam String perfumeId) {
        return Response.success(evaluationService.createEvaluation(evaluationRequestDto, user.getId(), Long.valueOf(perfumeId)), HttpStatus.OK);
    }

    @PatchMapping("/evaluation")
    public Response updateEvaluation(@AuthenticationPrincipal UserPrincipal user, @Valid @RequestBody EvaluationRequestDto evaluationRequestDto, @RequestParam String evaluationId) {
        evaluationService.update(Long.valueOf(evaluationId), user.getId(), evaluationRequestDto.getContent(), evaluationRequestDto.getScore());
        return Response.success(HttpStatus.CREATED);
    }

    @DeleteMapping("/evaluation")
    public Response deleteEvaluation(@AuthenticationPrincipal UserPrincipal user, @RequestParam String evaluationId) {
        evaluationService.deleteEvaluation(Long.valueOf(evaluationId), user.getId());
        return Response.success(HttpStatus.NO_CONTENT);
    }
}