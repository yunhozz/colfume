package colfume.api;

import colfume.api.dto.Response;
import colfume.domain.evaluation.model.repository.EvaluationRepository;
import colfume.domain.evaluation.service.EvaluationService;
import colfume.common.oauth.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static colfume.common.dto.EvaluationDto.*;

@RestController
@RequestMapping("/api/evaluations")
@RequiredArgsConstructor
public class EvaluationApiController {

    private final EvaluationService evaluationService;
    private final EvaluationRepository evaluationRepository;

    @GetMapping
    public Response getEvaluationListByPerfume(@RequestParam String perfumeId) {
        return Response.success(evaluationRepository.findEvaluationListByPerfumeId(Long.valueOf(perfumeId)), HttpStatus.OK);
    }

    @PostMapping
    public Response createEvaluation(@AuthenticationPrincipal UserPrincipal user, @Valid @RequestBody EvaluationRequestDto evaluationRequestDto, @RequestParam String perfumeId) {
        return Response.success(evaluationService.createEvaluation(evaluationRequestDto, user.getId(), Long.valueOf(perfumeId)), HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public Response updateEvaluation(@AuthenticationPrincipal UserPrincipal user, @PathVariable String id, @Valid @RequestBody EvaluationRequestDto evaluationRequestDto) {
        evaluationService.update(Long.valueOf(id), user.getId(), evaluationRequestDto.getContent(), evaluationRequestDto.getScore());
        return Response.success(HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public Response deleteEvaluation(@AuthenticationPrincipal UserPrincipal user, @RequestParam String id) {
        evaluationService.deleteEvaluation(Long.valueOf(id), user.getId());
        return Response.success(HttpStatus.NO_CONTENT);
    }
}