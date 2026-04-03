package it.mapsgroup.gzoom.configuration.spring;

import it.mapsgroup.gzoom.entity.goalfile.gateway.*;
import it.mapsgroup.gzoom.entity.goalfile.util.GoalMapper;
import it.mapsgroup.gzoom.entity.orgchart.gateway.OrganizationUnitRepositoryGateway;
import it.mapsgroup.gzoom.entity.user.gateway.PermissionViewRepositoryGateway;
import it.mapsgroup.gzoom.entity.sequencegenerator.gateway.SequenceGeneratorGateway;
import it.mapsgroup.gzoom.goalfile.*;
import it.mapsgroup.gzoom.orgchart.OrganizationUnitUseCase;
import it.mapsgroup.gzoom.user.PermissionUseCase;
import it.mapsgroup.gzoom.sequencegenerator.usecase.SequenceGeneratorUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class UseCasesConfiguration {
    @Bean
    public GoalFileUseCase getGoalFileUseCase(GoalAssocUseCase goalAssocUseCase,
                                              GoalMapper goalMapper,
                                              GoalRepositoryGateway goalRepositoryGateway) {
        return new GoalFileUseCase(goalAssocUseCase, goalMapper, goalRepositoryGateway);
    }

    @Bean
    public GoalAssocUseCase getGoalFileAssocUseCase(GoalAssocRepositoryGateway goalAssocRepositoryGateway) {
        return new GoalAssocUseCase(goalAssocRepositoryGateway);
    }

    @Bean
    public GoalStatusUseCase getGoalStatusUseCase(GoalRepositoryGateway goalFileRepositoryGateway,
                                                  GoalStatusRepositoryGateway goalStatusRepositoryGateway,
                                                  GoalNoteUseCase goalNoteUseCase,
                                                  GoalStatusHistoryUseCase goalStatusHistoryUseCase) {
        return new GoalStatusUseCase(goalFileRepositoryGateway, goalStatusRepositoryGateway, goalNoteUseCase,
                goalStatusHistoryUseCase);
    }

    @Bean
    public GoalNoteUseCase getGoalFileNoteUseCase(GoalNoteRepositoryGateway goalNoteRepositoryGateway,
                                                  SequenceGeneratorUseCase sequenceGeneratorUseCase) {
        return new GoalNoteUseCase(goalNoteRepositoryGateway, sequenceGeneratorUseCase);
    }

    @Bean
    public GoalContentUseCase getGoalContentUseCase(GoalContentRepositoryGateway goalContentRepositoryGateway,
                                                    SequenceGeneratorUseCase sequenceGeneratorUseCase,
                                                    it.mapsgroup.gzoom.service.ConfigurationImpl configuration) {
        return new GoalContentUseCase(goalContentRepositoryGateway, sequenceGeneratorUseCase, configuration);
    }

    @Bean
    public OrganizationUnitUseCase getOrganizationUnitUseCase(OrganizationUnitRepositoryGateway organizationUnitRepositoryGateway,
                                                              LanguageConfiguration languageConfiguration,
                                                              PermissionUseCase permissionUseCase) {
        return new OrganizationUnitUseCase(organizationUnitRepositoryGateway, languageConfiguration, permissionUseCase);
    }


    @Bean
    public PermissionUseCase getPermissionUseCase(PermissionViewRepositoryGateway permissionRepositoryGateway) {
        return new PermissionUseCase(permissionRepositoryGateway);
    }

    @Bean
    public SequenceGeneratorUseCase getSequenceGeneratorUseCase(SequenceGeneratorGateway sequenceGeneratorGateway) {
        return new SequenceGeneratorUseCase(sequenceGeneratorGateway);
    }

    @Bean
    public GoalStatusHistoryUseCase getGoalFileStatusHistoryUseCase(GoalStatusHistoryRepositoryGateway goalStatusHistoryRepositoryGateway) {
        return new GoalStatusHistoryUseCase(goalStatusHistoryRepositoryGateway);
    }
}
