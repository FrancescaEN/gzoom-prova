package it.mapsgroup.gzoom.infrastructure.db.repository.mybatis.mapper.goalfile;

import it.mapsgroup.gzoom.infrastructure.goalfile.dto.StatusItemChangeDto;
import it.mapsgroup.gzoom.infrastructure.goalfile.dto.StatusItemDto;
import it.mapsgroup.gzoom.infrastructure.goalfile.dto.WorkEffortStatusDto;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Mapper
@Repository
public interface WorkEffortStatusDtoMapper {
	StatusItemDto selectStatusById(String id);

	List<StatusItemChangeDto> selectAvailableGoalStatus(String workEffortId,
                                                        String statusId);

	void createWorkEffortStatus(WorkEffortStatusDto workEffortStatusDto, String userLoginId, Instant instant);
}