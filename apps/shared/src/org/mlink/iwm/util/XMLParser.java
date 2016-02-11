package org.mlink.iwm.util;

import java.beans.PersistenceDelegate;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringBufferInputStream;


import org.apache.log4j.Logger;

/**
 * User: andreipovodyrev
 * Date: Dec 4, 2007
 */
public class XMLParser {
    protected static final Logger logger = Logger.getLogger(XMLParser.class);
    
	public static Object getBean(String req) {
		Object result = null;
		try {
			XMLDecoder d = new XMLDecoder(new StringBufferInputStream(req));
			result = d.readObject();
			d.close();
			return result;
		} catch (Exception ex) {
			//Assert.fail(ex.toString());
		}
		return result;
	}

	public static String toXml(Object o) {
		OutputStream output = new OutputStream() {
			private StringBuilder string = new StringBuilder();

			@Override
			public void write(int b) throws IOException {
				this.string.append((char) b);
			}

			// Netbeans IDE automatically overrides this toString()
			public String toString() {
				return this.string.toString();
			}
		};
		
		StringBuffer sb = new StringBuffer(50000);
		// Serialize object into XML
		XMLEncoder encoder = new XMLEncoder(output);

		/**
		 * Next two lines are temporary until JDK bug 4733558 is fixed in JDK
		 * 1.7
		 **/
		PersistenceDelegate defaultDelegate = new DatePersistenceDelegate();
		encoder.setPersistenceDelegate(java.sql.Date.class, defaultDelegate);

		// p("set to write "+o);
		encoder.writeObject(o);
		encoder.close();
		
		return output.toString();
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

	        //p("set to write "+o);
	        encoder.writeObject(o);
	        encoder.close();
	    } catch (FileNotFoundException e) {
	         //Assert.fail("CRAP:"+e.toString()); 
	    }
	  }
	 
	  public static Object getBeanFromFile(String filename){
	    String dataset_root = System.getProperty("dataset_root","tests/datasets/expected_results");
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
	      //Assert.fail(ex.toString());
	    }
	    return result;
	  }

	  public static Object getBean(String type,String id) {
	    return getBean(type+"-"+id+".xml");
	  }
}
