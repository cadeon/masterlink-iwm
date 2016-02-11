
package org.mlink.agent;

import java.util.*;


import org.mlink.agent.model.*;
import org.mlink.agent.dao.*;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class PlannerTest extends MlinkTestCase {
	
	private Planner planner;
    public PlannerTest(String name) {
        super(name);
    }

    public void testCreatingDataset()  throws Exception{
      AgentDAO dao = AgentDAO.getInstance();
      Iterator it =  dao.hql("from TaskView  as taskview where object_id between 1127 and 1130").iterator();
      while (it.hasNext()) {  
        TaskView t = (TaskView) it.next();
        toXml(t,""+t.getId(),"../testbed/datasets/planner");
      }

       Planner planner = new Planner(this.getName());
       //       planner.run()
    }
    public static Test suite() {
        return new TestSuite(PlannerTest.class);
    }
}
