package it.mapsgroup.gzoom.service;

import it.mapsgroup.gzoom.mybatis.dao.NoteDataDao;
import it.mapsgroup.gzoom.mybatis.dto.NoteData;
import it.mapsgroup.gzoom.mybatis.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Alex Tivoli
 */
@Service
public class NoteDataService {
    private final NoteDataDao noteDataDao;
    private final PermissionService permissionService;

    @Autowired
    public NoteDataService(NoteDataDao noteDataDao, PermissionService permissionService) {
        this.noteDataDao = noteDataDao;
        this.permissionService = permissionService;
    }

    public NoteData getNoteDataByPrefValue(String prefValue) {
        NoteData noteData = this.noteDataDao.getNoteDataByPrefValue(prefValue);
        return noteData;
    }

}