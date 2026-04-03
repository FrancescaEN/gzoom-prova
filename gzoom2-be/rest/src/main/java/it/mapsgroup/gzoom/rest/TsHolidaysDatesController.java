package it.mapsgroup.gzoom.rest;

import it.mapsgroup.gzoom.common.Exec;
import it.mapsgroup.gzoom.model.Result;
import it.mapsgroup.gzoom.mybatis.dto.TsHolidaysDates;
import it.mapsgroup.gzoom.service.TsHolidayDatesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "tsHolidaysDates", produces = { MediaType.APPLICATION_JSON_VALUE })
public class TsHolidaysDatesController {

    private TsHolidayDatesService tsHolidayDatesService;

    @Autowired
    public TsHolidaysDatesController(TsHolidayDatesService tsHolidayDatesService) {

        this.tsHolidayDatesService = tsHolidayDatesService;
    }

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    @ResponseBody
    public Result<TsHolidaysDates> getTsHolidayDates(@PathVariable(value = "id")  String id) {
        return Exec.exec("TsHolidayDates get", () -> tsHolidayDatesService.getTsHolidayDates(id));
    }

}
