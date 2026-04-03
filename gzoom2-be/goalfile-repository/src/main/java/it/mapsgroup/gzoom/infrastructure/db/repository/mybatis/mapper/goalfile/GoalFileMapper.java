package it.mapsgroup.gzoom.infrastructure.db.repository.mybatis.mapper.goalfile;

import it.mapsgroup.gzoom.infrastructure.goalfile.dto.WorkEffortDto;
import jakarta.validation.constraints.NotNull;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Mapper
@Repository
public interface GoalFileMapper {
    List<WorkEffortDto> selectWorkEffortRoots(@NotNull String userLoginId);

    WorkEffortDto searchWorkEffortById(String id, Instant currentTimestamp);

    void updateWorkEffort(WorkEffortDto workEffortDto);

    List<WorkEffortDto> searchReferenceWorkEffortChildren(String goalFileId);
}