package org.mlink.iwm.dwr;

import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.mlink.iwm.access3.ServiceLocator;
import org.mlink.iwm.bean.ResponsePage;
import org.mlink.iwm.dao.DaoFactory;
import org.mlink.iwm.dao.PaginationRequest;
import org.mlink.iwm.dao.PaginationResponse;
import org.mlink.iwm.dao.WorkersCriteria;
import org.mlink.iwm.entity3.Address;
import org.mlink.iwm.entity3.Party;
import org.mlink.iwm.entity3.Person;
import org.mlink.iwm.lookup.LookupMgr;
import org.mlink.iwm.lookup.PersonRef;
import org.mlink.iwm.session.PolicySF;
import org.mlink.iwm.util.CopyUtils;

/**
 * User: John Mirick
 * Note: throwing Exceptions outside of methods allows use of DWR error handling (alerts in UI)
 * Note DWR does not support method oveloading well. Avoid that!
 */


public class Workers implements MaintainanceTable{
    private static final Logger logger = Logger.getLogger(Workers.class);

    public ResponsePage getData(HashMap criteria, int offset, int pageSize, String orderBy, String orderDirection) throws Exception {
        WorkersCriteria cr = new WorkersCriteria(criteria);
        SessionUtil.setAttribute("WorkersCriteria",cr);    //store in session, reuse in later when coming back
        PaginationRequest request = new PaginationRequest(offset,pageSize,orderBy,orderDirection);
        PaginationResponse response = DaoFactory.process(DaoFactory.NAME.WorkersDAO,cr,request);
        List lst =  response.convertRowsToClasses(org.mlink.iwm.bean.Worker.class);
        return new ResponsePage(response.getTotalCount(),lst);
    }


    public String deleteItem(Long personId) throws Exception{
        String rtn = ITEM_DELETED_MSG;
        PolicySF policy = ServiceLocator.getPolicySFLocal();
        Person person = policy.get(Person.class, personId);
        policy.removePerson(person);
        return rtn;
    }

    /**
     * Get worker for given personId
     * @param personId
     * @return
     * @throws Exception
     */
    public org.mlink.iwm.bean.Worker getItem(Long personId) throws Exception{
    	org.mlink.iwm.bean.Worker form = new org.mlink.iwm.bean.Worker();
        PolicySF policy = ServiceLocator.getPolicySFLocal();
        Person person = (Person)policy.get(Person.class, personId);
        Party party = person.getParty();
		CopyUtils.copyProperties(form, party);
		CopyUtils.copyProperties(form, person);
		form.setPersonId(Long.toString(person.getId()));
		form.setId(Long.toString(party.getId()));
		form.setAddressId(Long.toString(party.getAddress().getId()));
		form.setType(person.getWorkerTypeId().toString());
		return form;
    }

    /**
     * Updates/Creates worker
     * @param map HashMap is analaog to Worker class
     * @return Optional Business message
     * @throws Exception
     */
    public String saveItem(HashMap map) throws Exception{
    	String rtn = ITEM_SAVED_OK_MSG;
		PolicySF policy = ServiceLocator.getPolicySFLocal();
		Person person = convertMapToPerson(map);
        long personId = person.getId();
        Party party;
		
		if (personId > 0) {
			policy.update(person);
		} else {
			party = person.getParty();
			party.getAddress().setId(0);
			party.setId(0);
			person.setId(0);
			
			personId = policy.create(person);
			person.setId(personId);
			if (personId <= 0) {
				logger.error("Person not created.");
			} else {
				logger.info("Person created sucessfully Id: "+personId);
				LookupMgr.reloadCDLV(PersonRef.class);
			}
		}
		return rtn;
    }
    
    public static Person convertMapToPerson(HashMap map) throws Exception{
    	PolicySF policy = ServiceLocator.getPolicySFLocal();
        Address address = new Address();
		Party party = new Party();
		CopyUtils.copyProperties(party, map);
		party.setAddress(address);
		
		Person person = new Person();
		person.setParty(party);
		CopyUtils.copyProperties(person, map);
		person.setWorkerTypeId(Integer.parseInt((String)map.get("type")));
		person.setTitle((String)map.get("title"));
		
		person.setId(Long.decode((String)map.get("personId")));
		if(person.getId()>0){
			party.setId(Long.decode((String)map.get("id")));
			address.setId(Long.decode((String)map.get("addressId")));
		}
		return person;
    }
}
