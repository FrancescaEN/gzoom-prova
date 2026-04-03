package it.mapsgroup.gzoom.infrastructure.configuration.spring;

import it.mapsgroup.gzoom.entity.goalfile.gateway.*;
import it.mapsgroup.gzoom.entity.orgchart.gateway.OrganizationUnitRepositoryGateway;
import it.mapsgroup.gzoom.entity.user.gateway.PermissionViewRepositoryGateway;
import it.mapsgroup.gzoom.entity.sequencegenerator.gateway.SequenceGeneratorGateway;
import it.mapsgroup.gzoom.infrastructure.db.repository.mybatis.gateway.goalfile.*;
import it.mapsgroup.gzoom.infrastructure.db.repository.mybatis.gateway.orgchart.OrganizationUnitMyBatisRepositoryGateway;
import it.mapsgroup.gzoom.infrastructure.db.repository.mybatis.gateway.orgchart.PermissionViewMyBatisRepositoryGateway;
import it.mapsgroup.gzoom.infrastructure.db.repository.mybatis.mapper.goalfile.*;
import it.mapsgroup.gzoom.infrastructure.db.repository.mybatis.mapper.orgchart.OrganizationUnitMapper;
import it.mapsgroup.gzoom.infrastructure.db.repository.mybatis.mapper.user.PermissionViewMapper;
import it.mapsgroup.gzoom.infrastructure.goalfile.util.*;
import it.mapsgroup.gzoom.infrastructure.orgchart.util.PartyDtoOrganizationUnitMapper;
import it.mapsgroup.gzoom.infrastructure.sequencegenerator.gateway.SequenceGeneratorOldFashionGateway;
import it.mapsgroup.gzoom.infrastructure.user.util.UserCtxPermissionViewDtoPermissionMapper;
import it.mapsgroup.gzoom.persistence.common.SequenceGenerator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RepositoryConfiguration {
    @Bean
    @ConditionalOnProperty(value = "gzoom.repository.type", havingValue = "mybatis", matchIfMissing = true)
    public GoalAssocRepositoryGateway getGoalFileAssocRepositoryGateway(WorkEffortAssocDtoMapper workEffortAssocDtoMapper
            , WorkEffortAssocDtoGoalAssocMapper workEffortAssocDtoGoalAssocMapper) {
       return new WorkEffortAssocMyBatisRepositoryGateway(workEffortAssocDtoMapper, workEffortAssocDtoGoalAssocMapper);
    }

    @Bean
    @ConditionalOnProperty(value = "gzoom.repository.type", havingValue = "mybatis", matchIfMissing = true)
    public GoalRepositoryGateway getGoalFileRepositoryGateway(GoalFileMapper workEffortMapper,
                                                              WorkEffortDtoGoalMapper workEffortDtoGoalMapper) {
        return new WorkEffortMyBatisRepositoryGateway(workEffortMapper, workEffortDtoGoalMapper);
    }

    @Bean
    @ConditionalOnProperty(value = "gzoom.repository.type", havingValue = "mybatis", matchIfMissing = true)
    public GoalStatusRepositoryGateway getGoalFileStatusGateway(WorkEffortStatusDtoMapper workEffortStatusDtoMapper,
                                                                StatusItemDtoGoalStatusMapper statusItemDtoGoalStatusMapper) {
        return new GoalStatusMyBatisRepositoryGateway(workEffortStatusDtoMapper, statusItemDtoGoalStatusMapper);
    }

    @Bean
    @ConditionalOnProperty(
            value = {"gzoom.repository.type"},
            havingValue = "mybatis",
            matchIfMissing = true
    )
    public GoalNoteRepositoryGateway getGoalFileNoteGateway(GoalNoteMapper goalNoteMapper, WorkEffortTypeAttrDtoGoalNoteMapper workEffortTypeAttrDtoGoalNoteMapper) {
        return new GoalNoteMyBatisRepositoryGateway(goalNoteMapper, workEffortTypeAttrDtoGoalNoteMapper);
    }

    @Bean
    @ConditionalOnProperty(
            value = {"gzoom.repository.type"},
            havingValue = "mybatis",
            matchIfMissing = true
    )
    public GoalContentRepositoryGateway getGoalContentGateway(GoalContentMapper goalContentMapper,
                                                              WorkEffortContentDtoGoalContentMapper workEffortContentDtoGoalContentMapper,
                                                              ContentDtoGoalContentMapper contentDtoGoalContentMapper,
                                                              DataResourceDtoGoalContentMapper dataResourceDtoGoalContentMapper) {
        return new GoalContentMyBatisRepositoryGateway(goalContentMapper, workEffortContentDtoGoalContentMapper, contentDtoGoalContentMapper, dataResourceDtoGoalContentMapper);
    }

    @Bean
    @ConditionalOnProperty(value = "gzoom.repository.type", havingValue = "mybatis", matchIfMissing = true)
    public PermissionViewRepositoryGateway getPermissionViewGateway(PermissionViewMapper permissionViewMapper,
                                                                    UserCtxPermissionViewDtoPermissionMapper userCtxPermissionViewDtoPermissionMapper) {
        return new PermissionViewMyBatisRepositoryGateway(permissionViewMapper, userCtxPermissionViewDtoPermissionMapper);
    }

    @Bean
    @ConditionalOnProperty(value = "gzoom.repository.type", havingValue = "mybatis", matchIfMissing = true)
    public OrganizationUnitRepositoryGateway getOrganizationUnitGateway(OrganizationUnitMapper organizationUnitMapper,
                                                                        PartyDtoOrganizationUnitMapper partyDtoOrganizationUnitMapper) {
        return new OrganizationUnitMyBatisRepositoryGateway(organizationUnitMapper, partyDtoOrganizationUnitMapper);
    }

    @Bean
    @ConditionalOnProperty(value = "gzoom.repository.type", havingValue = "mybatis", matchIfMissing = true)
    public GoalStatusHistoryRepositoryGateway getGoalFileStatusHistoryRepositoryGateway(
            WorkEffortStatusDtoMapper workEffortStatusDtoMapper,
            WorkEffortStatusDtoGoalStatusMapper workEffortStatusDtoGoalStatusMapper) {
        return new WorkEffortStatusMyBatisRepositoryGateway(workEffortStatusDtoMapper, workEffortStatusDtoGoalStatusMapper);
    }

    @Bean
    @ConditionalOnProperty(
            value = {"gzoom.repository.type"},
            havingValue = "mybatis",
            matchIfMissing = true
    )
    public SequenceGeneratorGateway getSequenceGeneratorGateway(SequenceGenerator sequenceGenerator) {
        return new SequenceGeneratorOldFashionGateway(sequenceGenerator);
    }
}
