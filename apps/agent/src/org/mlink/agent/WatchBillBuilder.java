package org.mlink.agent;

import java.util.*;
import org.mlink.agent.model.*;
import org.mlink.agent.dao.*;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import java.sql.Timestamp;


public class WatchBillBuilder implements java.io.Serializable {
  AgentDAO  agent;
  private WatchBill watchBill;
	private Logger logger; 
	private String name;
	private String department;
	private String condition;
  private int sections = 3;
  private Collection people;  
  

  public WatchBillBuilder()  {
		logger = Logger.getLogger("WatchBillBuilder");
  }
 

  public WatchBillBuilder(String condition,String department, int sections ) {
    this();
    this.department=department;
    this.condition=condition;
    this.sections=sections;
    try {
      createWatchBill();
    }
    catch(Exception ex) {     
      ex.printStackTrace();
    } 
   
  }       

     /**
     * Loads all watch tasks for a condition, then sorts the ascending.
     *
     * @param condition  The text used to match the condition which is stored
     *       in the db as the object.tag .
     */
  public ArrayList loadWatchStations(String condition) throws Exception {
   agent = AgentDAO.getInstance();
   ArrayList watchList  = new ArrayList();
    Task t = null; 
    Iterator it = agent.hql("from Task as task where task.object.tag = '"+condition+"' and task.active > 0").iterator();
    while (it.hasNext()) {
      WatchStation station = new WatchStation((Task) it.next());
      watchList.add(station);  
    }        
    Collections.sort(watchList);   
    return watchList;
  }
  public Collection loadCrew(String organization) throws Exception {
    agent = AgentDAO.getInstance();
    return agent.hql("select person from Person person");//,Organization organization where person.organizationId = organization.id and organization.party.name = '"+organization+"'"); 
    //    return agent.hql("select person from Person person,Organization organization where person.organizationId = organization.id and organization.party.name = '"+organization+"'"); 
  }
 
  public  WatchBill getWatchBill() {
    return  watchBill;
  }

  public void setWatchBill( WatchBill watchBill ) {
       this.watchBill = watchBill;
  }

	protected void log (Object o) {
		logger.debug(o);
	}
  public  Collection getPeople() {
    return  people;
  }

  public void setPeople( Collection people ) {
       this.people = people;
  }

  public void createWatchBill() throws Exception {
      Iterator stations = loadWatchStations(condition).iterator();
      people = loadCrew(department);
      System.out.println("people:"+people);  
      System.out.println("people:"+people.size());  
      watchBill = new WatchBill("Test");
      int sects = sections;  
      while (stations.hasNext()) {
        WatchStation station =  (WatchStation) stations.next();
        System.out.println("sects:"+sects);  
        HashSet candidates = new HashSet(station.getCandidates());
        System.out.println("cands:"+candidates.size());  
        Person peep = null;
        // The intersect of the 2 sets is the set of available candidates
        candidates.retainAll(people);         
        System.out.println("cands:"+candidates.size());  
        // TODO: Get default sections from DB.
        if (sects == 0) sects = 3; //candidates.size();
        Iterator peeps = candidates.iterator();
        for (int i=1;i<=sects;i++) {
          peep = null;
          if (i > watchBill.getSectionCount()) watchBill.addSection(new WatchSection());
          if (peeps.hasNext()) {
            peep = (Person) peeps.next(); 
          } 
          System.out.println("peep"+i+":"+peep);      
          System.out.println("station:"+station.task);      
          watchBill.addPerson(peep,station,i);
          people.remove(peep);
          System.out.println("people:"+people);  
          System.out.println("people:"+people.size());  
        }
      }        
  } 


  public  String getDepartment() {
    return  department;
  }

  public void setDepartment( String department ) {
       this.department = department;
  }
  public  String getCondition() {
    return  condition;
  }

  public void setCondition( String condition ) {
       this.condition = condition;
  }
  public  int getSections() {
    return  sections;
  }

  public void setSections( int sections ) {
       this.sections = sections;
  }

  public String getTrigger () {
       try {createWatchBill();}
       catch (Exception ex) {ex.printStackTrace();}
       return "";
  }


}

