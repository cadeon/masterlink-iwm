package org.mlink.iwm.session;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.apache.log4j.Logger;
import org.mlink.iwm.access3.ServiceLocator;
import org.mlink.iwm.entity3.Address;
import org.mlink.iwm.entity3.Party;
import org.mlink.iwm.util.EnvUtils;

/**
 * PolicyBean Tester.
 *
 * @author <Authors name>
 * @since <pre>03/05/2009</pre>
 * @version 1.0
 */
public class PolicyBeanTest extends TestCase {

    private static final Logger logger = Logger.getLogger(PolicyBeanTest.class);

    public PolicyBeanTest(String name) {
        super(name);
    }

    public void setUp() throws Exception {
        super.setUp();
    }

    public void tearDown() throws Exception {
        super.tearDown();
    }

    public static Test suite() {
        return new TestSuite(PolicyBeanTest.class);
    }

    /**
     * Test basis entity operations
     * @throws Exception
     */
    public void test1() throws Exception{
        System.getProperties().put(EnvUtils.JNDI_PROVIDER_HOST_NAME,"localhost");
        PolicySFRemote policy = ServiceLocator.getPolicySFRemote();
        logger.info("policy:"+policy);
        String test ="hello";
        String resp = policy.echo(test);      
        logger.info(resp);
        assertTrue(resp.equals(test));
        
        Address address = new Address();
        address.setAddress1("street1");
        address.setAddress2("street2");
        address.setCity("city");
        address.setState("state");
        address.setZip("zip");

        Long id = policy.create(address);
        address.setId(id);
        Address address2 = policy.get(Address.class,id);
        assertTrue(address.equals(address2));

        policy.remove(address2);
        Address address3 = policy.get(Address.class,id);
        assertTrue(address3==null);

        Party party = policy.get(Party.class,6);
        Address address4  = party.getAddress();
        logger.info(party);
        logger.info(address4);
    }

    /**
     * Test sample finders
     * @throws Exception
     */
    public void test2() throws Exception{
        System.getProperties().put(EnvUtils.JNDI_PROVIDER_HOST_NAME,"localhost");
        PolicySFRemote policy = ServiceLocator.getPolicySFRemote();
        /**List <Party> parties = policy.getAllParties();
         * calling this will produce N+1 problem. All dresses will be retrieved for the parties. Mus use LAZY fetch
         * unit test however produces NoClassDefFoundError: javassist/util/proxy/MethodHandler when LAZY is on.
         * This is problem with client classpath. Cant fix this now
        logger.info("Number of parties = " + parties.size());*/
    }
}
