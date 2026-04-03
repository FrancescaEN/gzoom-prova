package it.mapsgroup.gzoom.goalfile;

import it.mapsgroup.gzoom.entity.goalfile.gateway.GoalContentRepositoryGateway;

import it.mapsgroup.gzoom.entity.goalfile.model.GoalContent;
import it.mapsgroup.gzoom.goalfile.exception.FileDeletionException;
import it.mapsgroup.gzoom.goalfile.exception.GoalContentCreationException;
import it.mapsgroup.gzoom.goalfile.exception.GoalContentNotFoundException;
import it.mapsgroup.gzoom.sequencegenerator.usecase.SequenceGeneratorUseCase;
import it.mapsgroup.gzoom.service.ConfigurationImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.List;


@Slf4j
public class GoalContentUseCase {
    private final GoalContentRepositoryGateway goalContentRepositoryGateway;
    private final SequenceGeneratorUseCase sequenceGeneratorUseCase;
    private final ConfigurationImpl configuration;

    public GoalContentUseCase(GoalContentRepositoryGateway goalContentRepositoryGateway, SequenceGeneratorUseCase sequenceGeneratorUseCase, ConfigurationImpl configuration) {
        this.goalContentRepositoryGateway = goalContentRepositoryGateway;
        this.sequenceGeneratorUseCase = sequenceGeneratorUseCase;
        this.configuration = configuration;
    }

    public List<GoalContent> getGoalContentListByWorkEffortId(String workEffortId,
                                                              Instant fromDate,
                                                              Instant thruDate) {
        var result = this.goalContentRepositoryGateway.getGoalContentListByWorkEffortId(workEffortId, fromDate, thruDate);
        log.info("getGoalContentListByWorkEffortId result={}", result);
        return result;
    }

    public void createGoalContent(MultipartFile file, String goalId, String description, String contentType, OffsetDateTime fromDate, OffsetDateTime thruDate, String userLoginId) throws GoalContentCreationException {

        GoalContent goalContent = new GoalContent();
        goalContent.setGoalId(goalId);
        goalContent.setDescription(description);
        goalContent.setGoalContentTypeId(contentType);
        goalContent.setFromDate(fromDate);
        goalContent.setThruDate(thruDate);

        // valori cablati
        goalContent.setContentId(this.sequenceGeneratorUseCase.getNextSeqId("Content"));
        goalContent.setDataResourceId(this.sequenceGeneratorUseCase.getNextSeqId("DataResource"));
        goalContent.setContentTypeId("DOCUMENT");
        goalContent.setContentStatusId("CTNT_INITIAL_DRAFT");
        goalContent.setDataResourceStatusId("CTNT_IN_PROGRESS");
        goalContent.setIsPublic("Y");
        goalContent.setDataResourceTypeId("LOCAL_FILE");
        goalContent.setDataTemplateTypeId("NONE");

        String fileName = file.getOriginalFilename();
        String extension = fileName.substring(fileName.lastIndexOf(".") + 1);
        goalContent.setContentName(fileName);
        goalContent.setMimeTypeId(this.goalContentRepositoryGateway.getMimeTypeIdByFileExtensionId(extension));


        // salvo file rinominato con dataResourceId
        String newFileName = goalContent.getDataResourceId() + "." + extension;
        String pathStr = configuration.getDocumentPath();
        Path path = Paths.get(pathStr, newFileName);
        goalContent.setObjectInfo(path.toString());

        try {
            Files.createDirectories(path.getParent());
            file.transferTo(path.toFile());

            boolean result = this.goalContentRepositoryGateway.createGoalContent(goalContent, userLoginId);
            log.info("createGoalContent result={}", result);
            if (!result) {
                throw new GoalContentCreationException("Error creating Content");
            }
        } catch (IOException e) {
            throw new GoalContentCreationException("Error saving file");
        }

    }

    public ResponseEntity<Resource> downloadContent(String contentId) throws GoalContentNotFoundException, MalformedURLException {
        var content = this.goalContentRepositoryGateway.getGoalContent(contentId);
        if (content == null) {
            throw new GoalContentNotFoundException("Content not found");
        }

        var filePath = Paths.get(content.getObjectInfo());
        if (!Files.exists(filePath)) {
            throw new GoalContentNotFoundException("File not found");
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(content.getMimeTypeId()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + content.getContentName() + "\"")
                .body(new UrlResource(filePath.toUri()));
    }

    public void updateGoalContent(GoalContent goalContent, String userLoginId) throws GoalContentNotFoundException {
        boolean result = this.goalContentRepositoryGateway.updateGoalContent(goalContent, userLoginId);
        log.info("updateGoalContent result={}", result);
        if (!result) {
            throw new GoalContentNotFoundException("Content not found");
        }
    }

    public void deleteGoalContent(String contentId) throws GoalContentNotFoundException {
        String toDelete = this.goalContentRepositoryGateway.getObjectInfoByContentId(contentId);

        if(toDelete != null) {

            boolean result = this.goalContentRepositoryGateway.deleteGoalContent(contentId);
            log.info("deleteGoalContent result={}", result);

            if (!result) {
                throw new GoalContentNotFoundException("Content not found");
            } else {
                Path path = Paths.get(toDelete);
                try {
                    boolean deleted = Files.deleteIfExists(path);
                    if (deleted) {
                        log.info("Successfully deleted File");
                    } else {
                        log.info("File not found");
                    }
                } catch (IOException e) {
                    throw new FileDeletionException("Error deleting file: " + e.getMessage(), e);
                }
            }
        } else { throw new GoalContentNotFoundException("Content not found"); }

    }
}
