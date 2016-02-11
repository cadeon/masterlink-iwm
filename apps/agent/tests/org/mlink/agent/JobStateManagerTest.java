
package org.mlink.agent;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.mlink.agent.model.Job;
import org.mlink.agent.model.JobPrecedes;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class JobStateManagerTest extends MlinkTestCase {
	
	private JobStateManager jsm;
	public Job j1;
    public JobStateManagerTest(String name) {
        super(name);
    }

    public void setUp() throws Exception {
        super.setUp();
        jsm = new JobStateManager(this.getName());
      	j1 = (Job) getBean("Job","1");
    }

    public void tearDown() throws Exception {
        super.tearDown();
        jsm = null;
    }
    
    public void testStateMachineCreation() throws Exception {
    	jsm.reloadStateMachine();
    }
    
    public void testINS2WFP() throws Exception {
      	Job j1 = (Job) getBean("Job","INS_to_WFP");
        /** This code is now stored in the dataset
        p("Check INS->WFP - no guard");
        j1.setStatus("INS");
        Set<Job> jobSet = new HashSet<Job>();
        jobSet.add(new Job());
        j1.setIncompletePreceedingJobs(jobSet);
        p("Start state: "+j1.getStatus());
        toXml(j1, "INS_to_WFP") ;
        **/
        Collection<Job> before = new ArrayList<Job>();
        before.add(j1);
        Collection<Job> after = jsm.run(before);
        Job result_job = after.iterator().next();
        String newState = result_job.getStatus();
        //        toXml(result_job, "INS_to_WFP","tests/datasets/expected_results");
        p("End state: "+newState);
        this.assertEquals("WFP", newState);
    }

    public void testWFP2PJO() throws Exception {
        p("Check WFP->PJO - 0 preceeds");
      	Job j1 = (Job) getBean("Job","WFP_to_PJO");
        /**
        j1.setStatus("WFP");
        Set<Job> jobSet = new HashSet<Job>();
        j1.setIncompletePreceedingJobs(jobSet);
        toXml(j1, "WFP_to_PJO") ;
        **/
        p("Start state: "+j1.getStatus());
        Collection<Job> before = new ArrayList<Job>();
        before.add(j1);
        Collection<Job> after = jsm.run(before);
        Job result_job = after.iterator().next();
        String newState = result_job.getStatus();
        //        toXml(result_job, "WFP_to_PJO","tests/datasets/expected_results");
        p("End state: "+newState);
        this.assertEquals("PJO", newState);
    }

    public void testPJO2RFS_DateOK() throws Exception {
      	Job job = (Job) getBean("Job","PJO_to_RFS");
        /**
        Job job = new Job();
        p("Check PJO->RFS -- earliest and latest start dates in bounds");
        job.setStatus("PJO");
        java.sql.Date today = new java.sql.Date(System.currentTimeMillis());
        job.setEarliestStart(today);
        job.setLatestStart(today);
        toXml(job, "PJO_to_RFS") ;
        **/
        p("Start state: "+job.getStatus());
        Collection<Job> before = new ArrayList<Job>();
        before.add(job);
        Collection<Job> after = jsm.run(before);
        Job result_job = after.iterator().next();
        String newState = result_job.getStatus();
        //        toXml(result_job, "PJO_to_RFS","tests/datasets/expected_results");
        p("End state: "+newState);
        this.assertEquals("RFS", newState);
    }
    public void testPJO2DPD_DateNotOKButScheduled() throws Exception {
      	Job job = (Job) getBean("Job","PJO_to_DPD");
        /**
        Job job = new Job();
        
        p("Check PJO->DPD -- earliest and latest start dates out of bounds, but job scheduled");
        job.setStatus("PJO");
        java.sql.Date today = new java.sql.Date(System.currentTimeMillis());
        Calendar c = Calendar.getInstance();
        //earliest start to three months in future
        c.add(Calendar.MONTH, 3);
        job.setEarliestStart(new java.sql.Date(c.getTimeInMillis()));
        //latest start to 7 months in past
        c.add(Calendar.MONTH, -10);
        job.setLatestStart(new java.sql.Date(c.getTimeInMillis()));
        job.setScheduledDate(today);
        p("Start state: "+job.getStatus());
        toXml(job, "PJO_to_DPD") ;
        **/
        Collection<Job> before = new ArrayList<Job>();
        before.add(job);
        Collection<Job> after = jsm.run(before);
        Job result_job = after.iterator().next();
        String newState = result_job.getStatus();
        // toXml(result_job, "PJO_to_DPD","tests/datasets/expected_results");
        p("End state: "+newState);
        this.assertEquals("DPD", newState);
    }
    public void testPJO2PJO_DateNotOKNotScheduled() throws Exception {
      	Job job = (Job) getBean("Job","PJO_to_PJO");
        /** This code is now stored in the dataset
        Job job = new Job();
        
        p("Check PJO->EJO -- earliest and latest start dates out of bounds, and job not scheduled");
        job.setStatus("PJO");
        java.sql.Date today = new java.sql.Date(System.currentTimeMillis());
        Calendar c = Calendar.getInstance();
        //earliest start to three months in future
        c.add(Calendar.MONTH, 3);
        job.setEarliestStart(new java.sql.Date(c.getTimeInMillis()));
        //latest start to 7 months in past
        c.add(Calendar.MONTH, -10);
        job.setLatestStart(new java.sql.Date(c.getTimeInMillis()));
        p("Start state: "+job.getStatus());
        toXml(job, "PJO_to_PJO") ;
        **/
        Collection<Job> before = new ArrayList<Job>();
        before.add(job);
        Collection<Job> after = jsm.run(before);
        Job result_job = after.iterator().next();
        String newState =  result_job.getStatus();
        // toXml(result_job, "PJO_to_PJO","tests/datasets/expected_results");

        p("End state: "+newState);
        this.assertEquals("EJO", newState);
    }
    public void testPJO2EJO() throws Exception {
      	Job job = (Job) getBean("Job","PJO_to_EJO");
        /** This code is now stored in the dataset
        Job job = new Job();
        
        p("Check PJO->EJO - latest start is in past, 0 preceeds, and not scheduled or completed");
        job.setStatus("PJO");
        Calendar c = Calendar.getInstance();
        //latest start to 10 months in past
        c.add(Calendar.MONTH, -10);
        job.setLatestStart(new java.sql.Date(c.getTimeInMillis()));
        Set<Job> jobSet = new HashSet<Job>();
        job.setIncompletePreceedingJobs(jobSet);
        p("Start state: "+job.getStatus());
        toXml(job, "PJO_to_EJO") ;
        **/
        Collection<Job> before = new ArrayList<Job>();
        before.add(job);
        Collection<Job> after = jsm.run(before);
        Job result_job = after.iterator().next();
        String newState = result_job.getStatus();
        // toXml(result_job, "PJO_to_EJO","tests/datasets/expected_results");
        p("End state: "+newState);
        this.assertEquals("EJO", newState);
    }
    public void testDPD2DJO() throws Exception {
      	Job job = (Job) getBean("Job","DPD_to_DJO");
        /** This code is now stored in the dataset
        Job job = new Job();
        
        p("Check DPD->DJO - dispatched date set");
        job.setStatus("DPD");
        java.sql.Date today = new java.sql.Date(System.currentTimeMillis());
        job.setDispatchedDate(today);
        p("Start state: "+job.getStatus());
        toXml(job, "DPD_to_DJO") ;
        **/
        Collection<Job> before = new ArrayList<Job>();
        before.add(job);
        Collection<Job> after = jsm.run(before);
        Job result_job = after.iterator().next();
        //        toXml(result_job, "DPD_to_DJO","tests/datasets/expected_results");

        String newState = result_job.getStatus();
        p("End state: "+newState);
        this.assertEquals("DJO", newState);
    }
    public void testDPD2RFS() throws Exception {
      	Job job = (Job) getBean("Job","DPD_to_RFS");
        /** This code is now stored in the dataset
        Job job = new Job();
        
        p("Check DPD->RFS - earliest and latest start in bounds, not scheuled");
        job.setStatus("DPD");
        java.sql.Date today = new java.sql.Date(System.currentTimeMillis());
        job.setEarliestStart(today);
        job.setLatestStart(today);
        p("Start state: "+job.getStatus());
        toXml(job, "DPD_to_RFS") ;
        **/
        Collection<Job> before = new ArrayList<Job>();
        before.add(job);
        Collection<Job> after = jsm.run(before);
        Job result_job = after.iterator().next();
        // toXml(result_job, "DPD_to_RFS","tests/datasets/expected_results");
        String newState = result_job.getStatus();
        p("End state: "+newState);
        this.assertEquals("RFS", newState);
    }
    public void testDJO2RFS() throws Exception {
      	Job job = (Job) getBean("Job","DJO_to_RFS");
        /** This code is now stored in the dataset
        Job job = new Job();
        
        p("Check DJO->RFS - earliest and latest start in bounds, not scheuled");
        job.setStatus("DJO");
        java.sql.Date today = new java.sql.Date(System.currentTimeMillis());
        job.setEarliestStart(today);
        job.setLatestStart(today);
        p("Start state: "+job.getStatus());
        toXml(job, "DJO_to_RFS") ;
        **/
        Collection<Job> before = new ArrayList<Job>();
        before.add(job);
        Collection<Job> after = jsm.run(before);
        Job result_job = after.iterator().next();
        // toXml(result_job, "DJO_to_RFS","tests/datasets/expected_results");
        String newState = result_job.getStatus();
        p("End state: "+newState);
        this.assertEquals("RFS", newState);
    }
    public void testDJO2DUN() throws Exception {
      	Job job = (Job) getBean("Job","DJO_to_DUN");
        /** This code is now stored in the dataset    

    Job job = new Job();
        
        p("Check DJO->DUN - completed date set");
        job.setStatus("DJO");
        java.sql.Date today = new java.sql.Date(System.currentTimeMillis());
        job.setCompletedDate(today);
        p("Start state: "+job.getStatus());
        toXml(job, "DJO_to_DUN") ;
        **/
        Collection<Job> before = new ArrayList<Job>();
        before.add(job);
        Collection<Job> after = jsm.run(before);
        Job result_job = after.iterator().next();
        // toXml(result_job, "DJO_to_DUN","tests/datasets/expected_results");

        String newState = result_job.getStatus();
        p("End state: "+newState);
        this.assertEquals("DUN", newState);
    }
    public void test400Jobs() throws Exception {
        System.out.println("Small Performance Check");
        Collection<Job> before = new ArrayList<Job>();
        for (int i=0;i<800;i++){
        	Job job = new Job();
            job.setStatus("INS");
            JobPrecedes jp = new JobPrecedes();
            jp.setId(job.getId());
            jp.setComplete(0);
            jp.setIncomplete(0);
            job.setJobPrecedes(jp);
            java.sql.Date today = new java.sql.Date(System.currentTimeMillis());
            job.setEarliestStart(today);
            job.setLatestStart(today);
            job.setScheduledDate(today);
            before.add(job);
        }
        System.out.println("Start : "+ System.currentTimeMillis());
        Collection<Job> after = jsm.run(before);
        System.out.println("End : "+ System.currentTimeMillis());
        String newState = after.iterator().next().getStatus();
        System.out.println("End state: "+newState);
        this.assertTrue(true);
    }

    public static Test suite() {
        return new TestSuite(JobStateManagerTest.class);
    }
}
