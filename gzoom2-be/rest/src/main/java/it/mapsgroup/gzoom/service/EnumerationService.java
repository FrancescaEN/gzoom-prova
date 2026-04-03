package it.mapsgroup.gzoom.service;

import it.mapsgroup.gzoom.commons.Filter;
import it.mapsgroup.gzoom.model.Result;
import it.mapsgroup.gzoom.mybatis.dao.EnumerationDao;
import it.mapsgroup.gzoom.mybatis.dto.Enumeration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EnumerationService {

    private final EnumerationDao enumerationDao;
    private final Configuration config;

    @Autowired
    public EnumerationService(EnumerationDao enumerationDao, Configuration config) {
        this.enumerationDao = enumerationDao;
        this.config = config;
    }

    public Result<Enumeration> getEnumerations(String enumTypeId) {
        List<Enumeration> list = enumerationDao.getEnumerations(enumTypeId);
        return new Result<>(list, list.size());
    }

    public Result<Enumeration> getEnumerationFilter(Filter filter) {
        List<Enumeration> list = this.enumerationDao.getEnumerationFilter(filter.getSecondValue(), filter.getField(), filter.getValue(), config.getLanguageType().equals("BILING"));
        return new Result<>(list, list.size());
    }
}
