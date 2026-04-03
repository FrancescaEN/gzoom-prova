package it.mapsgroup.gzoom.rest;

import it.mapsgroup.gzoom.common.Exec;
import it.mapsgroup.gzoom.mybatis.dto.NoteData;
import it.mapsgroup.gzoom.service.NoteDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * @author Leonardo Minaudo.
 */
@RestController
@RequestMapping(value = "note-data", produces = { MediaType.APPLICATION_JSON_VALUE })
public class NoteDataController {
    private final NoteDataService noteDataService;

    @Autowired
    public NoteDataController(NoteDataService noteDataService) {
        this.noteDataService = noteDataService;
    }

    @RequestMapping(value = "pref-value/{prefValue}", method = RequestMethod.GET)
    @ResponseBody
    public NoteData getNoteInfo(@PathVariable(value = "prefValue") String prefValue) {
        return Exec.exec("user-pref-value", () -> noteDataService.getNoteDataByPrefValue(prefValue));
    }

}

