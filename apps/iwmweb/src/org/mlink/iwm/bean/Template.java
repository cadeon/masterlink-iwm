package org.mlink.iwm.bean;

import org.mlink.iwm.util.CopyUtils;

import java.util.Map;

/**
 * User: andrei
 * Date: Oct 21, 2006
 */
public class Template extends ObjectCommon{
    private java.lang.String id;
    private ServicePlan servicePlan = new ServicePlan();
    protected String instanceCount;
    protected Long presentInventory;
    protected Long deltaInventory;
    
    public Template() {
    }

    public Template(Map map) throws Exception{
        CopyUtils.copyProperties(this,map);
    }

    public String getInstanceCount() {
        return instanceCount;
    }

    public void setInstanceCount(String instanceCount) {
        this.instanceCount = instanceCount;
    }

    public Long getPresentInventory() {
		return presentInventory;
	}

	public void setPresentInventory(Long presentInventory) {
		this.presentInventory = presentInventory;
	}

	public Long getDeltaInventory() {
		return deltaInventory;
	}

	public void setDeltaInventory(Long deltaInventory) {
		this.deltaInventory = deltaInventory;
	}

	public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ServicePlan getServicePlan() {
        return servicePlan;
    }

    public void setServicePlan(ServicePlan servicePlan) {
        this.servicePlan = servicePlan;
    }
}
