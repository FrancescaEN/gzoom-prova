package it.mapsgroup.gzoom.mybatis.service;

import java.util.HashMap;

import it.mapsgroup.gzoom.mybatis.util.ContextPermissionPrefixEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class FilterService {

	private final PermissionService permissionService;
	
	@Autowired
	public FilterService(PermissionService permissionService) {
		this.permissionService = permissionService;
	}
	
	public HashMap<String, Object> setMapFilter(String userLoginId, ContextPermissionPrefixEnum context) {
		HashMap<String, Object> param = new HashMap<>();
		param.put("isFullAdmin", permissionService.isFullAdmin(userLoginId, context));
		param.put("isResp", permissionService.isResp(userLoginId, context));
		param.put("isView", permissionService.isView(userLoginId, context));
		param.put("isOrgMgr", permissionService.isOrgMgr(userLoginId, context));
		param.put("isRole", permissionService.isRole(userLoginId, context));
		param.put("isSup", permissionService.isSup(userLoginId, context));
		param.put("isTop", permissionService.isTop(userLoginId, context));
		param.put("userLoginId", userLoginId);
		
		return param;
	}
	
}
