package org.mlink.iwm.dao;

import junit.framework.Test;
import junit.framework.TestSuite;
import junit.framework.TestCase;

import java.sql.Connection;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import org.mlink.iwm.util.DBAccess;
import org.mlink.iwm.util.EnvUtils;

/**
 * TemplatesDAO Tester.
 *
 * @author <Authors name>
 * @since <pre>10/19/2006</pre>
 * @version 1.0
 */
public class ObjectTemplatesDAOTest extends TestCase {
    public ObjectTemplatesDAOTest(String name) {
        super(name);
    }

    public void setUp() throws Exception {
        super.setUp();
    }

    public void tearDown() throws Exception {
        super.tearDown();
    }

    public void testMe() throws Exception {
        System.getProperties().put(DBAccess.DS_NAME,"test_ds");
        System.getProperties().put(EnvUtils.JNDI_PROVIDER_HOST_NAME,"localhost");
        Connection con = DBAccess.getDBConnection();
        assertFalse("failed to get Connection ",con==null);
        con.close();
        long t1=  System.currentTimeMillis();
        PaginationRequest request = new PaginationRequest(0,1000,"taskCount","ASC");
        Map params = new HashMap();
        params.put("id","2");
        TemplatesCriteria cr  = new TemplatesCriteria(params);
        PaginationResponse response = new TemplatesDAO().getData(cr,request);

        System.out.println("Total Count=" + response.getTotalCount());
        List rows = response.getRows();
        for (int i = 0; i < rows.size(); i++) {
            Map map =  (Map)rows.get(i);
            System.out.println(map);
        }
        System.out.println("exec time (msecs)="+ (System.currentTimeMillis()-t1));
        
    }



    public static Test suite() {
        return new TestSuite(ObjectTemplatesDAOTest.class);
    }
}
