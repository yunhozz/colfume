package colfume.api;

import colfume.api.dto.Response;
import colfume.api.dto.evaluation.EvaluationRequestDto;
import colfume.domain.evaluation.model.repository.EvaluationRepository;
import colfume.domain.evaluation.service.EvaluationService;
import colfume.oauth.model.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
@RequestMapping("/api/evaluations")
@RequiredArgsConstructor
public class EvaluationApiController {

    private final EvaluationService evaluationService;
    private final EvaluationRepository evaluationRepository;

    @GetMapping
    public Response getEvaluationListByPerfume(@RequestParam String perfumeId) {
        return Response.success(evaluationRepository.findListByPerfumeId(Long.valueOf(perfumeId)), HttpStatus.OK);
    }

    @PostMapping
    public Response createEvaluation(@AuthenticationPrincipal UserPrincipal userPrincipal, @Valid @RequestBody EvaluationRequestDto evaluationRequestDto, @RequestParam String perfumeId) {
        return Response.success(evaluationService.createEvaluation(evaluationRequestDto, userPrincipal.getId(), Long.valueOf(perfumeId)), HttpStatus.OK);
    }

    @PatchMapping("/{id}/update")
    public Response updateEvaluation(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable String id, @Valid @RequestBody EvaluationRequestDto evaluationRequestDto) {
        evaluationService.update(Long.valueOf(id), userPrincipal.getId(), evaluationRequestDto);
        return Response.success(HttpStatus.CREATED);
    }

    @PatchMapping("/{id}/delete")
    public Response deleteEvaluation(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable String id) {
        evaluationService.delete(Long.valueOf(id), userPrincipal.getId());
        return Response.success(HttpStatus.CREATED);
    }
}