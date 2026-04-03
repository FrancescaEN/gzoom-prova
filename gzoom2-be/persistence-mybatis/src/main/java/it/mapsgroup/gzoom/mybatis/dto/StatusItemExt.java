package it.mapsgroup.gzoom.mybatis.dto;

public class StatusItemExt extends StatusItem {
	private WorkEffort workEffort;
	private WorkEffortType workEffortType;
	
	/**
	 * @return the workEffort
	 */
	public WorkEffort getWorkEffort() {
		return workEffort;
	}
	/**
	 * @param workEffort the workEffort to set
	 */
	public void setWorkEffort(WorkEffort workEffort) {
		this.workEffort = workEffort;
	}
	/**
	 * @return the workEffortType
	 */
	public WorkEffortType getWorkEffortType() {
		return workEffortType;
	}
	/**
	 * @param workEffortType the workEffortType to set
	 */
	public void setWorkEffortType(WorkEffortType workEffortType) {
		this.workEffortType = workEffortType;
	}
	
	
}
