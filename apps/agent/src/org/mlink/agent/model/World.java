package org.mlink.agent.model;

import java.util.Date;
import java.util.HashMap;

import org.mlink.agent.WorldInfo;
import org.mlink.iwm.util.DateUtil;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;


/**
 *        @hibernate.class
 *         table="WORLD"
 *     
 */
public class World implements WorldInfo {
		
	private Long    id;
	private Integer isProduction;
	private Date    lastSimulationRun;
	private String  name;
	private String  parent;
	private String  schema;
	private Integer simStatus;
	private Logger  logger;
	
	public World() {
		super();
		this.logger = Logger.getLogger(World.class);
	}
	
	public World(String schema, String name) {
		super();
		this.schema = schema;
		this.name   = name;
		this.logger = Logger.getLogger(name);
	}
	
	/**
	 * Test method.
	 */
	public static World createWorld(String schema, String name) {
		return new World(schema,name);
	}
	

	//*******************************************************
    /**
     * 
     * @hibernate.id
     *  generator-class="sequence"    
     *  column="ID"
     *  @hibernate.generator-param
     *  name="sequence"
     *  value="world_seq"
     */	
	public Long getId() {return id;}
	
	/**
	 * @hibernate.property
	 * column="IS_PRODUCTION"
	 */
	public Integer getIsProduction() {return isProduction;}
	public boolean isProduction() {return isProduction.intValue()==1;}
	
	/**
	 * @hibernate.property
	 */
	public String  getName()    {return name;}
    /**
     * @hibernate.property
     */
	public String  getSchema()  {return schema;}
	/** 
	 * 
	 */
	public Integer getSimStatus() {return simStatus;}
	public String getDisplaySimStatus() {return (simStatus==null?"no sim running":simStatus.toString());}
	/** 
	 */
	public Date getLastSimulationRun() {return lastSimulationRun;}
	public String getDisplayLastSimulationRun(){return (lastSimulationRun==null?"":DateUtil.displayShortDate(lastSimulationRun));}
	/** 
	 * @hibernate.property
	 */
	public String getParent() {return parent;}
	

	public void setName(String s)   {name=s;logger=Logger.getLogger(name);}
	public void setLastSimulationRun(Date d) {lastSimulationRun=d;}
	
	public void setId(Long l)      {id=l;}
	public void setIsProduction(Integer i) {isProduction=i;}
	public void setSchema(String s){schema=s;}
	public void setParent(String s){parent=s;}	
	public void setSimStatus(Integer i){simStatus=i;}

	public String toString() {
		return this.name +"; Schema "+ this.schema +"; Parent "+ this.parent;
	}
	public boolean equals (Object obj) {
		if (this==obj) return true;
		if (null==obj || this.getClass()!=obj.getClass()) return false;
		World x= (World)obj;
		return (this.name.equals(x.name) &&
				this.schema.equals(x.schema));
	}
	public int hashCode() {
		int hash = 7;
		hash = 31 * hash + id.hashCode();
		hash = 31 * hash + name.hashCode();
		hash = 31 * hash + schema.hashCode();
		return hash;
	}
	
	// Logging helper methods
	public void log (Object o) {
		logger.debug(o);
	}
}
