package it.mapsgroup.gzoom.service;

import it.mapsgroup.gzoom.model.Messages;
import it.mapsgroup.gzoom.model.Result;
import it.mapsgroup.gzoom.mybatis.dao.ContentAssocDao;
import it.mapsgroup.gzoom.mybatis.dao.ContentDao;
import it.mapsgroup.gzoom.mybatis.dao.DataResourceDao;
import it.mapsgroup.gzoom.mybatis.dao.QueryConfigDao;
import it.mapsgroup.gzoom.mybatis.dto.Content;
import it.mapsgroup.gzoom.mybatis.dto.ContentAssoc;
import it.mapsgroup.gzoom.mybatis.dto.QueryConfig;
import it.mapsgroup.gzoom.mybatis.dto.DataResource;
import it.mapsgroup.gzoom.util.MimeTypeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Base64;
import java.util.List;

import static it.mapsgroup.gzoom.security.Principals.principal;

@Service
public class QueryConfigService {

    private final QueryConfigDao queryConfigDao;
    private final DataResourceDao dataResourceDao;
    private final ContentDao contentDao;
    private final ContentAssocDao contentAssocDao;

    @Autowired
    public QueryConfigService(QueryConfigDao queryConfigDao, DataResourceDao dataResourceDao, ContentDao contentDao, ContentAssocDao contentAssocDao) {
        this.queryConfigDao = queryConfigDao;
        this.dataResourceDao = dataResourceDao;
        this.contentDao = contentDao;
        this.contentAssocDao = contentAssocDao;
    }

    public Result<QueryConfig> getAllQueryConfig(String parentTypeId,String queryType, String userLoginId) {
        List<QueryConfig> list = queryConfigDao.getAllQueryConfig(parentTypeId,queryType, userLoginId);
        return new Result<>(list, list.size());
    }

    public QueryConfig getQueryConfig(String id) {
        QueryConfig query = queryConfigDao.getQueryConfig(id);
        //return new Result<>(query, query.size());
        return query;
    }

    public Result<QueryConfig> getQueryConfig() {
        List<QueryConfig> list = queryConfigDao.selectAllOrderByQueryCode();
        return new Result<>(list, list.size());
    }

    public Result<QueryConfig> getQueryConfigByQueryType(String queryType) {
        List<QueryConfig> list = queryConfigDao.selectByQueryTypeOrderByQueryCode(queryType);
        return new Result<>(list, list.size());
    }

    @Transactional
    public String createQueryConfig(QueryConfig queryConfig) {
        queryConfig.setQueryInfo(new String(Base64.getDecoder().decode(queryConfig.getQueryInfo()), StandardCharsets.UTF_8));
        this.validateQueryConfig(queryConfig, "CREATE");
        String queryConfigId = this.queryConfigDao.create(queryConfig);

        if (queryConfig.getQueryType().equals("T")) {
            this.createQueryTypeTNew(queryConfigId, queryConfig);
        }
        return queryConfigId;
    }

    @Transactional
    public boolean updateQueryConfig(QueryConfig queryConfig) {
        queryConfig.setQueryInfo(new String(Base64.getDecoder().decode(queryConfig.getQueryInfo()), StandardCharsets.UTF_8));
        this.validateQueryConfig(queryConfig, "UPDATE");
        Messages msg = new Messages();
        Validators.assertNotBlank(queryConfig.getQueryInfo(), msg.getMessageColumn(Messages.QUERY_CONFIG, Messages.QUERY_INFO, Messages.IS_REQUIRED));
        return queryConfigDao.update(queryConfig);
    }

    @Transactional
    public boolean updateQueryConfigInfoBase(QueryConfig queryConfig) {
        this.validateQueryConfig(queryConfig, "UPDATE");
        return queryConfigDao.updateInfoBase(queryConfig);
    }

    public boolean updateConditions(QueryConfig queryConfig) {
        queryConfig.setQueryInfo(new String(Base64.getDecoder().decode(queryConfig.getQueryInfo()), StandardCharsets.UTF_8));
        this.validateQueryConfig(queryConfig, "UPDATE");
        return queryConfigDao.updateConditions(queryConfig);
    }


