package it.mapsgroup.gzoom.service;

import it.mapsgroup.gzoom.model.Result;
import it.mapsgroup.gzoom.mybatis.dao.ContentDao;
import it.mapsgroup.gzoom.mybatis.dto.Content;
import it.mapsgroup.gzoom.mybatis.dto.ContentExDataResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContentService {
    private final ContentDao contentDao;

    @Autowired
    public ContentService(ContentDao contentDao) {
        this.contentDao = contentDao;
    }

    public Result<ContentExDataResource> getContentListByContentTypeId(String contentTypeId) {
        List<ContentExDataResource> list = this.contentDao.getContentListByContentTypeId(contentTypeId);
        return new Result<>(list, list.size());
    }

    public Result<Content> findByContentTypeId(String contentTypeId) {
        List<Content> list = this.contentDao.findByContentTypeId(contentTypeId);
        return new Result<>(list, list.size());
    }
}
