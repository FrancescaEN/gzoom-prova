package it.mapsgroup.gzoom.infrastructure.goalfile.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import it.mapsgroup.gzoom.entity.goalfile.model.*;
import it.mapsgroup.gzoom.exception.ResourceNotFoundException;
import it.mapsgroup.gzoom.goalfile.GoalFileUseCase;
import it.mapsgroup.gzoom.goalfile.exception.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
@Validated
@Slf4j
public class GoalFileController {
    private final GoalFileUseCase goalFileUseCase;

    public GoalFileController(GoalFileUseCase goalFileUseCase) {
        this.goalFileUseCase = goalFileUseCase;
    }

    @GetMapping("/v1/goalfile/roots")
    @Operation(summary = "Restituisce la lista delle schede")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successo")
    })
    public List<GoalFile> getGoalFileRoots() {
        String userLoginId = SecurityContextHolder.getContext().getAuthentication().getName();
        return this.goalFileUseCase.getGoalFileRoots(userLoginId);
    }

    @GetMapping("/v1/goalfile/{id}/tree/details")
    @Operation(summary = "Restituisce l'albero della scheda")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successo"),
            @ApiResponse(responseCode = "404", description = "Nessuno stato di scheda radice")
    })
    public List<Goal> getGoalFileTreeDetails(@PathVariable String id) {
        String userLoginId = SecurityContextHolder.getContext().getAuthentication().getName();
        try {
            return this.goalFileUseCase.getGoalFileTreeDetails(id, userLoginId);
        } catch (GoalFileNotFoundException e) {
            throw new ResourceNotFoundException(e.getMessage());
        }
    }
}
