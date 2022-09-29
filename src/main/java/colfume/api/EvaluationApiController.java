package colfume.api;

import colfume.api.dto.Response;
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

    @GetMapping("/evaluations")
    public Response getEvaluationList(@RequestParam String perfumeId) {
        return Response.success(evaluationService.findEvaluationDtoListByPerfumeId(Long.valueOf(perfumeId)), HttpStatus.OK);
    }

    @PostMapping("/evaluations")
    public Response createEvaluation(@AuthenticationPrincipal UserPrincipal user, @Valid @RequestBody EvaluationRequestDto evaluationRequestDto, @RequestParam String perfumeId) {
        return Response.success(evaluationService.createEvaluation(evaluationRequestDto, user.getId(), Long.valueOf(perfumeId)), HttpStatus.OK);
    }

    @PatchMapping("/evaluations")
    public Response updateContent(@AuthenticationPrincipal UserPrincipal user, @Valid @RequestBody EvaluationRequestDto evaluationRequestDto,
                                              @RequestParam String evaluationId) {
        evaluationService.updateContent(Long.valueOf(evaluationId), user.getId(), evaluationRequestDto.getContent());
        return Response.success(HttpStatus.CREATED);
    }

    @DeleteMapping("/evaluations")
    public Response deleteEvaluation(@AuthenticationPrincipal UserPrincipal user, @RequestParam String evaluationId) {
        evaluationService.deleteEvaluation(Long.valueOf(evaluationId), user.getId());
        return Response.success(HttpStatus.NO_CONTENT);
    }
}