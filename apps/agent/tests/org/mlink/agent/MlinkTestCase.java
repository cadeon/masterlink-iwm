package org.mlink.agent;
 
import java.util.*;

import junit.framework.*;
import java.beans.*;
import java.io.*;
import java.sql.Connection;
import org.mlink.agent.model.Job;
import org.mlink.agent.util.TestHelper;
import org.mlink.iwm.util.*;


public class MlinkTestCase extends TestCase {

  private static String dataset_root = System.getProperty("dataset_root","tests/datasets");

  public MlinkTestCase() {
    //    LookupMgr.readLookupTables();   

  }
	
    public MlinkTestCase(String name) {
        super(name);
    }

  public void dbConnect() throws Exception {
    System.getProperties().put(DBAccess.DS_NAME,"test_ds");
    System.getProperties().put(EnvUtils.JNDI_PROVIDER_HOST_NAME,"localhost");

    Connection con = DBAccess.getDBConnection();
    assertFalse("failed to get Connection ",con==null);
  }
 
  public void testLoad() {
    assertEquals( ((Job) getBean("Job","1")).toString(),
           TestHelper.getJob(1,60,TestHelper.MECHANIC,5,TestHelper.HIGH,1,TestHelper.LOCATORS[1]).toString());
  }

  public static void p(Object str) {
    if (!(str instanceof String))  str = str.toString();
    System.out.println(str);
  }

  public static void toXml(Object o,String id) {
    p("Feck"+id);
    toXml(o,id,dataset_root);
  }

  public static void toXml(Object o,String id, String file_location) { 
       try {


        // Serialize object into XML
        XMLEncoder encoder = new XMLEncoder(new BufferedOutputStream(
                                  new FileOutputStream(file_location+"/"+
                              o.getClass().getSimpleName()+"-"+id+".xml")));

        /** Next two lines are temporary until  JDK bug 4733558 is fixed 
            in JDK 1.7 **/
        PersistenceDelegate defaultDelegate = 
               new DatePersistenceDelegate();
        encoder.setPersistenceDelegate(java.sql.Date.class, defaultDelegate);

        p("set to write "+o);
        encoder.writeObject(o);
        encoder.close();
    } catch (FileNotFoundException e) {
         Assert.fail("CRAP:"+e.toString()); 
    }
  }
 
  public static Object getBean(String filename){
    dataset_root = System.getProperty("dataset_root","tests/datasets");
    Object result = null;
    try {
       XMLDecoder d = new XMLDecoder(
                  new BufferedInputStream(
                       new FileInputStream(dataset_root + '/' + filename)));
       result = d.readObject();
       d.close();
       return result;
    }
    catch (Exception ex) {
      Assert.fail(ex.toString());
    }
    return result;
  }


  public static Object getBean(String type,String id) {
    return getBean(type+"-"+id+".xml");
  }

}
