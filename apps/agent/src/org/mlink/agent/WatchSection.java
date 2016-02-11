package org.mlink.agent;

import java.util.*;
import java.math.BigDecimal;
import org.mlink.agent.model.*;
import org.mlink.agent.dao.*;

public class WatchSection  {
  private ArrayList watches = new ArrayList();

  public void WatchSection() {      

  }
  
  public void addStation(Task task, Person person) {
    String p = (person==null ? null : person.toString());
    watches.add(new WatchAssignment(task.toString(),p));
  }

  public void removeStation(Task task) {
    //    watches.remove(task);
  }

  public Person getPerson(Task task) {
    //    return (Person) watches.get(task);
    return null;
  }
 
  public String toString() {
    return watches.toString();   
  }

  public  ArrayList getWatches() {
    return  watches;
  }

  public void setWatches( ArrayList watches ) {
       this.watches = watches;
  } 

}

