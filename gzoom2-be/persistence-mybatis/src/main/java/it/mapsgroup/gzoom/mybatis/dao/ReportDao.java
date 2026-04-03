package it.mapsgroup.gzoom.mybatis.dao;

import it.mapsgroup.gzoom.mybatis.dto.Report;
import it.mapsgroup.gzoom.mybatis.dto.ReportType;
import it.mapsgroup.gzoom.mybatis.dto.WorkEffortAssoc;
import it.mapsgroup.gzoom.mybatis.dto.WorkEffortTypeExt;
import it.mapsgroup.gzoom.mybatis.mapper.ReportMapper;
import it.mapsgroup.gzoom.mybatis.service.FilterService;
import it.mapsgroup.gzoom.mybatis.service.PermissionService;
import it.mapsgroup.gzoom.mybatis.util.ContextPermissionPrefixEnum;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.List;
import java.util.Map;

import static org.slf4j.LoggerFactory.getLogger;
@Service
public class ReportDao {
    private static final Logger LOG = getLogger(ReportDao.class);
    private final PermissionService permissionService;
    private final FilterService filterService;
    private final ReportMapper reportMapper;

    @Autowired
    public ReportDao(PermissionService permissionService, FilterService filterService, ReportMapper reportMapper) {
        this.permissionService = permissionService;
        this.filterService = filterService;
        this.reportMapper = reportMapper;
    }

    /**
     * Prendo la lista dei report per quel modulo
     * @param parentTypeId
     * @return
     */
    @Transactional
    public List<Report> getReports(String parentTypeId, String userLoginId) {
        LOG.info("getReports");

        Map<String, Object> filterPermission = this.filterService.setMapFilter(userLoginId, ContextPermissionPrefixEnum.valueOf(parentTypeId));
        List<Report> reports = this.reportMapper.getReports(parentTypeId, filterPermission);
        LOG.info("size = {}", reports.size());
        return reports;
    }

    @Transactional
    public List<Report> getReportsByWorkEffortTypeId(String workEffortTypeId) {
        LOG.info("getReportsByWorkEffortTypeId");

        List<Report> reports = this.reportMapper.getReportsByWorkEffortTypeId(workEffortTypeId);
        LOG.info("size = {}", reports.size());
        return reports;
    }


    @Transactional
    public Report getReport(String parentTypeId, String reportContentId, String resourceName, String workEffortTypeId)  {
        LOG.info("getReport");

        Report report = this.reportMapper.getReport(reportContentId, parentTypeId, workEffortTypeId);
        LOG.info("Report = {}", (report != null));
        return report;
    }


    /**
     * Prendo la lista dei report collegati all'analisi
     * @param parentTypeId
     * @return
     */
    @Transactional
    public List<Report> getAnalysisReports(String parentTypeId) {
        LOG.info("getAnalysisReports");

        List<Report> reports = this.reportMapper.getAnalysisReports(parentTypeId);
        LOG.info("size = {}", reports.size());
        return reports;
    }

    /**
     * TODO aggiunege la lista di condizione per etichette
     * Prendo la lista dei report collegati all'analisi
     * @param parentTypeId
     * @return
     */
    @Transactional
    public Report getAnalysisReport(String parentTypeId, String reportContentId, String reportName)  {
        LOG.info("getAnalysisReport");

        Report report = this.reportMapper.getAnalysisReport(parentTypeId, reportContentId);
        LOG.info("Report = {}", (report != null));
        return report;
    }


    @Transactional
    public List<ReportType> getReportType(String reportContentId) {
        LOG.info("getReportType");

        List<ReportType> reportType = this.reportMapper.getReportType(reportContentId);
        LOG.info("size = {}", reportType.size());
        return reportType;
    }

    @Transactional
    public List<WorkEffortAssoc> getChildRootEquality(String workEffortId) {
        LOG.info("getChildRootEquality");

        List<WorkEffortAssoc> workEffortAssocList = this.reportMapper.getChildRootEquality(workEffortId);
        LOG.info("size = {}", workEffortAssocList.size());
        return workEffortAssocList;
    }

    @Transactional
    public List<WorkEffortTypeExt> getAnalysisWorkEffortTypeContents(String parentTypeId, String reportContentId, String reportName) {
        LOG.info("getAnalysisWorkEffortTypeContents");

        List<WorkEffortTypeExt> workEffortTypeExts = this.reportMapper.getAnalysisWorkEffortTypeContents(parentTypeId, reportContentId, reportName);
        LOG.info("size = {}", workEffortTypeExts.size());
        return workEffortTypeExts;
    }

}
