package org.mlink.iwm.bean;

import org.mlink.iwm.lookup.SkillTypeRef;
import org.mlink.iwm.util.StringUtils;
import org.mlink.iwm.struts.form.BaseForm;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * User: andrei
 * Date: Dec 5, 2006
 */
public class Skill  extends BaseForm implements Comparable {
    private java.lang.String skillId;
    private java.lang.String personId;
    private java.lang.String skillTypeId;
    private java.lang.String skillLevelId;

    public String getSkillId() {
        return skillId;
    }

    public void setSkillId(String skillId) {
        this.skillId = skillId;
    }

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public String getSkillTypeId() {
        return skillTypeId;
    }

    public String getSkillType() {
        return SkillTypeRef.getLabel(StringUtils.getInteger(skillTypeId));
    }

    public void setSkillTypeId(String skillTypeId) {
        this.skillTypeId = skillTypeId;
    }

    public String getSkillLevelId() {
        return skillLevelId;
    }


    public void setSkillLevelId(String skillLevelId) {
        this.skillLevelId = skillLevelId;
    }

    public int compareTo(Object o) {
        return getSkillType().compareTo(((Skill)o).getSkillType());
    }

    public void reset(ActionMapping mapping, HttpServletRequest request) {
        super.reset(mapping, request);
        skillId=null;
        personId=null;
        skillTypeId=null;
        skillLevelId=null;
    }
}

