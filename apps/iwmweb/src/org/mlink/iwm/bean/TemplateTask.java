package org.mlink.iwm.bean;

/**
 * User: andrei
 * Date: Oct 30, 2006
 */
public class TemplateTask implements Comparable{

    private    String skillType;
    private    String skillLevel;
    private    String priority;
    private    String taskType;
    private    String actionCount;
    private    String instanceCount;


    private String id;
	private String estTime;
    private String runHoursThreshInc;
	private String taskTypeId;
	private String priorityId;
	private String workerTypeId;
	private String objectDefId;
	private String groupId;
	private String skillTypeId;
	private String skillLevelId;
	private String numberOfWorkers;
	private String freqMonths;
	private String freqDays;
	private String frequencyId;
	private String description;

	private String expiryTypeId;
	private String expiryNumOfDays;
	
    public String getSkillType() {
        return skillType;
    }

    public void setSkillType(String skillType) {
        this.skillType = skillType;
    }

    public String getSkillLevel() {
        return skillLevel;
    }

    public void setSkillLevel(String skillLevel) {
        this.skillLevel = skillLevel;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    public String getActionCount() {
        return actionCount;
    }

    public void setActionCount(String actionCount) {
        this.actionCount = actionCount;
    }

    public String getInstanceCount() {
        return instanceCount;
    }

    public void setInstanceCount(String instanceCount) {
        this.instanceCount = instanceCount;
    }

    public String getId() {
        return id;
    }

    public void setId(String taskDefId) {
        this.id = taskDefId;
    }

    public String getEstTime() {
        return estTime;
    }

    public void setEstTime(String estTime) {
        this.estTime = estTime;
    }


    public String getRunHoursThreshInc() {
        return runHoursThreshInc;
    }

    public void setRunHoursThreshInc(String runHoursThreshInc) {
        this.runHoursThreshInc = runHoursThreshInc;
    }

    public String getTaskTypeId() {
        return taskTypeId;
    }

    public void setTaskTypeId(String taskTypeId) {
        this.taskTypeId = taskTypeId;
    }

    public String getPriorityId() {
        return priorityId;
    }

    public void setPriorityId(String priorityId) {
        this.priorityId = priorityId;
    }

    public String getWorkerTypeId() {
        return workerTypeId;
    }

    public void setWorkerTypeId(String workerTypeId) {
        this.workerTypeId = workerTypeId;
    }

    public String getObjectDefId() {
        return objectDefId;
    }

    public void setObjectDefId(String objectDefId) {
        this.objectDefId = objectDefId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getSkillTypeId() {
        return skillTypeId;
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

    public String getNumberOfWorkers() {
        return numberOfWorkers;
    }

    public void setNumberOfWorkers(String numberOfWorkers) {
        this.numberOfWorkers = numberOfWorkers;
    }

    public String getFreqMonths() {
        return freqMonths;
    }

    public void setFreqMonths(String freqMonths) {
        this.freqMonths = freqMonths;
    }

    public String getFreqDays() {
        return freqDays;
    }

    public void setFreqDays(String freqDays) {
        this.freqDays = freqDays;
    }

    public String getFrequencyId() {
        return frequencyId;
    }

    public void setFrequencyId(String frequencyId) {
        this.frequencyId = frequencyId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String taskDescription) {
        this.description = taskDescription;
    }

	public String getExpiryTypeId() {
		return expiryTypeId;
	}

	public void setExpiryTypeId(String expiryTypeId) {
		this.expiryTypeId = expiryTypeId;
	}

	public String getExpiryNumOfDays() {
		return expiryNumOfDays;
	}

	public void setExpiryNumOfDays(String expiryNumOfDays) {
		this.expiryNumOfDays = expiryNumOfDays;
	}

	public int compareTo(Object o) {
		TemplateTask ott = (TemplateTask) o;
		return getId().compareTo(ott.getId());
	}
	
	public boolean equals(Object o){
		int x = compareTo(o);
		if(x==0) return true;
		else return false;
	}
}
