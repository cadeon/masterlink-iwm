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
 * TemplateTasksDAO Tester.
 *
 * @author <Authors name>
 * @since <pre>10/30/2006</pre>
 * @version 1.0
 */
public class TemplateTasksDAOTest extends TestCase {
    public TemplateTasksDAOTest(String name) {
        super(name);
    }

    public void setUp() throws Exception {
        super.setUp();
    }

    public void tearDown() throws Exception {
        super.tearDown();
    }

    public void testGetInstance() throws Exception {
        System.getProperties().put(DBAccess.DS_NAME,"test_ds");
        System.getProperties().put(EnvUtils.JNDI_PROVIDER_HOST_NAME,"localhost");
        Connection con = DBAccess.getDBConnection();
        assertFalse("failed to get Connection ",con==null);
        con.close();
        long t1=  System.currentTimeMillis();
        PaginationRequest request = new PaginationRequest(0,2000,"ID","ASC");
        Map params = new HashMap();
        params.put("id","10000261560");
        TasksCriteria cr  = new TasksCriteria(params);
        PaginationResponse response = new TemplateTasksDAO().getData(cr,request);

        System.out.println("Total Count=" + response.getTotalCount());
        List rows = response.getRows();
        for (int i = 0; i < rows.size(); i++) {
            Map map =  (Map)rows.get(i);
            System.out.println(map);
        }

         System.out.println("exec time (msecs)="+ (System.currentTimeMillis()-t1));
    }

    public static Test suite() {
        return new TestSuite(TemplateTasksDAOTest.class);
    }
}
