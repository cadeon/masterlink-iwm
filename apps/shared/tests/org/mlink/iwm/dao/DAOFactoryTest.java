package org.mlink.iwm.dao;

import junit.framework.Test;
import junit.framework.TestSuite;
import junit.framework.TestCase;

/**
 * DaoFactory Tester.
 *
 * @author <Authors name>
 * @since <pre>11/14/2006</pre>
 * @version 1.0
 */
public class DAOFactoryTest extends TestCase {
    public DAOFactoryTest(String name) {
        super(name);
    }

    public void setUp() throws Exception {
        super.setUp();
    }

    public void tearDown() throws Exception {
        super.tearDown();
    }

    public void testMe() throws Exception {
       //PaginatedListDAO dao = DaoFactory.getInstance().get(DaoFactory.NAME.ObjectsDAO);

    }


    public static Test suite() {
        return new TestSuite(DAOFactoryTest.class);
    }
}
