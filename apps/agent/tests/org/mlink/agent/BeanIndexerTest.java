package org.mlink.agent;

import org.mlink.agent.model.*;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class BeanIndexerTest extends MlinkTestCase {
	
	private BeanIndexer bdf;
	
    public BeanIndexerTest(String name) {
        super(name);
    }

    public void setUp() throws Exception {
        super.setUp();
        bdf = new BeanIndexer("bean_test");
    }

    public void tearDown() throws Exception {
        super.tearDown();
        bdf = null;
    }
    
   
    public void testBuildDocumentFromBean() throws Exception {
      	Job j1 = (Job) getBean("Job","INS_to_WFP");         
        bdf.add(j1);
    }

    public static Test suite() {
        return new TestSuite(BeanIndexerTest.class);
    }
}
