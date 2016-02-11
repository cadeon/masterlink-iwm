package org.mlink.agent.xao;

import java.io.StringWriter;

/*import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Validator;
*/

public class WorldXAO {
	private static WorldXAO instance = null;

	//private static JAXBContext jaxbContext = null;
	private static ObjectFactory objectFactory = null;
	
	public static final String CLONE  = "clone";
	public static final String DELETE  = "delete";
	public static final String MODIFY = "modify";
	public static final String RENAME = "rename";
	public static final String RUN    = "run";
	
	public static final String FAIL    = "fail";
	public static final String SUCCESS = "success";

	
	private WorldXAO() throws Exception {
		super();
		//jaxbContext = JAXBContext.newInstance("org.mlink.iwm.xao");
		objectFactory = new ObjectFactory();
	}

	/**
	 * Gets the singleton instance of the XML Access Object
	 * 
	 * @return WorldXAO
	 * @throws XAOException
	 */
	public static WorldXAO getInstance() {
		if (instance == null) {
			try {
				instance = new WorldXAO();
			} catch (Exception je) {
				throw new XAOException(
						"Exception during XAO instance creation", je);
			}
		}
		return instance;
	}
	
	/**
	 *  Creates a SimResponse object 
	 *  
	 *  @return SimResponse
	 */
	public SimResponse createSimResponse() {
		return objectFactory.createSimResponse();
	}
	/**
	 * 
	 * @return String containing  the xml representation of the SimResponse
	 * @throws XAOException
	 */
	public String getXml(SimResponse sr) {
		StringWriter sw = new StringWriter();
		try {
			/*Marshaller marshaller = jaxbContext.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT,Boolean.TRUE);
			
			Validator validator = jaxbContext.createValidator();
			validator.validate(sr);
			
			marshaller.marshal(sr, sw);
			*/
			
		} catch (Exception je) {
			throw new XAOException("Exception creating SimResponse",je);
		}
		return sw.toString();
	}
}
