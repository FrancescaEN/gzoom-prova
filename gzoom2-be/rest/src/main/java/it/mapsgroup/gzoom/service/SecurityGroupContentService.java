package it.mapsgroup.gzoom.service;

import it.mapsgroup.gzoom.model.Messages;
import it.mapsgroup.gzoom.model.Result;
import it.mapsgroup.gzoom.mybatis.dao.SecurityGroupContentDao;
import it.mapsgroup.gzoom.mybatis.dto.SecurityGroupContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class SecurityGroupContentService {
    private final SecurityGroupContentDao securityGroupContentDao;

    @Autowired
    public SecurityGroupContentService(SecurityGroupContentDao securityGroupContentDao) {
        this.securityGroupContentDao = securityGroupContentDao;
    }

    public Result<SecurityGroupContent> getSecurityGroupContentList(String groupId) {
        List<SecurityGroupContent> list = this.securityGroupContentDao.getSecurityGroupContentByGroupId(groupId);
        return new Result<>(list, list.size());
    }

    public boolean createSecurityGroupContent(SecurityGroupContent req) {
        this.validateSecurityGroupContent(req, "CREATE");
        return this.securityGroupContentDao.create(req);
    }

    public boolean updateSecurityGroupContent(SecurityGroupContent req) {
        this.validateSecurityGroupContent(req, "UPDATE");
        return this.securityGroupContentDao.update(req);
    }

    public boolean deleteSecurityGroupContent (String groupId, String contentId, Instant fromDate) {
        SecurityGroupContent securityGroupContent = new SecurityGroupContent();
        securityGroupContent.setGroupId(groupId);
        securityGroupContent.setContentId(contentId);
        securityGroupContent.setFromDate(fromDate);
        this.validateSecurityGroupContent(securityGroupContent, "DELETE");
        return this.securityGroupContentDao.delete(groupId, contentId, fromDate);
    }

    private void validateSecurityGroupContent(SecurityGroupContent req, String method) {
        Messages msg = new Messages();
        Validators.assertNotNull(req, msg.getMessageTable(Messages.SECURITY_GROUP_CONTENT, Messages.IS_REQUIRED));
        Validators.assertNotBlank(req.getGroupId(), msg.getMessageColumn(Messages.SECURITY_GROUP_CONTENT, Messages.GROUP_ID, Messages.IS_REQUIRED));
        Validators.assertNotNull(req.getContentId(), msg.getMessageColumn(Messages.SECURITY_GROUP_CONTENT, Messages.CONTENT_ID, Messages.IS_REQUIRED));
        Validators.assertNotNull(req.getFromDate(), msg.getMessageColumn(Messages.SECURITY_GROUP_CONTENT, Messages.FROM_DATE, Messages.IS_REQUIRED));

        SecurityGroupContent record = this.securityGroupContentDao.getSecurityGroupContent(req.getGroupId(), req.getContentId(), req.getFromDate());
        if ( method.equalsIgnoreCase("UPDATE") ||  method.equalsIgnoreCase("DELETE")) {
            Validators.assertFalse(record == null, msg.getMessageTable(Messages.SECURITY_GROUP_CONTENT, Messages.INVALID));
        }
        if ( method.equalsIgnoreCase("CREATE")) {
            Validators.assertFalse(record != null, msg.getMessageTable(Messages.SECURITY_GROUP_CONTENT, Messages.EXISTING));
        }

    }

}
