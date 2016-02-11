package org.mlink.iwm.bean;

import org.mlink.iwm.util.CopyUtils;

import java.util.Map;

/**
 * User: andrei
 * Date: Dec 4, 2006
 */
public class UserRole {
    private String isAssigned;
    private String userId;
    private String roleId;
    private String desc;

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getIsAssigned() {
        return isAssigned;
    }

    public void setIsAssigned(String assigned) {
        isAssigned = assigned;
    }



    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

}
