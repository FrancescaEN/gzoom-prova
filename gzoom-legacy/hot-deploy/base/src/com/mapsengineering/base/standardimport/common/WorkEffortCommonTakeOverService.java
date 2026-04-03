package com.mapsengineering.base.standardimport.common;

import org.ofbiz.base.util.Debug;
import org.ofbiz.base.util.UtilValidate;

import com.mapsengineering.base.standardimport.util.WeRootInterfaceContext;


public abstract class WorkEffortCommonTakeOverService extends TakeOverService {

	public static final String MODULE = WorkEffortCommonTakeOverService.class.getName();
	
	private WeRootInterfaceContext weRootInterfaceContext = null;
    private String organizationId;
    private String workEffortRootId;
    private String workEffortRootTypeId;
    
    protected String getOrganizationId() {
		return organizationId;
	}

	protected void setOrganizationId(String organizationId) {
		this.organizationId = organizationId;
	}

	protected String getWorkEffortRootId() {
		return workEffortRootId;
	}

	protected void setWorkEffortRootId(String workEffortRootId) {
		this.workEffortRootId = workEffortRootId;
	}
	
	protected String getWorkEffortRootTypeId() {
        return workEffortRootTypeId;
    }

    protected void setWorkEffortRootTypeId(String workEffortRootTypeId) {
        this.workEffortRootTypeId = workEffortRootTypeId;
    }
    
    protected WeRootInterfaceContext getWeRootInterfaceContext() {
		return (this.weRootInterfaceContext != null) ? this.weRootInterfaceContext : WeRootInterfaceContext.get(getManager());
	}
	
	protected void getContextData() {
    	setOrganizationId((String) getManager().getContext().get(E.defaultOrganizationPartyId.name()));
    	
        WeRootInterfaceContext weRootInterfaceContext = getWeRootInterfaceContext();
        if (UtilValidate.isNotEmpty(weRootInterfaceContext)) {
            setWorkEffortRootTypeId(weRootInterfaceContext.getWorkEffortRootTypeId());
            setWorkEffortRootId(weRootInterfaceContext.getWorkEffortRootId());
            setWithWeRootInterfaceContext(weRootInterfaceContext);
        }
        
        setOtherContextData();
    }

	protected void setWithWeRootInterfaceContext(WeRootInterfaceContext weRootInterfaceContext) {
		// subclasses override
	}

	protected void setOtherContextData() {
		// subclasses override	
	}

}
