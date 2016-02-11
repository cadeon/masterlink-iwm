package org.mlink.iwm.dao;

import org.mlink.iwm.util.CopyUtils;
import java.util.Map;

/**
 * User: andrei
 * Date: Nov 3, 2006
 */
public class WorkersCriteria extends SearchCriteria{
    String isActive;
    Integer workerStatus;

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String active) {
        isActive = active;
    }

    public Integer getWorkerStatus() {
        return workerStatus;
    }

    public void setWorkerStatus(Integer workerStatus) {
        this.workerStatus = workerStatus;
    }

    public WorkersCriteria() {
    }

    public WorkersCriteria(Map criteria) throws Exception{
        CopyUtils.copyProperties(this,criteria);
    }
}
