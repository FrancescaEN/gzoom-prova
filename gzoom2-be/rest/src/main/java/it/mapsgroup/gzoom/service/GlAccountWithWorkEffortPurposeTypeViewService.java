package it.mapsgroup.gzoom.service;

import it.mapsgroup.gzoom.commons.Filter;
import it.mapsgroup.gzoom.model.Result;
import it.mapsgroup.gzoom.mybatis.dao.GlAccountWithWorkEffortPurposeTypeViewDao;
import it.mapsgroup.gzoom.mybatis.dto.GlAccountWithWorkEffortPurposeTypeView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Leonardo Minaudo
 */
@Service
public class GlAccountWithWorkEffortPurposeTypeViewService {
    private final GlAccountWithWorkEffortPurposeTypeViewDao glAccountWithWorkEffortPurposeTypeViewDao;

    @Autowired
    public GlAccountWithWorkEffortPurposeTypeViewService(GlAccountWithWorkEffortPurposeTypeViewDao workEffortViewDao) {
        this.glAccountWithWorkEffortPurposeTypeViewDao = workEffortViewDao;
    }

    public Result<GlAccountWithWorkEffortPurposeTypeView> getGlAccountWithWorkEffortPurposeTypeView(String organizationId) {
        List<GlAccountWithWorkEffortPurposeTypeView> list = this.glAccountWithWorkEffortPurposeTypeViewDao.getGlAccountWithWorkEffortPurposeTypeView(organizationId);
        return new Result<>(list, list.size());
    }

    public Result<GlAccountWithWorkEffortPurposeTypeView> getGlAccountWithWorkEffortPurposeTypeViewFilter(Filter filter) {
        List<GlAccountWithWorkEffortPurposeTypeView> list = this.glAccountWithWorkEffortPurposeTypeViewDao.getGlAccountWithWorkEffortPurposeTypeViewFilter(filter.getSecondValue(), filter.getField(), filter.getValue(), filter.isSecondaryLang());
        return new Result<>(list, list.size());
    }
}
