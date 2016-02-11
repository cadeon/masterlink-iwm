package org.mlink.iwm.dao;

import junit.framework.Test;
import junit.framework.TestSuite;
import junit.framework.TestCase;

import java.sql.Connection;
import java.util.List;

import org.mlink.iwm.util.DBAccess;
import org.mlink.iwm.util.EnvUtils;

/**
 * GlobalSearchDAO Tester.
 *
 * @author <Authors name>
 * @since <pre>04/17/2007</pre>
 * @version 1.0
 */
public class GlobalSearchDAOTest extends TestCase {
    public GlobalSearchDAOTest(String name) {
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

            SearchCriteria cr = new SearchCriteria();
            cr.setFilterText("Paint");
            PaginationRequest request = new PaginationRequest(0,50,"id","ASC");
            DAOResponse response = DaoFactory.process(DaoFactory.NAME.GlobalSearchDAO,cr,request);
            List lst =  response.convertRowsToHtml();
            System.out.println("Total count:" +lst.size());
            for (int i = 0; i < lst.size(); i++) {
                Object o =  lst.get(i);
                System.out.println(o.toString());
            }

        }finally{
            if(con!=null) DBAccess.close(con);
        }
    }

    public static Test suite() {
        return new TestSuite(GlobalSearchDAOTest.class);
    }
}
