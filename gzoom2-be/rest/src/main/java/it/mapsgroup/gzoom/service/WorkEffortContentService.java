package it.mapsgroup.gzoom.service;

import it.mapsgroup.gzoom.commons.InfoPage;
import it.mapsgroup.gzoom.model.Result;
import it.mapsgroup.gzoom.mybatis.dto.*;
import it.mapsgroup.gzoom.mybatis.dao.ContentDao;
import it.mapsgroup.gzoom.mybatis.dao.DataResourceDao;
import it.mapsgroup.gzoom.mybatis.dao.WorkEffortContentDao;
import it.mapsgroup.gzoom.mybatis.dao.WorkEffortContentTypeDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import it.mapsgroup.gzoom.model.Messages;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static it.mapsgroup.gzoom.security.Principals.principal;

@Service
public class WorkEffortContentService {
    private final Configuration config;
    private final WorkEffortContentDao workEffortContentDao;
    private final ContentDao contentDao;
    private final WorkEffortContentTypeDao workEffortContentTypeDao;
    private final DataResourceDao dataResourceDao;
    private final DataResourceService dataResourceService;

    @Autowired
    public WorkEffortContentService(Configuration config, WorkEffortContentDao workEffortContentDao, ContentDao contentDao, WorkEffortContentTypeDao workEffortContentTypeDao, DataResourceDao dataResourceDao, DataResourceService dataResourceService) {
        this.config = config;
        this.workEffortContentDao = workEffortContentDao;
        this.contentDao = contentDao;
        this.workEffortContentTypeDao = workEffortContentTypeDao;
        this.dataResourceDao = dataResourceDao;
        this.dataResourceService = dataResourceService;
    }


    public Result<WorkEffortContentEx> getWorkEffortContentExList() {
        List<WorkEffortContentEx> list = this.workEffortContentDao.getWorkEffortContentExList();
        return new Result<>(list, list.size());
    }
    public Result<WorkEffortContentEx> getWorkEffortContentListExFilter(InfoPage infoPage) {
        List<WorkEffortContentEx> list = this.workEffortContentDao.getWorkEffortContentListExFilter( infoPage);
        return new Result<>(list, list.size());
    }

    @Transactional
    public WorkEffortContentEx createWorkEffortContentEx(WorkEffortContentEx req) {
        Messages msg = new Messages();

        Validators.assertNotBlank(req.getWorkEffortId(), msg.getMessageColumn(Messages.WORK_EFFORT_CONTENT, Messages.WORK_EFFORT_ID, Messages.IS_REQUIRED));
        Validators.assertNotNull(req.getFromDate(), msg.getMessageColumn(Messages.WORK_EFFORT_CONTENT, Messages.FROM_DATE, Messages.IS_REQUIRED));

        WorkEffortContentType workEffortContentType = this.workEffortContentTypeDao.getContentTypeId(req.getWorkEffortId(), req.getWorkEffortContentTypeId());
        Content content = req.getContent();
        content.setContentTypeId(workEffortContentType.getContentTypeId());
        Validators.assertNotNull(content, msg.getMessageTable(Messages.CONTENT, Messages.IS_REQUIRED) );
        Validators.assertNotBlank(content.getContentTypeId(), msg.getMessageColumn(Messages.CONTENT, Messages.CONTENT_TYPE_ID, Messages.IS_REQUIRED));
        Validators.assertNotBlank(content.getDescription(), msg.getMessageColumn(Messages.CONTENT, Messages.DESCRIPTION, Messages.IS_REQUIRED));
        if (config.getLanguageType().equals("BILING")) {
            Validators.assertNotBlank(content.getDescriptionLang(), msg.getMessageColumn(Messages.CONTENT, Messages.DESCRIPTION_LANG, Messages.IS_REQUIRED));
        }

        DataResource dataResource = new DataResource();
        dataResource.setDataResourceTypeId("LOCAL_FILE");
        dataResource.setDataTemplateTypeId("NONE");
        dataResource.setStatusId("CTNT_IN_PROGRESS");
        dataResource.setDataResourceName(content.getContentName());
        String dataResourceId = this.dataResourceDao.createRetId(dataResource, principal().getUserLoginId());
        Validators.assertNotNull(dataResourceId, msg.getMessageColumn(Messages.DATA_RESOURCE, Messages.DATA_RESOURCE_ID, Messages.INVALID));

        content.setStatusId("CTNT_INITIAL_DRAFT");
        content.setDataResourceId(dataResourceId);
        content.setTemplateDataResourceId(dataResourceId);
        String contentId = this.contentDao.createRetId(content, principal().getUserLoginId());
        Validators.assertNotNull(contentId, msg.getMessageColumn(Messages.CONTENT, Messages.CONTENT_ID, Messages.INVALID));
        req.setContentId(contentId);

        WorkEffortContent workEffortContent = (WorkEffortContent) req;
        this.workEffortContentDao.create(workEffortContent, principal().getUserLoginId());
        return this.workEffortContentDao.getWorkEffortContentEx(dataResourceId);

    }

