package it.mapsgroup.gzoom.service;

import it.mapsgroup.gzoom.model.Result;
import it.mapsgroup.gzoom.mybatis.dao.PartyTypeDao;
import it.mapsgroup.gzoom.mybatis.dto.PartyType;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

@Service
public class PartyTypeService {

    private static final Logger LOG = getLogger(PartyTypeService.class);

    private final PartyTypeDao partyTypeDao;

    @Autowired
    public PartyTypeService(PartyTypeDao partyTypeDao) {
        this.partyTypeDao = partyTypeDao;
    }

    public Result<PartyType> getPartyTypes() {
        List<PartyType> list = partyTypeDao.getPartyTypes();
        return new Result<>(list, list.size());
    }
}
