package it.mapsgroup.gzoom.infrastructure.goalfile.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import it.mapsgroup.gzoom.entity.goalfile.model.GoalStatus;
import it.mapsgroup.gzoom.entity.goalfile.model.GoalStatusAvailable;
import it.mapsgroup.gzoom.entity.goalfile.model.Results;
import it.mapsgroup.gzoom.exception.ResourceNotFoundException;
import it.mapsgroup.gzoom.goalfile.GoalStatusUseCase;
import it.mapsgroup.gzoom.goalfile.exception.GoalFileNotFoundException;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
@Validated
public class GoalFileStatusController {
    private final GoalStatusUseCase goalFileGoalStatusUseCase;

    public GoalFileStatusController(GoalStatusUseCase goalFileGoalStatusUseCase) {
        this.goalFileGoalStatusUseCase = goalFileGoalStatusUseCase;
    }

    @GetMapping("/v1/goalfile/status/available")
    @Operation(summary = "Restituisce la lista degli stati assegnabili alla scheda")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successo"),
            @ApiResponse(responseCode = "404", description = "Nessuno stato trovato")
    })
    public List<GoalStatusAvailable> getGoalFileAvailableStatus(@RequestParam String goalFileId,
                                                                @RequestParam String goalFileStatusId) {
        return this.goalFileGoalStatusUseCase.getGoalFileAvailableStatus(goalFileId, goalFileStatusId);
    }

    @PutMapping("/v1/goalfile/{goalFileId}/status")
    @Operation(summary = "Aggiorna lo stato alla scheda e agli obiettivi figli")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successo"),
            @ApiResponse(responseCode = "404", description = "Scheda non trovata")
    })
    public Results<Boolean> updateGoalFileStatus(@PathVariable String goalFileId,
                                                 @RequestBody @Valid GoalStatus goalStatus,
                                                 @RequestParam(required = false) String noteId) {
        try {
            return this.goalFileGoalStatusUseCase.updateGoalFileStatus(goalFileId, goalStatus, noteId);
        } catch (GoalFileNotFoundException e) {
            throw new ResourceNotFoundException(e.getMessage());
        }
    }
}
