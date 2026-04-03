package it.mapsgroup.gzoom.rest;

import it.mapsgroup.gzoom.common.Exec;
import it.mapsgroup.gzoom.mybatis.dto.UserPreference;
import it.mapsgroup.gzoom.service.UserLoginService;
import it.mapsgroup.gzoom.service.UserPreferenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "", produces = { MediaType.APPLICATION_JSON_VALUE })
public class UserPreferenceController {

	private final UserLoginService userLoginService;
    private final UserPreferenceService userPreferenceService;

	@Autowired
    public UserPreferenceController(UserLoginService userLoginService, UserPreferenceService userPreferenceService) {
	    this.userLoginService = userLoginService;
	    this. userPreferenceService = userPreferenceService;
    }
	

	@RequestMapping(value = "/user-preference-na/{typeId}", method = RequestMethod.GET)
    @ResponseBody
    public UserPreference getUserPreferenceNA(@PathVariable(value = "typeId") String typeId) {
        return Exec.exec("/user-preference-na get", () -> userPreferenceService.getUserPreferenceNA(typeId));
    }

    @RequestMapping(value = "/user-preference/{typeId}", method = RequestMethod.GET)
    @ResponseBody
    public UserPreference getUserPreference(@PathVariable(value = "typeId") String typeId) {
        return Exec.exec("user-preference get", () -> userPreferenceService.getUserPreference(typeId));
    }

    @RequestMapping(value = "/user-preference", method = RequestMethod.PUT)
    @ResponseBody
    public Boolean updateUom(@RequestBody UserPreference userPreference) {
        return Exec.exec("user-preference", () -> userPreferenceService.updateUserPreference(userPreference));
    }

    @RequestMapping(value = "/user-preference/default-portal-page", method = RequestMethod.GET)
    @ResponseBody
    public String getDefaultPortalPage() {
        return Exec.exec("Default Portal Page", userPreferenceService::getDefaultPortalPage);
    }
}
