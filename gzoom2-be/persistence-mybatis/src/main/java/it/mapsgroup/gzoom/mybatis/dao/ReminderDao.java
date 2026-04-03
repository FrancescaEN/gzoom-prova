package it.mapsgroup.gzoom.mybatis.dao;

import java.util.Date;
import java.util.List;

import it.mapsgroup.gzoom.mybatis.util.ContextPermissionPrefixEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.mapsgroup.gzoom.mybatis.dto.Reminder;
import it.mapsgroup.gzoom.mybatis.mapper.ReminderMapper;
import it.mapsgroup.gzoom.mybatis.service.FilterService;

@Service
public class ReminderDao {

	private final ReminderMapper reminderMapper;
	private final FilterService filterService;
	
	@Autowired
	public ReminderDao(ReminderMapper reminderMapper, FilterService filterService) {
		this.reminderMapper = reminderMapper;
		this.filterService = filterService;
	}

	public List<Reminder> selectReminderPeriod(String userLoginId, String permission, String workEffortTypeId, Date monitoringDate) {
		return reminderMapper.selectReminderPeriod(workEffortTypeId, monitoringDate, filterService.setMapFilter(userLoginId, ContextPermissionPrefixEnum.valueOf(permission)));
	}
	
	public List<Reminder> selectReminderWorkEffortExpiry(String userLoginId, String permission, String workEffortTypeId, Date monitoringDate) {
		return reminderMapper.selectReminderWorkEffortExpiry(workEffortTypeId, monitoringDate, filterService.setMapFilter(userLoginId, ContextPermissionPrefixEnum.valueOf(permission)));
	}
	
}
