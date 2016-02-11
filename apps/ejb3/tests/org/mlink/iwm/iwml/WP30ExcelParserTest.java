package org.mlink.iwm.iwml;

import junit.framework.Test;
import junit.framework.TestSuite;
import junit.framework.TestCase;

import java.io.File;

import org.mlink.iwm.iwml.WP30ExcelParser;

/**
 * WP30ExcelParser Tester.
 *
 * @author <Authors name>
 * @since <pre>12/15/2007</pre>
 * @version 1.0
 */
public class WP30ExcelParserTest extends TestCase {
    public WP30ExcelParserTest(String name) {
        super(name);
    }

    public void setUp() throws Exception {
        super.setUp();
    }

    public void tearDown() throws Exception {
        super.tearDown();
    }

    public static Test suite() {
        return new TestSuite(WP30ExcelParserTest.class);
    }

    public void testMe(){
        try {
            WP30ExcelParser ep = new WP30ExcelParser(new File("/Users/andreipovodyrev/Masterlink/iwm_v35/apps/ejb3/src/org/mlink/iwm/iwml/VP30 Data Table 17Dec07b-noblankrows.xls"));
            System.out.println(ep.toXML());
            //ep.toXML();
        } catch (Exception e) {
        }
    }
}
