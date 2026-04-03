package it.mapsgroup.gzoom.mybatis.mapper;

import it.mapsgroup.gzoom.mybatis.dto.Report;
import it.mapsgroup.gzoom.mybatis.dto.ReportType;
import it.mapsgroup.gzoom.mybatis.dto.WorkEffortAssoc;
import it.mapsgroup.gzoom.mybatis.dto.WorkEffortTypeExt;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface ReportMapper {
    List<Report> getReports(@Param("parentTypeId") String parentTypeId, @Param("filter") Map<String, Object> filter);

    List<Report> getReportsByWorkEffortTypeId(@Param("workEffortTypeId") String workEffortTypeId);

    Report getReport(@Param("reportContentId") String reportContentId, @Param("parentTypeId") String parentTypeId, @Param("workEffortTypeId") String workEffortTypeId);

    List<Report> getAnalysisReports(@Param("parentTypeId") String parentTypeId);

    Report getAnalysisReport(@Param("parentTypeId") String parentTypeId, @Param("reportContentId") String reportContentId);

    List<ReportType> getReportType(@Param("reportContentId") String reportContentId);

    @ResultType(WorkEffortAssoc.class)
    List<WorkEffortAssoc> getChildRootEquality(@Param("workEffortId") String workEffortId);

    List<WorkEffortTypeExt> getAnalysisWorkEffortTypeContents(@Param("parentTypeId") String parentTypeId, @Param("reportContentId") String reportContentId, @Param("reportName") String reportName);
}
