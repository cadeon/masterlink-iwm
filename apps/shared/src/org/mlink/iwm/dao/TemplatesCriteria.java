package org.mlink.iwm.dao;

import org.mlink.iwm.util.CopyUtils;

import java.util.Map;
import java.util.List;
import java.util.ArrayList;

/**
 * User: andrei
 * Date: Oct 19, 2006
 */
public class TemplatesCriteria extends SearchCriteria {
    Long classId;

    public TemplatesCriteria(Map criteria) throws Exception{
        CopyUtils.copyProperties(this,criteria);
    }

    public TemplatesCriteria() throws Exception{
    }


    public Long getClassId() {
        return classId;
    }

    public void setClassId(Long classId) {
        this.classId = classId;
    }
}
