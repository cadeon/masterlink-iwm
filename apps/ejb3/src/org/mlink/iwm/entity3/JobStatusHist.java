/*-----------------------------------------------------------------------------------
	File: JobStatusHist.java
	Package: org.mlink.iwm.entity3
---------------------------------------------------------------------------------------*/

package org.mlink.iwm.entity3;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.mlink.iwm.access3.ServiceLocator;
import org.mlink.iwm.session.ControlSF;
import org.mlink.iwm.session.PolicySF;

import uk.ltd.getahead.dwr.WebContextFactory;

@Entity
@Table(name="JOB_STATUS_HIST")
@SequenceGenerator(name="JSH_SEQ_GEN", sequenceName="JSH_SEQ",allocationSize = 1)
public class JobStatusHist implements Serializable,BaseEntity  {
	
	public long id;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "JSH_SEQ_GEN")
	@Column(name="ID",nullable = false)
    public long getId(){  return id; }
	public void setId(long value){ this.id = value; }
	
	private Long job_id;
	private Integer new_status_id;
	private Date date_changed; 
	private Long user_id;
	private Long assigned_wkr_id;
	
	public JobStatusHist() {	};
	
	public JobStatusHist(Long job_id, Integer new_status_id){
		ControlSF csf = ServiceLocator.getControlSFLocal();
		PolicySF psf = ServiceLocator.getPolicySFLocal();
		//get our user
		
		String user = psf.getCallerPrincipal().getName();
			
		if (user !=null && !user.equals("super")){
			this.setUserId(psf.getUserByName(user).getPerson().getId());
		} else {
			this.setUserId(null); 
		}
		this.setJobId(job_id);
		this.setNewStatusId(new_status_id);
		this.setDate(new java.sql.Date(System.currentTimeMillis()));
		//Get assigned worker
		
		List<Person> wkrs = new ArrayList<Person>();
		try {
			wkrs = csf.getJobWorkers(this.getJobId(), (java.sql.Date) this.getDate());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (wkrs.size()>0){
			this.setAssigned(wkrs.get(0).getId());
		} else {
			this.setAssigned(null);
		}
	}
	
	
	@Column(name="JOB_ID",nullable = false)
    public Long getJobId() {
		return job_id;
	}
	public void setJobId(Long job_id) {
		this.job_id = job_id;
	}
	
	@Column(name="NEW_STATUS_ID",nullable = true)
    public Integer getNewStatusId() {
		return new_status_id;
	}
	public void setNewStatusId(Integer new_status_id) {
		this.new_status_id = new_status_id;
	}
	
	@Column(name="DATE_CHANGED",nullable = false)
    public Date getDate() {
		return date_changed;
	}
	public void setDate(Date date_changed) {
		this.date_changed = date_changed;
	}
	
	@Column(name="USER_ID",nullable = true)
    public Long getUserId() {
		return user_id;
	}
	public void setUserId(Long i) {
		this.user_id = i;
	}
	
	@Column(name="ASSIGNED_WKR_ID",nullable = true)
    public Long getAssigned() {
		return assigned_wkr_id;
	}
	public void setAssigned(Long assigned_wkr_id) {
		this.assigned_wkr_id = assigned_wkr_id;
	}
}