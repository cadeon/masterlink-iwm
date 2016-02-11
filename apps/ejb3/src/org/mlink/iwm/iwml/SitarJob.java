package org.mlink.iwm.iwml;

import org.jboss.util.property.PropertyMap;
import org.mlink.iwm.lookup.PriorityRef;
import org.mlink.iwm.lookup.TaskTypeRef;

/**
 * User: andreipovodyrev
 * Date: Nov 21, 2007
 */
public class SitarJob {
    private static PropertyMap workCtrToSkillMap;

    private String description;
    private String locator;
    private String organization;
    private String workUnitCode;
    private String priority = PriorityRef.MEDIUM;
    //private String skillType;
    private String skillLevel = "Journeyman";
    private int numberOfWorkers = 1;
    private long estimatedTime   = 60;
    private String type = TaskTypeRef.ROUTINE_MAINT;
    private String jcn;
    private String comment;


    private void init(){
        workCtrToSkillMap = new PropertyMap();
        workCtrToSkillMap.setProperty("020 Maintenance Control","AZ");
        workCtrToSkillMap.setProperty("030 Maintenance Admin","AZ");
        workCtrToSkillMap.setProperty("040 Quality Assurance","AZ");
        workCtrToSkillMap.setProperty("050 Material Control","SK");
        workCtrToSkillMap.setProperty("05D Tool Room","SK");
        workCtrToSkillMap.setProperty("110 Power Plants","AD");
        workCtrToSkillMap.setProperty("120 Airframes","AM");
        workCtrToSkillMap.setProperty("12C Corrosion","AM");
        workCtrToSkillMap.setProperty("13A ALSS PR","PR");
        workCtrToSkillMap.setProperty("13B ALSS AME","AME");
        workCtrToSkillMap.setProperty("210 Avionics","AT");
        workCtrToSkillMap.setProperty("220 Electrical","AE");
        workCtrToSkillMap.setProperty("230 Ordnance","AO");
        workCtrToSkillMap.setProperty("310 Line","AD");
    }


    public String getLocator() {
        return locator;
    }

    public void setLocator(String locator) {
        this.locator = locator;
    }

    public String getOrganization() {
        return organization==null?"":organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getWorkUnitCode() {
        return workUnitCode;
    }

    public void setWorkUnitCode(String workUnitCode) {
        this.workUnitCode = workUnitCode;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getSkillType() throws Exception{
        if(workCtrToSkillMap==null) init();
        String skillType =  workCtrToSkillMap.getProperty(getOrganization());
        if(skillType==null){
            throw new Exception("Could not find default skill type for workcenter " + getOrganization());
        }
        return skillType;
    }

    /*public void setSkillType(String skillType) {
        this.skillType = skillType;
    }*/

    public String getSkillLevel() {
        return skillLevel;
    }

    public void setSkillLevel(String skillLevel) {
        this.skillLevel = skillLevel;
    }

    public int getNumberOfWorkers() {
        return numberOfWorkers;
    }

    public void setNumberOfWorkers(int numberOfWorkers) {
        this.numberOfWorkers = numberOfWorkers;
    }

    public long getEstimatedTime() {
        return estimatedTime;
    }

    public void setEstimatedTime(int estimatedTime) {
        this.estimatedTime = estimatedTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getJcn() {
        return jcn;
    }

    public void setJcn(String jcn) {
        this.jcn = jcn;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
