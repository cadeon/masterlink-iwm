package org.mlink.iwm.web.bean;

import org.mlink.iwm.access3.ServiceLocator;
import org.mlink.iwm.entity3.Action;
import org.mlink.iwm.entity3.JobAction;
import org.mlink.iwm.session.ControlSF;
import org.mlink.iwm.util.FunkyStringBuffer;
import org.mlink.iwm.util.StringUtils;


public class JobActionOp implements BaseExtOp {
	private Long id;
	private String verb;
	private String subject;
	private String modifier;
	private String fc;
	
	public JobActionOp(){}
	
	public JobActionOp(JobAction ja){
		FunkyStringBuffer fsb = new FunkyStringBuffer();
		Action a = ja.getAction();
		this.setId(ja.getId());
		this.setVerb(fsb.funkify(a.getVerb()));
		this.setSubject(fsb.funkify(a.getName()));
		this.setModifier(fsb.funkify(a.getModifier()));
		this.setFc(fsb.funkify(ja.getFieldCondition()));
	}
	
	public boolean logicEquals(JobAction jobAction){
		boolean logicEquals = true;
		if(!StringUtils.compareXMLString(jobAction.getFieldCondition(), this.getFc())){
			logicEquals = false;
		}
		return logicEquals;
	}
	
	public void update(JobAction jobAction){
		String fc = this.getFc();
		jobAction.setFieldCondition(fc!=null?(String)fc:"");
		ControlSF mCsf = ServiceLocator.getControlSFLocal( );
		mCsf.updateJobAction(jobAction);
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public void setVerb(String verb) {
		this.verb = verb;
	}
	public String getVerb() {
		return verb;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getSubject() {
		return subject;
	}
	public void setModifier(String modifier) {
		this.modifier = modifier;
	}
	public String getModifier() {
		return modifier;
	}
	public void setFc(String fc) {
		this.fc = fc;
	}
	public String getFc() {
		return fc;
	}
	
}
