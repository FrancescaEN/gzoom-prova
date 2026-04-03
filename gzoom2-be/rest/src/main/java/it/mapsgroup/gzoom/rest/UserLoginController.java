package it.mapsgroup.gzoom.rest;

import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;

import it.mapsgroup.gzoom.model.Result;
import it.mapsgroup.gzoom.mybatis.dto.UserLogin;
import it.mapsgroup.gzoom.service.UserPreferenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import it.mapsgroup.gzoom.common.Exec;
import it.mapsgroup.gzoom.service.UserLoginService;

@RestController
@RequestMapping(value = "", produces = { MediaType.APPLICATION_JSON_VALUE })
public class UserLoginController {
	
	private final UserLoginService userLoginService;

	@Autowired
    public UserLoginController(UserLoginService userLoginService, UserPreferenceService userPreferenceService) {
	    this.userLoginService = userLoginService;
    }


    @RequestMapping(value = "user-login", method = RequestMethod.GET)
    @ResponseBody
    public UserLogin getUserLogin() {
        return Exec.exec("user-login", () -> userLoginService.getUserLogin());
    }

    @GetMapping("user-login/list")
    @ResponseBody
    public Result<UserLogin> findAllOrderByPrimaryKey(){
        return Exec.exec("get user-login list", this.userLoginService::findAllOrderByPrimaryKey);
    }

	@RequestMapping(value = "change-password", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> changePassword(@RequestBody Map<String, Object> req, HttpServletRequest request) {
        return Exec.exec("change-password post", () -> userLoginService.changePassword(req, request));
    }

    @RequestMapping(value = "change-language", method = RequestMethod.POST)
    @ResponseBody
    public boolean changeLanguage(@RequestBody Map<String, String> req, HttpServletRequest request) {
	    return Exec.exec("change-languages", () -> userLoginService.changeLang(req, request));
    }
}
