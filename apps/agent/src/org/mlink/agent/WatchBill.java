package org.mlink.agent;

import java.util.*;
import org.mlink.agent.model.*;
import org.mlink.agent.dao.*;
import java.sql.Timestamp;

public class WatchBill  implements java.io.Serializable {

  private String name;
  private Timestamp startDate;
  private Timestamp endDate;
  private ArrayList sections = new ArrayList(); 

  public WatchBill() {
    this("WatchBill"); 
  }

 
  public WatchBill(String name) {
    this.name = name;
    addSection(new WatchSection());
  }

  public  String getName() {
    return  name;
  }
  
  public void setName( String name ) {
       this.name = name;
  }
  public  Timestamp getStartDate() {
    return  startDate;
  }

  public void setStartDate( Timestamp startDate ) {
       this.startDate = startDate;
  }

  public  Timestamp getEndDate() {
    return  endDate;
  }

  public void setEndDate( Timestamp endDate ) {
       this.endDate = endDate;
  }

  public  ArrayList getSections() {
    return  sections;
  }
 
  public int getSectionCount() {
    return sections.size();
  }
  
  public void addSection(WatchSection ws) {
    sections.add(ws);
  }

  public void addPerson(Person p , WatchStation station, int section) {
    WatchSection sect = (WatchSection) sections.get(section-1);    
    sect.addStation(station.task,p); 
  }

  public String toString() {
    Iterator i = sections.iterator(); 
    int sec_no = 1; 
    String str = "";
    while (i.hasNext()) {
      str = str+ "Section "+sec_no+":" + ((WatchSection) i.next()).toString()+"\n";
      sec_no++;
    }
    return str;
  }

}
