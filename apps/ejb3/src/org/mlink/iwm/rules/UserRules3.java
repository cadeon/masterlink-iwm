package org.mlink.iwm.rules;


import org.mlink.iwm.access3.ServiceLocator;
import org.mlink.iwm.entity3.Person;
import org.mlink.iwm.exception.BusinessException;
import org.mlink.iwm.session.PolicySF;

/**
 * Date: Apr 3, 2005
 * Time: 5:34:38 PM
 */
public class UserRules3 {

    /**
     * When user is created/updated make sure username is not a duplicate
     * @param person
     * @throws BusinessException
     */
    public static void validateUsername(long personId, String username) throws  BusinessException{
        PolicySF psf = ServiceLocator.getPolicySFLocal();
    	psf.validateUsername(personId, username);
    }

    public static void validateRequired(Person person) throws  BusinessException{
    	if(person.getParty().getName()==null){
            throw new BusinessException("First and last name required ");
        }
        if(person.getOrganizationId()==null){
            throw new BusinessException("Organization is required ");
        }
    }


}
