package org.mlink.agent.dao;

import java.util.Collection;
import org.mlink.agent.model.Task;

public class WatchStation implements Comparable {
  public Task task;
  private Collection candidates  = null;
  private int candidateCount = 0;

  public WatchStation(Task t) {
    task = t;
    loadCandidates();
  }
  
  public Collection loadCandidates() {
    try {
      AgentDAO agent = AgentDAO.getInstance();
      candidates = agent.hql("select skill.person from Skill as skill where skill.skillTypeRef.id = "+task.getSkillTypeId()+" and skill.skillLevelRef.id = "+task.getSkillLevelId());
      candidateCount = candidates.size();
    }
    catch(Exception ex) {     
      ex.printStackTrace();
    }
    return candidates; 
  }


  public  Collection getCandidates() {
    return  candidates;
  }

  public void setCandidates( Collection candidates ) {
     this.candidates = candidates;
  }

  public  int getCandidateCount() {
    return  candidateCount;
  }

  public void setCandidateCount( int candidateCount ) {
     this.candidateCount = candidateCount;
  }
 
  public int compareTo(Object ws) {
    return new Integer(candidateCount).compareTo(new Integer(((WatchStation) ws).getCandidateCount()));
  }


}


