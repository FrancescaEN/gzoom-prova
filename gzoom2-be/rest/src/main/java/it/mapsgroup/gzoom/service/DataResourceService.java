package it.mapsgroup.gzoom.service;

import it.mapsgroup.gzoom.mybatis.dao.ContentDao;
import it.mapsgroup.gzoom.mybatis.dao.DataResourceDao;
import it.mapsgroup.gzoom.mybatis.dao.WorkEffortContentDao;
import it.mapsgroup.gzoom.mybatis.dto.Content;
import it.mapsgroup.gzoom.mybatis.dto.DataResource;
import it.mapsgroup.gzoom.mybatis.dto.WorkEffortContentEx;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static it.mapsgroup.gzoom.security.Principals.principal;
import static org.slf4j.LoggerFactory.getLogger;

@Service
public class DataResourceService {
    private final DataResourceDao dataResourceDao;
    private final WorkEffortContentDao workEffortContentDao;
    private final ContentDao contentDao;
    private final ConfigurationImpl configuration;
    private static final Logger LOG = getLogger(DataResourceService.class);

    @Autowired
    public DataResourceService(DataResourceDao dataResourceDao, WorkEffortContentDao workEffortContentDao, ContentDao contentDao, ConfigurationImpl configuration) {
        this.dataResourceDao = dataResourceDao;
        this.workEffortContentDao = workEffortContentDao;
        this.contentDao = contentDao;
        this.configuration = configuration;
    }

    public WorkEffortContentEx uploadFile(MultipartFile file, String nameFile, String dataResourceId) throws IOException {

        Path path = Paths.get(this.configuration.getOfbizPath() + "/runtime/uploads/attachments/");
        Files.createDirectories(path);
        String extension = "." + FilenameUtils.getExtension(nameFile);
        file.transferTo(Paths.get(path + "/" + dataResourceId + extension));

        WorkEffortContentEx workEffortContentEx = workEffortContentDao.getWorkEffortContentEx(dataResourceId);
        DataResource dataResource = workEffortContentEx.getDataResource();
        Content content = workEffortContentEx.getContent();

        dataResource.setMimeTypeId(file.getContentType());
        dataResource.setDataResourceName(nameFile);
        dataResource.setObjectInfo(Paths.get(path + "/" + dataResourceId + extension).toString());
        content.setContentName(nameFile);

        this.dataResourceDao.update(dataResource, principal().getUserLoginId());
        this.contentDao.update(content, principal().getUserLoginId());


        return workEffortContentDao.getWorkEffortContentEx(dataResourceId);
    }

    public boolean deleteFile(DataResource dataResource) {
        Path path = Paths.get(this.configuration.getOfbizPath() + "/runtime/uploads/attachments/");

        try {
            Files.delete(Paths.get(dataResource.getObjectInfo()));
            return true;
        } catch (NoSuchFileException x) {
            LOG.error("%s: no such" + " file or directory%n", path);
        } catch (IOException x) {
            LOG.error(x.toString());
        }
        return false;
    }
}
