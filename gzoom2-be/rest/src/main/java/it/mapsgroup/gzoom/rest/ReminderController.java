package it.mapsgroup.gzoom.rest;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import it.mapsgroup.gzoom.common.Exec;
import it.mapsgroup.gzoom.service.reminder.ReminderService;

/**
 */
@RestController
@RequestMapping(value = "", produces = { MediaType.APPLICATION_JSON_VALUE })
public class ReminderController {

    private final ReminderService reminderService;

    @Autowired
    public ReminderController(ReminderService reminderService) {
        this.reminderService = reminderService;
    }    
    
    @RequestMapping(value = "reminder-period", method = RequestMethod.GET)
    @ResponseBody
    public String reminderPeriodoScheduled() {
        return Exec.exec("reminder-period", () ->  reminderService.reminderPeriodoScheduled());
    }
        
    
    @RequestMapping(value = "reminder-expiry", method = RequestMethod.GET)
    @ResponseBody
    public String reminderWorkEffortExpiry() {
        return Exec.exec("report/sendmail post", () -> reminderService.reminderWorkEffortExpiry());
    }
    
}