    @Transactional
    public boolean deleteQueryConfig(String queryConfigId) {
        Messages msg = new Messages();
        Validators.assertNotBlank(queryConfigId, msg.getMessageColumn(Messages.QUERY_CONFIG, Messages.QUERY_ID, Messages.IS_REQUIRED));
        QueryConfig record = this.queryConfigDao.getQueryConfig(queryConfigId);
        Validators.assertFalse(record == null, msg.getMessageColumn(Messages.QUERY_CONFIG, Messages.QUERY_ID, Messages.INVALID));

        if (record.getQueryType().equals("T")) {
            this.deleteQueryTypeTOld(record);
        }

        return this.queryConfigDao.delete(queryConfigId);
    }

    private void validateQueryConfig(QueryConfig req, String method) {
        Messages msg = new Messages();
        Validators.assertNotNull(req, msg.getMessageTable(Messages.QUERY_CONFIG, Messages.IS_REQUIRED));
        Validators.assertNotBlank(req.getQueryCode(), msg.getMessageColumn(Messages.QUERY_CONFIG, Messages.QUERY_CODE, Messages.IS_REQUIRED));
        Validators.assertNotBlank(req.getQueryName(), msg.getMessageColumn(Messages.QUERY_CONFIG, Messages.QUERY_NAME, Messages.IS_REQUIRED));
        Validators.assertNotBlank(req.getQueryType(), msg.getMessageColumn(Messages.QUERY_CONFIG, Messages.QUERY_TYPE, Messages.IS_REQUIRED));
        Validators.assertNotBlank(req.getExportMimeType(), msg.getMessageColumn(Messages.QUERY_CONFIG, Messages.EXPORT_MIME_TYPE, Messages.IS_REQUIRED));
        Validators.assertNotBlank(req.getQueryCtx(), msg.getMessageColumn(Messages.QUERY_CONFIG, Messages.QUERY_CTX, Messages.IS_REQUIRED));
        Validators.assertNotBlank(req.getQueryPublic(), msg.getMessageColumn(Messages.QUERY_CONFIG, Messages.QUERY_PUBLIC, Messages.IS_REQUIRED));
        Validators.assertNotBlank(req.getQueryActive(), msg.getMessageColumn(Messages.QUERY_CONFIG, Messages.QUERY_ACTIVE, Messages.IS_REQUIRED));
        Validators.assertNotBlank(req.getQueryComm(), msg.getMessageColumn(Messages.QUERY_CONFIG, Messages.QUERY_COMM, Messages.IS_REQUIRED));

        if ( method.equalsIgnoreCase("UPDATE")) {
            Validators.assertNotBlank(req.getQueryId(), msg.getMessageColumn(Messages.QUERY_CONFIG, Messages.QUERY_ID, Messages.IS_REQUIRED));
            QueryConfig record = this.queryConfigDao.getQueryConfig(req.getQueryId());
            Validators.assertFalse(record == null, msg.getMessageColumn(Messages.QUERY_CONFIG, Messages.QUERY_ID, Messages.INVALID));

            if(req.getQueryType().equals("T") && !record.getQueryType().equals("T")) {
                this.createQueryTypeTNew(req.getQueryId(), req);
            } else if(req.getQueryType().equals("T") && !req.getQueryName().equals(record.getQueryName())) {
                this.contentDao.updateDescription("Q" + record.getQueryId(), req.getQueryName(), principal().getUserLoginId());
            }
            if(!req.getQueryType().equals("T") && record.getQueryType().equals("T")) {
                this.deleteQueryTypeTOld(req);
            }
            if(req.getQueryType().equals("T") && !req.getExportMimeType().equals(record.getExportMimeType())) {
                this.updateQueryTypeT(req.getQueryId(), req, record.getExportMimeType());
            }
        }
    }
    private void updateQueryTypeT(String queryId, QueryConfig queryConfig, String preExport) {
        ZoneId zoneId = ZoneId.systemDefault();
        ZoneOffset zoneOffset = zoneId.getRules().getOffset(java.time.Instant.now());
        this.contentAssocDao.delete(getContentIdByExportMimeType(preExport), "Q" + queryId, "TYPE_PRINT", LocalDateTime.of(2011, 1,1, 0, 0, 0).toInstant(zoneOffset));
        createContentAssocExport(queryId, queryConfig, zoneOffset);
    }

