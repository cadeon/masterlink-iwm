package org.mlink.iwm.util;

/**
 * Created by IntelliJ IDEA.
 * User: Owner
 * Date: Jul 29, 2005
 * Time: 3:51:35 PM
 * To change this template use Options | File Templates.
 */
public class JobFilterHelper {
    public static final String JOB_FILTER_HELPER="JobFilterHelper";

	protected java.lang.String skillTypeId;
	protected java.lang.String jobTypeId;
	protected java.lang.String jobStatusId;
	protected java.lang.String objectId;

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getJobStatusId() {
        return jobStatusId;
    }

    public void setJobStatusId(String jobStatusId) {
        this.jobStatusId = jobStatusId;
    }

    public String getJobTypeId() {
        return jobTypeId;
    }

    public void setJobTypeId(String jobTypeId) {
        this.jobTypeId = jobTypeId;
    }

    public String getSkillTypeId() {
        return skillTypeId;
    }

    public void setSkillTypeId(String skillTypeId) {
        this.skillTypeId = skillTypeId;
    }

    public void reset(){
        skillTypeId=null;
        jobTypeId=null;
        jobStatusId=null;
        objectId=null;
    }

}
