package it.mapsgroup.gzoom.entity.goalfile.util;

import it.mapsgroup.gzoom.entity.goalfile.model.Goal;
import it.mapsgroup.gzoom.entity.goalfile.model.GoalAssoc;
import it.mapsgroup.gzoom.entity.goalfile.model.GoalFile;
import org.mapstruct.*;

import java.math.BigDecimal;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, imports = {BigDecimal.class, Double.class})
public interface GoalMapper {
    @Mapping(target = ".", source = "toGoal")
    @Mapping(target = "children", ignore = true)
    @Mapping(target = "organizationUnitType", ignore = true)
    @Mapping(target = "organizationUnit", ignore = true)
    @Mapping(target = "supervisorOrganizationUnit", ignore = true)
    @Mapping(target = "topOrganizationUnit", ignore = true)
    @Mapping(target = "goalRevision", ignore = true)
    @Mapping(target = "goalPeriod", ignore = true)
    @Mapping(target = "goalFileParent", ignore = true)
    @Mapping(target = "userLoginId", ignore = true)
    Goal extractToGoalFromGoalAssoc(GoalAssoc goalAssoc);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "goalType", ignore = true)
    @Mapping(target = "name", ignore = true)
    @Mapping(target = "code", ignore = true)
    @Mapping(target = "goalPeriod", ignore = true)
    @Mapping(target = "goalFileParent", ignore = true)
    @Mapping(target = "goalHierarchy", ignore = true)
    void updateGoalFromGoalFile(Goal goalHierarchy, @MappingTarget Goal goal);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    GoalFile extractGoalFileParentId(Goal goal);
}
