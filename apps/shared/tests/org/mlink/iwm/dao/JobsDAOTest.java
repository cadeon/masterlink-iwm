package org.mlink.iwm.dao;

import junit.framework.Test;
import junit.framework.TestSuite;
import junit.framework.TestCase;
import org.mlink.iwm.util.DBAccess;
import org.mlink.iwm.util.EnvUtils;

import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * JobsDAO Tester.
 *
 * @author <Authors name>
 * @since <pre>10/31/2006</pre>
 * @version 1.0
 */
public class JobsDAOTest extends TestCase {
    public JobsDAOTest(String name) {
        super(name);
    }

    public void setUp() throws Exception {
        super.setUp();
    }

    public void tearDown() throws Exception {
        super.tearDown();
    }

    public void testMe() throws Exception {
        Connection con=null;
        try{
            System.getProperties().put(DBAccess.DS_NAME,"test_ds");
            System.getProperties().put(EnvUtils.JNDI_PROVIDER_HOST_NAME,"localhost");
            con = DBAccess.getDBConnection();
            assertFalse("failed to get Connection ",con==null);

            //DBAccess.executeUpdate("ALTER TABLE ORGANIZATION ADD PARENT_ID NUMBER");


            long t1=  System.currentTimeMillis();
            PaginationRequest request = new PaginationRequest(0,40,"ID","ASC");
            JobsCriteria cr  = new JobsCriteria(new HashMap());
            PaginationResponse response = new JobsDAO().getData(cr,request);

            System.out.println("Total Count=" + response.getTotalCount());
            List rows = response.getRows();
            for (int i = 0; i < rows.size(); i++) {
                Map map =  (Map)rows.get(i);
                System.out.println(map);
            }
            System.out.println("exec time (msecs)="+ (System.currentTimeMillis()-t1));

        }finally{
            if(con!=null) con.close();
        }
    }

    public static Test suite() {
        return new TestSuite(JobsDAOTest.class);
    }
}
