package org.mlink.iwm.access;

import java.util.List;
import java.util.Map;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.mlink.iwm.util.DBAccess;
import org.mlink.iwm.util.EnvUtils;

/**
 * JobTaskAccess Tester.
 *
 * @author <Authors name>
 * @since <pre>10/11/2007</pre>
 * @version 1.0
 */
public class JobTaskAccessTest extends TestCase {
    public JobTaskAccessTest(String name) {
        super(name);
    }

    public void setUp() throws Exception {
        super.setUp();
    }

    public void tearDown() throws Exception {
        super.tearDown();
    }

    public static Test suite() {
        return new TestSuite(JobTaskAccessTest.class);
    }

    public void testGetJobTask() throws Exception {
        System.getProperties().put(DBAccess.DS_NAME,"test_ds");
        System.getProperties().put(EnvUtils.JNDI_PROVIDER_HOST_NAME,"localhost");

        String s =
                " select jtt.id " +
                        " from job_task_time jtt, job_schedule js "+
                        " where jtt.job_schedule_id = js.id "+
                        " and jtt.job_task_id = 59886 and js.work_schedule_id in ( 53743  ) ";
        System.out.println(s);
        List l = DBAccess.execute(s,true);
        if (l.size()>0) {
            Map m = (Map)l.iterator().next();
            System.out.println(m.toString());
            Long tmp = (Long)m.get("ID");
        }
    }


}