    private void createContentAssocExport(String queryId, QueryConfig queryConfig, ZoneOffset zoneOffset) {
        ContentAssoc contentAssoc1 = new ContentAssoc();
        contentAssoc1.setContentId(getContentIdByExportMimeType(queryConfig.getExportMimeType()));
        contentAssoc1.setContentIdTo("Q" + queryId);
        contentAssoc1.setContentAssocTypeId("TYPE_PRINT");
        contentAssoc1.setFromDate(LocalDateTime.of(2011, 1,1, 0, 0, 0).toInstant(zoneOffset));
        contentAssoc1.setSequenceNum(BigDecimal.valueOf(0));
        this.contentAssocDao.create(contentAssoc1, principal().getUserLoginId());
    }

    private void createQueryTypeTNew(String queryConfigId, QueryConfig queryConfig) {
        DataResource dataResource = new DataResource();
        dataResource.setDataResourceId("Q" + queryConfigId);
        dataResource.setDataResourceTypeId("LOCAL_FILE");
        dataResource.setStatusId("CTNT_PUBLISHED");
        dataResource.setDataResourceName(queryConfigId);
        dataResource.setMimeTypeId("text/plain");
        dataResource.setObjectInfo(" ");
        dataResource.setIsPublic("Y");
        this.dataResourceDao.createWithId(dataResource);

        Content content = new Content();
        content.setContentId("Q" + queryConfigId);
        content.setContentTypeId("QUERY_CONFIG");
        content.setDataResourceId("Q" + queryConfigId);
        content.setStatusId("CTNT_IN_PROGRESS");
        content.setContentName(queryConfig.getQueryCode());
        content.setDescription(queryConfig.getQueryName());
        content.setMimeTypeId("text/plain");
        this.contentDao.createWithId(content);

        ZoneId zoneId = ZoneId.systemDefault();
        ZoneOffset zoneOffset = zoneId.getRules().getOffset(java.time.Instant.now());

        ContentAssoc contentAssoc0 = new ContentAssoc();
        contentAssoc0.setContentId("WE_PRINT");
        contentAssoc0.setContentIdTo("Q" + queryConfigId);
        contentAssoc0.setContentAssocTypeId("REP_PERM");
        contentAssoc0.setFromDate(LocalDateTime.of(2011, 1,1, 0, 0, 0).toInstant(zoneOffset));
        contentAssoc0.setSequenceNum(BigDecimal.valueOf(0));
        this.contentAssocDao.create(contentAssoc0, principal().getUserLoginId());
        createContentAssocExport(queryConfigId, queryConfig, zoneOffset);
    }


    private void deleteQueryTypeTOld(QueryConfig queryConfig) {
        ZoneId zoneId = ZoneId.systemDefault();
        ZoneOffset zoneOffset = zoneId.getRules().getOffset(java.time.Instant.now());
        String queryId = queryConfig.getQueryId();
        this.contentAssocDao.delete("WE_PRINT", "Q" + queryId, "REP_PERM", LocalDateTime.of(2011, 1,1, 0, 0, 0).toInstant(zoneOffset));
        this.contentAssocDao.delete(getContentIdByExportMimeType(queryConfig.getExportMimeType()), "Q" + queryId, "TYPE_PRINT", LocalDateTime.of(2011, 1,1, 0, 0, 0).toInstant(zoneOffset));
        this.contentDao.delete("Q" + queryId);
        this.dataResourceDao.delete("Q" + queryId);
    }

    private String getContentIdByExportMimeType(String exportMimeType) {
        String contentId = null;
        if (exportMimeType.equals(MimeTypeEnum.PDF.getExportMimeType())) {
            contentId = MimeTypeEnum.PDF.getContentId();
        }
        else if (exportMimeType.equals(MimeTypeEnum.HTML.getExportMimeType())) {
            contentId = MimeTypeEnum.PDF.getContentId();
        }
        else if (exportMimeType.equals(MimeTypeEnum.CSV.getExportMimeType())) {
            contentId = MimeTypeEnum.CSV.getContentId();
        }
        else if (exportMimeType.equals(MimeTypeEnum.XLSX.getExportMimeType())) {
            contentId = MimeTypeEnum.XLSX.getContentId();
        }
        return contentId;
    }
}
