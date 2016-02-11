package org.mlink.iwm.integration;

import junit.framework.Test;
import junit.framework.TestSuite;
import junit.framework.TestCase;
//import org.mlink.iwm.jaxb.SampleMe;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * SampleMe Tester.
 *
 * @author <Authors name>
 * @since <pre>05/04/2007</pre>
 * @version 1.0
 */
public class SampleMeTest extends TestCase {
    public SampleMeTest(String name) {
        super(name);
    }

    public void setUp() throws Exception {
        super.setUp();
    }

    public void tearDown() throws Exception {
        super.tearDown();
    }

    public void testMe() throws Exception {
        //SampleMe test = new SampleMe();
        //test.run();
        String fileName = "VP30_02232008jkhasjkhd";

        if(fileName.length()>=13 && fileName.substring(5,13).matches("\\d{8}")){     //expected   VP30_02232008xxxxx.xls
            SimpleDateFormat fmt = new SimpleDateFormat("MMddyyyy");
            Date date = fmt.parse(fileName.substring(5,13));
            System.out.println(date);
        }
    }

    public static Test suite() {
        return new TestSuite(SampleMeTest.class);
    }
}
