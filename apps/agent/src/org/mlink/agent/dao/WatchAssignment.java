package org.mlink.agent.dao;

public class WatchAssignment implements java.io.Serializable {

  private String taskId;
  private String personId;

  public WatchAssignment() {
  }

  public WatchAssignment(String t, String p) {
    taskId = t;
    personId = p;
  }

  public  String getTaskId() {
    return  taskId;
  }

  public void setTaskId( String taskId ) {
       this.taskId = taskId;
  }

  public  String getPersonId() {
    return  personId;
  }

  public void setPersonId( String personId ) {
       this.personId = personId;
  }


 
}
