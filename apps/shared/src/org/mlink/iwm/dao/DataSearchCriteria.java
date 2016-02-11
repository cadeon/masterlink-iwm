package org.mlink.iwm.dao;

import org.mlink.iwm.util.CopyUtils;

import java.util.Map;

/**
 * User: andrei
 * Date: Oct 31, 2006
 */
public class DataSearchCriteria extends SearchCriteria{
    Integer dataTypeId;
    boolean isDisplayOnly = false;

    public DataSearchCriteria(Map criteria) throws Exception{
        CopyUtils.copyProperties(this,criteria);
    }
    public Integer getDataTypeId() {
        return dataTypeId;
    }

    public void setDataTypeId(Integer dataTypeId) {
        this.dataTypeId = dataTypeId;
    }

    public boolean getIsDisplayOnly() {
        return isDisplayOnly;
    }

    public void setIsDisplayOnly(boolean displayOnly) {
        isDisplayOnly = displayOnly;
    }

}
