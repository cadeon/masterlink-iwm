package org.mlink.agent;

import java.util.*;
import org.mlink.agent.model.*;
import java.sql.Timestamp;

public class WatchBillAgent extends BaseAgent {

    protected WatchBillAgent(String name) {
		super(name);
	}
    
	/**
	 * Runs the JSM algorithm:
	 * 1. Examine all jobs
	 * 2. Run each job through the state transition rules
	 * 3. Repeat until the job no longer changes state
	 * 
	 * @param c The collection of jobs to examine
	 * @returns The jobs modified as a result of the run
	 */
	public Collection run(Collection c) {
		// TODO Auto-generated method stub
    Task j = null;
    String s = null;
    Iterator it = c.iterator();
    while (it.hasNext()) {
      j = (Task) it.next();
      s = j.getDescription();
            if (s!=null &&  s.length()>1) {
              // System.out.println("Id:"+j.getId()+" "+s);
              log("Id:"+j.getId()+" "+s);
            }
    } 
       
		return c;
	}
}
