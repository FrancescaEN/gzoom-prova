package it.mapsgroup.gzoom.infrastructure.db.repository.mybatis.mapper.goalfile;

import it.mapsgroup.gzoom.infrastructure.goalfile.dto.WorkEffortAssocDto;
import jakarta.validation.constraints.NotNull;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface WorkEffortAssocDtoMapper {
    List<WorkEffortAssocDto> selectWorkEffortRootTreeWithDetails(@NotNull String workEffortId,
                                                                 @NotNull String userLoginId);
}