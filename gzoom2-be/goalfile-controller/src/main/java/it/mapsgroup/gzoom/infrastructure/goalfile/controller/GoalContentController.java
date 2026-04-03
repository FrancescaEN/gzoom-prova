package it.mapsgroup.gzoom.infrastructure.goalfile.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import it.mapsgroup.gzoom.entity.goalfile.model.GoalContent;
import it.mapsgroup.gzoom.exception.ResourceNotFoundException;
import it.mapsgroup.gzoom.goalfile.GoalContentUseCase;
import it.mapsgroup.gzoom.goalfile.exception.GoalContentCreationException;
import it.mapsgroup.gzoom.goalfile.exception.GoalContentNotFoundException;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.List;


@RestController
@RequestMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
@Validated
public class GoalContentController {

    private final GoalContentUseCase goalContentUseCase;

    public GoalContentController(GoalContentUseCase goalContentUseCase) {
        this.goalContentUseCase = goalContentUseCase;
    }

    @GetMapping("/v1/goal/{goalId}/contents")
    @Operation(summary = "Restituisce tutti gli allegati")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successo"),
    })
    public List<GoalContent> getGoalContents(@PathVariable String goalId,
                                             @RequestParam(required = false) Instant fromDate,
                                             @RequestParam(required = false) Instant thruDate) {
        return this.goalContentUseCase.getGoalContentListByWorkEffortId(goalId, fromDate, thruDate);
    }

    @PostMapping(value = "/v1/goal/content", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Crea allegato")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successo"),
            @ApiResponse(responseCode = "400", description = "Errore creazione allegato")
    })
    public void createGoalContent(@RequestPart MultipartFile file,
                                  @RequestParam String goalId,
                                  @RequestParam String description,
                                  @RequestParam String contentType,
                                  @RequestParam OffsetDateTime fromDate,
                                  @RequestParam(required = false) OffsetDateTime thruDate
                                  ) throws GoalContentCreationException {
        String userLoginId = SecurityContextHolder.getContext().getAuthentication().getName();
        this.goalContentUseCase.createGoalContent(file, goalId, description, contentType, fromDate, thruDate, userLoginId);
    }

    @GetMapping("/v1/goal/content/{id}/download")
    public ResponseEntity<Resource> downloadContentAttachment(@PathVariable String id) throws IOException {
        try {
            return this.goalContentUseCase.downloadContent(id);
        } catch (GoalContentNotFoundException e) {
            // todo malformed uri exception handler?
            throw new ResourceNotFoundException(e.getMessage());
        }
    }

    @PutMapping("/v1/goal/content/{id}")
    @Operation(summary = "Modifica un allegato")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successo"),
            @ApiResponse(responseCode = "404", description = "Nessun allegato")
    })
    public void updateGoalContent(@PathVariable String id, @RequestBody GoalContent goalContent) {
        String userLoginId = SecurityContextHolder.getContext().getAuthentication().getName();
        goalContent.setContentId(id);
        goalContent.setDataResourceId(id);
        try {
            this.goalContentUseCase.updateGoalContent(goalContent, userLoginId);
        } catch (GoalContentNotFoundException e) {
            throw new ResourceNotFoundException(e.getMessage());
        }
    }

    @DeleteMapping("/v1/goal/content/{id}")
    @Operation(summary = "Elimina un allegato")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successo"),
            @ApiResponse(responseCode = "404", description = "Allegato non trovato")
    })
    public void deleteGoalContent(@PathVariable String id) {
        try {
            this.goalContentUseCase.deleteGoalContent(id);
        } catch (GoalContentNotFoundException e) {
            throw new ResourceNotFoundException(e.getMessage());
        }
    }
}
