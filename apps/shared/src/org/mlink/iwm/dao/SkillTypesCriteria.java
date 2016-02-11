package org.mlink.iwm.dao;

import java.util.Map;

import org.mlink.iwm.util.CopyUtils;

/**
 * User: andrei
 * Date: Nov 3, 2006
 */
public class SkillTypesCriteria extends SearchCriteria{
	public static enum Types {None, Workers, Jobs, Templates}
	
	String type;
    String isActive;

    public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String active) {
        isActive = active;
    }

    public SkillTypesCriteria() {
    }

    public SkillTypesCriteria(Map criteria) throws Exception{
        CopyUtils.copyProperties(this,criteria);
    }
}
