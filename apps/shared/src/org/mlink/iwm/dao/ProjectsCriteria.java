package org.mlink.iwm.dao;

import org.mlink.iwm.util.CopyUtils;

import java.util.Map;
import java.sql.Date;

/**
 * User: andrei
 * Date: Feb 6, 2007
 */
public class ProjectsCriteria extends SearchCriteria{

    public enum ProjectCategory{Active,History,All}
    private ProjectCategory projectCategory;

    private java.lang.String rangeStartDate;
    private java.lang.String rangeEndDate;
    private java.lang.Integer projectType;
    private java.lang.Long organization;



    public ProjectsCriteria(Map criteria) throws Exception{
        CopyUtils.copyProperties(this,criteria);
    }

    public ProjectsCriteria() {
    }

    public ProjectCategory getCategory() {
        return projectCategory;
    }

    public String getProjectCategory() {
        return projectCategory.toString();
    }

    public void setProjectCategory(String projectCategory) {
        for(ProjectCategory pc: ProjectCategory.values()){
            if(pc.toString().equals(projectCategory)){
                this.projectCategory = pc;
                break;
            }
        }

    }

    public String getRangeStartDate() {
        return rangeStartDate;
    }

    public void setRangeStartDate(String rangeStartDate) {
        this.rangeStartDate = rangeStartDate;
    }

    public String getRangeEndDate() {
        return rangeEndDate;
    }

    public void setRangeEndDate(String rangeEndDate) {
        this.rangeEndDate = rangeEndDate;
    }

    public Integer getProjectType() {
        return projectType;
    }

    public void setProjectType(Integer projectType) {
        this.projectType = projectType;
    }

    public Long getOrganization() {
        return organization;
    }

    public void setOrganization(Long organization) {
        this.organization = organization;
    }
    /*public boolean isHistory(){
        return ProjectCategory.History.toString().equals(getProjectCategory());
    }*/

}
