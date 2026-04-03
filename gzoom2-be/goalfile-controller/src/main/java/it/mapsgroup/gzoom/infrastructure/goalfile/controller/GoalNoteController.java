package it.mapsgroup.gzoom.infrastructure.goalfile.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import it.mapsgroup.gzoom.entity.goalfile.model.GoalNote;
import it.mapsgroup.gzoom.exception.ResourceNotFoundException;
import it.mapsgroup.gzoom.goalfile.GoalNoteUseCase;
import it.mapsgroup.gzoom.goalfile.exception.*;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
@Validated
public class GoalNoteController {

    private final GoalNoteUseCase goalNoteUseCase;

    public GoalNoteController(GoalNoteUseCase goalNoteUseCase) {
        this.goalNoteUseCase = goalNoteUseCase;
    }


    @GetMapping("/v1/goal/{goalId}/notes")
    @Operation(summary = "Restituisce tutte le note")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successo"),
    })
    public List<GoalNote> getGoalNotes(@PathVariable String goalId) {
        return this.goalNoteUseCase.getGoalNoteListByWorkEffortId(goalId);
    }


    @PostMapping("/v1/goal/note")
    @Operation(summary = "Crea nota")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successo"),
            @ApiResponse(responseCode = "400", description = "Errore creazione nota")
    })
    public void createGoalNote(@RequestBody @Valid GoalNote goalNote) throws GoalNoteCreationException {
        String userLoginId = SecurityContextHolder.getContext().getAuthentication().getName();
        this.goalNoteUseCase.createGoalNote(goalNote, userLoginId);
    }

    @PutMapping("/v1/goal/note/{id}")
    @Operation(summary = "Modifica una nota")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successo"),
            @ApiResponse(responseCode = "404", description = "Nessuna nota")
    })
    public void updateGoalNote(@PathVariable String id, @RequestBody GoalNote goalNote) {
        String userLoginId = SecurityContextHolder.getContext().getAuthentication().getName();
        goalNote.setId(id);
        try {
            this.goalNoteUseCase.updateGoalNote(goalNote, userLoginId);
        } catch (GoalNoteNotFoundException e) {
            throw new ResourceNotFoundException(e.getMessage());
        }
    }


    @DeleteMapping("/v1/goal/note/{id}")
    @Operation(summary = "Elimina una nota")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successo"),
            @ApiResponse(responseCode = "404", description = "Nota non trovata")
    })
    public void deleteGoalNote(@PathVariable String id) {
        try {
            this.goalNoteUseCase.deleteGoalNote(id);
        } catch (GoalNoteNotFoundException e) {
            throw new ResourceNotFoundException(e.getMessage());
        }
    }
}
