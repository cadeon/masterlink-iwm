/*-----------------------------------------------------------------------------------
	File: ObjectEntity.java
	Package: org.mlink.iwm.entity3
---------------------------------------------------------------------------------------*/

package org.mlink.iwm.entity3;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.ejb.CreateException;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.Hibernate;
import org.mlink.iwm.access3.ServiceLocator;
import org.mlink.iwm.session.ImplementationSF;
import org.mlink.iwm.session.PolicySF;
import org.mlink.iwm.util.Constants;

@Entity
@Table(name="OBJECT")
@SequenceGenerator(name="OBJECT_SEQ_GEN", sequenceName="OBJECT_SEQ",allocationSize = 1)
public class ObjectEntity implements Serializable,BaseEntity  {
	private long id;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "OBJECT_SEQ_GEN")
    @Column(name="ID",nullable = false)
    public long getId(){  return id; }
    public void setId(long value){ this.id = value; }

    public ObjectEntity(){}
    public ObjectEntity(long id){this.id = id;}
    
	private Integer runHours;
	private Long locatorId;
	private String tag;
	private String objectRef;
	private Integer objectTypeId;
	private Long classId;
	private Integer active;
	private Date archivedDate;
	private Date startDate;
	private Date createdDate;
	private Integer custom;
	private Integer hasCustomData;
	private Integer hasCustomTask;
	private Integer hasCustomTaskGroup;
	private Collection<TaskGroup> taskGroups;
	private List<Task> tasks;
	private ObjectDefinition objectDefinition;
	private Collection<ObjectData> datums;
    private Long organizationId;
    private Long parentObjectId;
    
    @Column(name="RUN_HOURS")
    public Integer getRunHours() {
		return runHours;
	}
	public void setRunHours(Integer runHours) {
		this.runHours = runHours;
	}
	
	@Column(name="LOCATOR_ID")
    public Long getLocatorId() {
		return locatorId;
	}
	public void setLocatorId(Long locatorId) {
		this.locatorId = locatorId;
	}
	
	@Column(name="TAG")
    public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}
	
	@Column(name="OBJECT_REF")
    public String getObjectRef() {
		return objectRef;
	}
	public void setObjectRef(String objectRef) {
		this.objectRef = objectRef;
	}
	
	@Column(name="OBJECT_TYPE_ID")
    public Integer getObjectTypeId() {
		return objectTypeId;
	}
	public void setObjectTypeId(Integer objectTypeId) {
		this.objectTypeId = objectTypeId;
	}
	
	@Column(name="CLASS_ID")
    public Long getClassId() {
		return classId;
	}
	public void setClassId(Long classId) {
		this.classId = classId;
	}
	
	@Column(name="ACTIVE")
    public Integer getActive() {
		return active;
	}
	public void setActive(Integer active) {
		this.active = active;
	}
	
	@Column(name="ARCHIVED_DATE")
	@Temporal(TemporalType.TIMESTAMP)
    public Date getArchivedDate() {
		return archivedDate;
	}
	public void setArchivedDate(Date archivedDate) {
		this.archivedDate = archivedDate;
	}
	
	@Column(name="START_DATE")
	@Temporal(TemporalType.TIMESTAMP)
    public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	
	@Column(name="CREATED_DATE")
	@Temporal(TemporalType.TIMESTAMP)
    public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	
	@Column(name="CUSTOM")
	public Integer getCustom() {
		return custom;
	}
	public void setCustom(Integer custom) {
		this.custom = custom;
	}
	
	@Column(name="HAS_CUSTOM_DATA")
    public Integer getHasCustomData() {
		return hasCustomData;
	}
	public void setHasCustomData(Integer hasCustomData) {
		this.hasCustomData = hasCustomData;
	}

	@Column(name="HAS_CUSTOM_TASK")
	public Integer getHasCustomTask() {
		return hasCustomTask;
	}
	public void setHasCustomTask(Integer hasCustomTask) {
		this.hasCustomTask = hasCustomTask;
	}
	
	@Column(name="HAS_CUSTOM_TASK_GROUP")
	public Integer getHasCustomTaskGroup() {
		return hasCustomTaskGroup;
	}
	public void setHasCustomTaskGroup(Integer hasCustomTaskGroup) {
		this.hasCustomTaskGroup = hasCustomTaskGroup;
	}
	
	@Column(name="ORGANIZATION_ID")
	public Long getOrganizationId() {
		return organizationId;
	}
	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}
    
	@Column(name="PARENT_OBJECT_ID")
	public Long getParentObjectId() {
		return parentObjectId;
	}
	public void setParentObjectId(Long parentObjectId) {
		this.parentObjectId = parentObjectId;
	}
	
	@OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, mappedBy="object")
    public Collection<TaskGroup> getTaskGroups() {
		return taskGroups;
	}
	public void setTaskGroups(Collection<TaskGroup> taskGroups) {
		this.taskGroups = taskGroups;
	}
	
	@OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, mappedBy="object")
	@OrderBy("id")
    public List<Task> getTasks() {
		return tasks;
	}
	public void setTasks(List<Task> tasks) {
		this.tasks = tasks;
	}
	
	@ManyToOne()
	@JoinColumn(name = "OBJECT_DEF_ID")
    public ObjectDefinition getObjectDefinition() {
		return objectDefinition;
	}
	public void setObjectDefinition(ObjectDefinition objectDefinition) {
		this.objectDefinition = objectDefinition;
	}
	
	@OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, mappedBy="object")
    public Collection<ObjectData> getDatums() {
		return datums;
	}
	public void setDatums(Collection<ObjectData> datums) {
		this.datums = datums;
	}
	
	//KEEPMESTART
    //TODO:
    //void updateCustom();

    /**
     * Creates ObjectData off ObjectDataDefinition and adds to current ObjectEntity
     * @param odd          ObjectDataDefinition
     * @throws CreateException
     */
    //void inherit(ObjectDataDefinition odd) throws CreateException;

    /**
     * Creates Task off TaskDefinition and adds to current ObjectEntity
     * @param td
     * @throws CreateException
     */
    //void inherit(TaskDefinition td) throws CreateException;

    /**
     * Creates TaskGroup off TaskGroupDefinition and adds to current ObjectEntity
     * @param tgd
     * @throws CreateException
     */
    //void inherit(TaskGroupDefinition tgd) throws CreateException;

    /**
     * Update status of object tasks
     * @param status
     */
    //void updateTasksStatus(Integer status);
