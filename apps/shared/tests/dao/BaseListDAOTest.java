package dao;

import junit.framework.Test;
import junit.framework.TestSuite;
import junit.framework.TestCase;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

import org.mlink.iwm.dao.LocatorsDAO;
import org.mlink.iwm.dao.PaginationRequest;
import org.mlink.iwm.dao.PaginationResponse;
import org.mlink.iwm.dao.SearchCriteria;
import org.mlink.iwm.util.DBAccess;
import org.mlink.iwm.util.EnvUtils;
//import org.mlink.iwm.bean.LocatorLI;

/**
 * PaginatedListDAO Tester.
 *
 * @author <Authors name>
 * @since <pre>09/18/2006</pre>
 * @version 1.0
 */
public class BaseListDAOTest extends TestCase {
    public BaseListDAOTest(String name) {
        super(name);
    }

    public void setUp() throws Exception {
        super.setUp();
    }

    public void tearDown() throws Exception {
        super.tearDown();
    }

    public void testGetPropertyToColumnMap() throws Exception {
        //TODO: Test goes here...
    }

    public void testProcess() throws Exception {
       System.getProperties().put(DBAccess.DS_NAME,"test_ds");
       System.getProperties().put(EnvUtils.JNDI_PROVIDER_HOST_NAME,"localhost");
        Connection con = DBAccess.getDBConnection();
        assertFalse("failed to get Connection ",con==null);
        con.close();

        PaginationRequest request = new PaginationRequest(1,100,"inServiceDate","DESC");
        //request.addParameter("locatorId",null);
        //PaginatedListDAO dao = DaoFactory.get(DaoFactory.EnumDAO.LOCATORS);
        //PaginationResponse response= dao.process(request);
        //PaginationResponse response = LocatorsDAO.getInstance().getTopLevelLocators(request);
        PaginationResponse response = new LocatorsDAO().getData(new SearchCriteria(),request);

        System.out.println("Total Count=" + response.getTotalCount());
        List rows = response.getRows();
        for (int i = 0; i < rows.size(); i++) {
            Map map =  (Map)rows.get(i);
            System.out.println(map);

        }

        /*List beans = response.convertRowsToClasses(LocatorLI.class);
        for (int i = 0; i < beans.size(); i++) {
            Object o =  beans.get(i);
            System.out.println(o);
        }*/
    }

    public static Test suite() {
        return new TestSuite(BaseListDAOTest.class);
    }
}
