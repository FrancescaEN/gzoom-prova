package it.mapsgroup.gzoom.service;

import it.mapsgroup.gzoom.model.Result;
import it.mapsgroup.gzoom.mybatis.dao.ContentTypeDao;
import it.mapsgroup.gzoom.mybatis.dto.ContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContentTypeService {
    private final ContentTypeDao contentTypeDao;

    @Autowired
    public ContentTypeService( ContentTypeDao contentTypeDao) {

        this.contentTypeDao = contentTypeDao;
    }

    public Result<ContentType> getContentTypeWithParentTypeId(String parentTypeId) {
        List<ContentType> list = this.contentTypeDao.getContentTypeWithParentTypeId(parentTypeId);
        return new Result<>(list, list.size());
    }
}
