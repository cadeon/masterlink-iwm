package org.mlink.iwm.dao;

import org.mlink.iwm.util.CopyUtils;

import java.util.Map;
import java.util.List;
import java.util.ArrayList;

/**
 * User: andrei
 * Date: Oct 19, 2006
 */
public class LocatorsCriteria extends SearchCriteria{
    Long locatorId;

    public LocatorsCriteria(Map criteria) throws Exception{
        CopyUtils.copyProperties(this,criteria);
    }

    public Long getLocatorId() {
        return locatorId;
    }

    public void setLocatorId(Long locatorId) {
        this.locatorId = locatorId;
    }
}