    @Transactional
    public boolean updateWorkEffortContentEx(WorkEffortContentEx req) {
        Messages msg = new Messages();
        Validators.assertNotBlank(req.getWorkEffortId(), msg.getMessageColumn(Messages.WORK_EFFORT_CONTENT, Messages.WORK_EFFORT_ID, Messages.IS_REQUIRED));
        Validators.assertNotNull(req.getFromDate(), msg.getMessageColumn(Messages.WORK_EFFORT_CONTENT, Messages.FROM_DATE, Messages.IS_REQUIRED));
        WorkEffortContentType workEffortContentType = this.workEffortContentTypeDao.getContentTypeId(req.getWorkEffortId(), req.getWorkEffortContentTypeId());
        Content content = req.getContent();
        content.setContentTypeId(workEffortContentType.getContentTypeId());
        Validators.assertNotNull(content, msg.getMessageTable(Messages.CONTENT, Messages.IS_REQUIRED) );
        Validators.assertNotBlank(content.getContentTypeId(), msg.getMessageColumn(Messages.CONTENT, Messages.CONTENT_TYPE_ID, Messages.IS_REQUIRED));
        Validators.assertNotBlank(content.getDescription(), msg.getMessageColumn(Messages.CONTENT, Messages.DESCRIPTION, Messages.IS_REQUIRED));
        if (config.getLanguageType().equals("BILING")) {
            Validators.assertNotBlank(content.getDescriptionLang(), msg.getMessageColumn(Messages.CONTENT, Messages.DESCRIPTION_LANG, Messages.IS_REQUIRED));
        }

        DataResource dataResource = req.getDataResource();
        dataResource.setDataResourceTypeId("LOCAL_FILE");
        dataResource.setDataTemplateTypeId("NONE");
        dataResource.setStatusId("CTNT_IN_PROGRESS");
        dataResource.setDataResourceName(content.getContentName());
        this.dataResourceDao.update(dataResource, principal().getUserLoginId());

        content.setStatusId("CTNT_INITIAL_DRAFT");
        content.setDataResourceId(dataResource.getDataResourceId());
        content.setTemplateDataResourceId(dataResource.getDataResourceId());
        this.contentDao.update(content, principal().getUserLoginId());
        return  this.workEffortContentDao.update(req, principal().getUserLoginId());
    }

    @Transactional
    public boolean deleteWorkEffortContentEx(WorkEffortContentEx req) {
        Messages msg = new Messages();
        Validators.assertNotBlank(req.getWorkEffortId(), msg.getMessageColumn(Messages.WORK_EFFORT_CONTENT, Messages.WORK_EFFORT_ID, Messages.IS_REQUIRED));
        Validators.assertNotBlank(req.getContentId(), msg.getMessageColumn(Messages.WORK_EFFORT_CONTENT, Messages.CONTENT_ID, Messages.IS_REQUIRED));
        Validators.assertNotBlank(req.getWorkEffortContentTypeId(), msg.getMessageColumn(Messages.WORK_EFFORT_CONTENT, Messages.WORK_EFFORT_CONTENT_TYPE_ID, Messages.IS_REQUIRED));
        Validators.assertNotNull(req.getFromDate(), msg.getMessageColumn(Messages.WORK_EFFORT_CONTENT, Messages.FROM_DATE, Messages.IS_REQUIRED));
        Validators.assertNotBlank(req.getDataResource().getDataResourceId(), msg.getMessageColumn(Messages.DATA_RESOURCE, Messages.DATA_RESOURCE_ID, Messages.IS_REQUIRED));

        if (req.getDataResource().getDataResourceName() != null) Validators.assertTrue(this.dataResourceService.deleteFile(req.getDataResource()), "IMPOSSIBLE DELETE FILE");
        Validators.assertTrue(this.workEffortContentDao.delete((WorkEffortContent) req), "IMPOSSIBLE DELETE WORK_EFFORT_CONTENT");
        Validators.assertTrue(this.contentDao.delete(req.getContent().getContentId()), "IMPOSSIBLE DELETE CONTENT");
        Validators.assertTrue(this.dataResourceDao.delete(req.getDataResource().getDataResourceId()), "IMPOSSIBLE DELETE DATA_RESOURCE");

        return true;
    }
}
