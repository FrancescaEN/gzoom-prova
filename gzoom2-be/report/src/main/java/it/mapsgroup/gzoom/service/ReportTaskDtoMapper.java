package it.mapsgroup.gzoom.service;

import it.mapsgroup.gzoom.mybatis.dto.Content;
import it.mapsgroup.gzoom.mybatis.dto.DataResource;
import it.mapsgroup.gzoom.mybatis.dto.ReportActivity;
import org.springframework.stereotype.Component;

/**
 * @author Andrea Fossi.
 */
@Component
public class ReportTaskDtoMapper {

    public Content getContent(ReportActivity from, String dataResourceId) {
        Content to = new Content();
        to.setContentTypeId("TMP_ENCLOSE");
        to.setDataResourceId(dataResourceId);//data resource id
        to.setStatusId("CTNT_INITIAL_DRAFT");
        to.setServiceName(from.getActivityId());//scheduled job id
        to.setContentName(from.getReportName());//report file name (when downloaded)
        to.setMimeTypeId("application/pdf");
        to.setCreatedByUserLogin(from.getCreatedByUserLogin());
        to.setLastModifiedByUserLogin(from.getLastModifiedByUserLogin());
        return to;
    }

    public DataResource getDataResource(ReportActivity from) {
        DataResource to = new DataResource();
        to.setDataResourceTypeId("LOCAL_FILE");
        to.setDataTemplateTypeId("NONE");
        to.setStatusId("CTNT_IN_PROGRESS");
        to.setDataResourceName(from.getReportName());
        to.setMimeTypeId("application/pdf");
        to.setObjectInfo(from.getObjectInfo());
        to.setIsPublic("N");
        to.setCreatedByUserLogin(from.getCreatedByUserLogin());
        to.setLastModifiedByUserLogin(from.getLastModifiedByUserLogin());
        return to;
    }
}
