package it.mapsgroup.gzoom.service.report;

import it.mapsgroup.gzoom.model.Report;
import it.mapsgroup.gzoom.model.Result;
import it.mapsgroup.gzoom.mybatis.dao.ReportDao;
import it.mapsgroup.gzoom.mybatis.dao.WorkEffortTypeContentDao;
import it.mapsgroup.gzoom.mybatis.dto.ReportParams;
import it.mapsgroup.gzoom.mybatis.dto.ReportType;
import it.mapsgroup.gzoom.mybatis.dto.WorkEffortTypeExt;
import it.mapsgroup.gzoom.service.DtoMapper;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static it.mapsgroup.gzoom.security.Principals.principal;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * Profile service.
 *
 */
@Service
public class ReportService {
    private static final Logger LOG = getLogger(ReportService.class);

    private final ReportClientService client;
    
    private final ReportDao reportDao;
    private final WorkEffortTypeContentDao workEffortTypeContentDao;
    private final DtoMapper dtoMapper;

    @Autowired
    public ReportService(ReportClientService client, ReportDao reportDao, DtoMapper dtoMapper,
                         WorkEffortTypeContentDao workEffortTypeContentDao) {
    	this.client = client;
        this.reportDao = reportDao;
        this.workEffortTypeContentDao = workEffortTypeContentDao;
        this.dtoMapper = dtoMapper;
    }

    public Result<Report> getReportsByWorkEffortTypeId(String workEffortTypeId) {

        List<it.mapsgroup.gzoom.mybatis.dto.Report> list = reportDao.getReportsByWorkEffortTypeId(workEffortTypeId);
        List<Report> ret = list.stream().map(p -> dtoMapper.copy(p, new Report())).collect(Collectors.toList());

        LOG.info("getReportsByWorkEffortTypeId size="+ ret.size());
        return new Result<>(ret, ret.size());
    }

    public Result<Report> getReports(String parentTypeId) {
    	
    	List<it.mapsgroup.gzoom.mybatis.dto.Report> list = reportDao.getReports(parentTypeId, principal().getUserLoginId());
    	List<Report> ret = list.stream().map(p -> dtoMapper.copy(p, new Report())).collect(Collectors.toList());

//        GN-6181
//    	List<it.mapsgroup.gzoom.mybatis.dto.Report> listAnalysis =  reportDao.getAnalysisReports(parentTypeId);
//        ret.addAll(listAnalysis.stream().map(p -> dtoMapper.copy(p, new Report())).collect(Collectors.toList()));

        LOG.info("getReports size="+ ret.size());
        return new Result<>(ret, ret.size());
    }

    public Report getReport(String parentTypeId, String reportContentId, String resourceName, String workEffortTypeId) {
        LOG.info("Start getReport");
    	it.mapsgroup.gzoom.mybatis.dto.Report report = null;
    	List<WorkEffortTypeExt> workEffortTypes = null;

        report = reportDao.getReport(parentTypeId, reportContentId, resourceName, workEffortTypeId);
        workEffortTypes = workEffortTypeContentDao.getWorkEffortTypeContents(parentTypeId, reportContentId, resourceName, workEffortTypeId);

        Report ret = dtoMapper.copy(report, new Report());
        ret.setWorkEffortTypes(workEffortTypes);

        //carico la lista di formati
        List<ReportType> outputFormats = reportDao.getReportType(reportContentId);
        ret.setOutputFormats(outputFormats);
        LOG.info("outputFormats="+outputFormats);

        if(!ret.getReportContentTypeId().equals("QUERY_CONFIG")) {
            ReportParams params = getParams(parentTypeId, ret.getResourceName(), ret.getContentName());
            ret.setParams(params.getParams());
            ret.setServices(params.getServices());
            LOG.info("params=" + params);
        }

        LOG.info("End getReport=" + ret);
        return ret;
    }
    
    /**
     *  Dato il noem del report  mi carico la lista dei parametri
     * @param
     * @return
     */
	private ReportParams getParams(String parentTypeId, String resourceName, String contentName) {
		ResponseEntity<ReportParams> params = client.getReportParams(parentTypeId, resourceName, contentName);
        return params.getBody();
    }	

}
