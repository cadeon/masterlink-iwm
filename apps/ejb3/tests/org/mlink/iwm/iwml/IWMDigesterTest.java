package org.mlink.iwm.iwml;

import junit.framework.Test;
import junit.framework.TestSuite;
import junit.framework.TestCase;

import java.io.InputStream;
import java.io.FileInputStream;
import java.util.List;

/**
 * IWMDigester Tester.
 *
 * @author <Authors name>
 * @since <pre>06/04/2007</pre>
 * @version 1.0
 */
public class IWMDigesterTest extends TestCase {
    public IWMDigesterTest(String name) {
        super(name);
    }

    public void setUp() throws Exception {
        super.setUp();
    }

    public void tearDown() throws Exception {
        super.tearDown();
    }

    public void testme(){
       IWMLDigester dg = new IWMLDigester();
        try {
            InputStream is = dg.getClass().getClassLoader().getResourceAsStream("org/mlink/iwm/iwml/iwml.xsd.xml");
            List rtn = dg.parse(is);
            System.out.print(rtn.size());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static Test suite() {
        return new TestSuite(IWMDigesterTest.class);
    }
}
