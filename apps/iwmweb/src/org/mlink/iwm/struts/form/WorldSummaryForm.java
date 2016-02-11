package org.mlink.iwm.struts.form;

import org.mlink.agent.model.World;
import org.mlink.iwm.util.AutoGrowingList;

import javax.servlet.ServletRequest;
import java.util.List;

import org.apache.struts.action.ActionMapping;

public class WorldSummaryForm extends BaseForm {
	protected java.util.List worlds = new AutoGrowingList(World.class);
	protected java.lang.String selectedItemId;
	protected String agent;
	protected int numActiveTasks;
	
	public String getAgent(){return agent;}
	public int    getNumActiveTasks(){return numActiveTasks;}
	public String getSelectedItemId() {return selectedItemId;}
	public List   getWorlds() {return worlds;}

	public void setAgent(String s){agent=s;}
	public void setNumActiveTasks(int i){numActiveTasks=i;}
	public void setSelectedItemId(String selectedItemId) {this.selectedItemId = selectedItemId;}
	public void setWorlds(List worlds) {this.worlds = worlds;}

	public void reset(ActionMapping actionMapping, ServletRequest servletRequest) {
		super.reset(actionMapping, servletRequest);
		worlds = new AutoGrowingList(World.class);
	}
}
