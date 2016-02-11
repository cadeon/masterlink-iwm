package org.mlink.iwm.dwr;

import java.util.HashMap;
import java.util.List;

import org.mlink.iwm.access3.ServiceLocator;
import org.mlink.iwm.bean.Skill;
import org.mlink.iwm.entity3.Person;
import org.mlink.iwm.entity3.User;
import org.mlink.iwm.exception.BusinessException;
import org.mlink.iwm.rules.UserRules3;
import org.mlink.iwm.session.PolicySF;
import org.mlink.iwm.session.PolicySFLocal;
import org.mlink.iwm.util.CopyUtils;

/**
 * User: andrei
 * Date: Jan 14, 2007
 */
public class WorkerCreateWizard implements ReturnCodes{

    /**
     * This is the last step in creation of user
     * @return
     * @throws Exception
     */
    public String finish() throws Exception{
    	PolicySF psf = (PolicySFLocal) ServiceLocator.getPolicySFLocal();
        UserRoles userRolesAction = new UserRoles();
        String rtn = ITEM_SAVED_OK_MSG;
        try{
            Person person = (Person) SessionUtil.getAttribute("newUser");
            //skills prepared by WorkerSkillsMaint being a part of the Create Wizard (the second step in creation of user)
            
            User user = (User) SessionUtil.getAttribute("newUserPwd");
            //Long personId =  psf.create(person);
            
            user.setPerson(person);
            Long userId =  psf.create(user);
            person = user.getPerson();
            person.setUserId(userId);
            psf.update(person);
            Long personId = user.getPerson().getId();
            
            List <org.mlink.iwm.entity3.Skill> skills = (List<org.mlink.iwm.entity3.Skill>)CopyUtils.copyProperties(org.mlink.iwm.entity3.Skill.class,(List<Skill>)SessionUtil.getAttribute("skills"));
            if(skills!=null){
                psf.updatePersonSkills(personId,skills);
            }

            /*if(SessionUtil.getAttribute("workerCqls")!=null){
                psf.updatePersonCQLS(Long.valueOf(personId), CopyUtils.copyProperties(CQLSVO.class,(Collection<CQLS>)SessionUtil.getAttribute("workerCqls")));
            }*/

            // roles are prepared by UserRoles being a part of the Create Wizard (the third step in creation of user)
            userRolesAction.saveItem(user.getId());
        }catch(BusinessException be){
            rtn = be.getMessage();
        }
        return rtn;
    }

    /**
     * This is the first step in creation of user
     * @param map
     * @return
     * @throws Exception
     */
    public String saveWorkerInfo(HashMap map) throws Exception {
        Person person = Workers.convertMapToPerson(map);
        String rtn = ITEM_SAVED_OK_MSG;
        try {
            UserRules3.validateUsername(person.getId(),(String)map.get("username"));
        } catch (BusinessException e) {
            rtn = e.getMessage();
        }
        SessionUtil.setAttribute("newUser",person);
        
        User user = new User();
        CopyUtils.copyProperties(user, map);
        if(user.getPassword()==null){
        	user.setPassword(user.getUsername());
        }
        SessionUtil.setAttribute("newUserPwd",user);
        return rtn;
    }
}