//KEEPMEEND

    public void updateCustom(){
    	ImplementationSF isf = ServiceLocator.getImplementationSFLocal();
        setHasCustomData(Constants.CUSTOMIZED_NO);
        for (Object o : isf.getDatums(this)) {
            ObjectData data = (ObjectData) o;
            if (Constants.CUSTOMIZED_YES.equals(data.getCustom())) {
                setHasCustomData(Constants.CUSTOMIZED_YES);
                break;
            }
        }

        //loop through tasks again to set hasCustomTask flag
        setHasCustomTask(Constants.CUSTOMIZED_NO);
        for (Object o1 : isf.getTasks(this)) {
            Task task = (Task) o1;
            if (Constants.CUSTOMIZED_YES.equals(task.getCustom())) {
                setHasCustomTask(Constants.CUSTOMIZED_YES);
                break;
            }
        }

        //loop through tasks groups  to set hasCustomTaskGroup flag
        setHasCustomTaskGroup(Constants.CUSTOMIZED_NO);
        for (Iterator iterator = isf.getTaskGroups(this).iterator(); iterator.hasNext();) {
            TaskGroup taskGroup = (TaskGroup) iterator.next();
            if(Constants.CUSTOMIZED_YES.equals(taskGroup.getCustom())){
                setHasCustomTaskGroup(Constants.CUSTOMIZED_YES);
                break;
            }
        }
    }
    
    /**
     * Creates ObjectData off ObjectDataDefinition and adds to current ObjectEntity
     * @param odd          ObjectDataDefinition
     * @throws CreateException
     */
    public void inherit(ObjectDataDefinition odd){
    	ImplementationSF isf = ServiceLocator.getImplementationSFLocal();
        ObjectData oData = new ObjectData(odd);
        oData.setObject(this);
    	oData.setCustom(Constants.CUSTOMIZED_NO);
    	isf.create(oData);
    }

    
    /////
    /**
     * Creates TaskGroup off TaskGroupDefinition and adds to current ObjectEntity
     * @param tgd
     * @throws CreateException
     */
    public void inherit(TaskGroupDefinition tgd) {
    	ImplementationSF isf = ServiceLocator.getImplementationSFLocal();
    	TaskGroup group = new TaskGroup(tgd);
    	group.setObject(this);
    	isf.create(group);
    }

    /**
     * Creates Task off TaskDefinition and adds to current ObjectEntity
     * @param td
     * @throws CreateException
     */
    public void inherit(TaskDefinition td) {
    	ImplementationSF isf = ServiceLocator.getImplementationSFLocal();
    	PolicySF psf = ServiceLocator.getPolicySFLocal();
    	Task task = new Task(td);
    	task.setObject(this);
    	//Tasks get the object's org
    	task.setOrganizationId(this.getOrganizationId());
    	List<Task> tasks = this.getTasks();
    	if(tasks == null){
        	tasks = new ArrayList<Task>();
        	this.setTasks(tasks);
        }
        tasks.add(task);
    	//isf.getTasks(this).add(task);
        psf.synchronizeGroupMembership(task, isf.getTaskGroups(this));
        isf.create(task);
        isf.update(this);
    }

    /**
     * Update status of object tasks
     * @param status
     */
    public void updateTasksStatus(Integer status){
    	ImplementationSF isf = ServiceLocator.getImplementationSFLocal();
    	PolicySF psf = ServiceLocator.getPolicySFLocal();
    	if(status==null) status = Constants.STATUS_NOT_ACTIVE;
        Collection<Task> tasks = isf.getTasks(this);
        for (Object task1 : tasks) {
            Task task = (Task) task1;
            psf.processStatusUpdate(task, status);
        }
    }
    
    /**
     * Reduces the threshold on all meter-based tasks by diff
     * Useful for meter replacements 
     * @param diff
     */
	public void updateTasksThresh(int diff) {
		
		/*Basically, we're going to be reducing the thresholds for all the tasks
		by as much as the reduction in run hours due to the meter reading change.
		Example!
		old meter = 2200
		new meter = 400
		
		old - new = 1800, so we need to reduce all the thresholds similarly
		
		Task that was due at 3000 (800 clicks away) now needs to be due at 1200 
		(800 clicks away, on the new meter)
		
		NOTE/TODO: This doesn't account for time lost in-between readings. 
		
		Say the old meter stopped working at 2200, and someone replaced the meter with a new one
		which started at 0. The system was unaware, until now, when the new meter was read.
		Well during the time away, 400 clicks went by, so those tasks due by meter are actually
		going to be *late* by 400 clicks. We can't automatically handle this because we can't assume
		a new meter starts at 0. The ONLY way around it is to replace the meter *USING AN IWM JOB*
		*WITH TWO METER READING ACTIONS.* 
		
		The first meter reading action would record the old meter, the second recording the new meter.
		Since these two changes would be processed separately, the math works out.
		
		Note that name and verb must be record and meter, respectively, for that logic to work, 
		so the actions should be something like "Record Meter Reading (old)" and Record Meter Reading (new)"
		
		*/		
		
		List<Task> tasks = this.getTasks();
		for(Task task : tasks){
			if (task.getRunHoursThreshInc() != null){
				//It has a thresh inc, means it's a meter task. Do the update.
				Double oldThresh = task.getRunHoursThresh();
				//Sometimes, some tasks have an inc but no thresh. Not sure why, but this line fixes those broken tasks (and npes)
				if (oldThresh==null) oldThresh=task.getRunHoursThreshInc();
				task.setRunHoursThresh(oldThresh - diff);
			}
		}
	}
}