package org.mlink.iwm.lookup;

import junit.framework.Test;
import junit.framework.TestSuite;
import junit.framework.TestCase;
import org.mlink.iwm.util.DBAccess;
import org.mlink.iwm.util.EnvUtils;
import org.mlink.iwm.util.Config;

import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * LookupManager Tester.
 *
 * @author <Authors name>
 * @since <pre>10/31/2006</pre>
 * @version 1.0
 */
public class LookupManagerTest extends TestCase {
    public LookupManagerTest(String name) {
        super(name);
    }

    public void setUp() throws Exception {
        super.setUp();
    }

    public void tearDown() throws Exception {
        super.tearDown();
    }

    public void testValidLookup() throws Exception {
        Connection con=null;
        try{
          System.getProperties().put("locator.schema.top","Location");
          /*            System.getProperties().put(DBAccess.DS_NAME,"test_ds");
                        System.getProperties().put(EnvUtils.JNDI_PROVIDER_HOST_NAME,"localhost");*/
            System.getProperties().put(Config.PRODUCTION_SCHEMA,"jagent_test");
            //                      con = DBAccess.getDBConnection();
            //            assertFalse("failed to get Connection ",con==null);

            
            System.out.println(LocatorRef.getLabel(22l));
            assertEquals("286-Hall",LocatorRef.getLabel(22l));
            assertEquals("n/a",SkillTypeRef.getLabel(1)); 
            //            assertEquals("YO",SkillTypeRef.getCode("1")); 
        }finally{
            if(con!=null) con.close();
        }
    }
  /**
    public void testNoInitialization() throws Exception {
        Connection con=null;
        try{

            System.getProperties().put(DBAccess.DS_NAME,"test_ds");
            System.getProperties().put(EnvUtils.JNDI_PROVIDER_HOST_NAME,"localhost");
            con = DBAccess.getDBConnection();
            assertFalse("failed to get Connection ",con==null);


          SkillTypeRef.getCode(1);
         

        }finally{
            if(con!=null) con.close();
        }
    }
  **/

    public static Test suite() {
        return new TestSuite(LookupManagerTest.class);
    }
}
