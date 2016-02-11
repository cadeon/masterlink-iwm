package org.mlink.iwm.iwml;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.FinderException;

import org.apache.log4j.Logger;
import org.mlink.iwm.access3.ServiceLocator;
import org.mlink.iwm.entity3.Organization;
import org.mlink.iwm.entity3.Party;
import org.mlink.iwm.entity3.Person;
import org.mlink.iwm.lookup.SkillLevelRef;
import org.mlink.iwm.lookup.SkillTypeRef;
import org.mlink.iwm.session.PolicySF;
import org.mlink.iwm.util.Constants;
import org.mlink.iwm.util.ConvertUtils;

/**
 * Created by IntelliJ IDEA.
 * User: andreipovodyrev
 * Date: Jun 2, 2007
 */
public class WorkerUpdator extends Updator{
    private static final Logger logger = Logger.getLogger(WorkerUpdator.class);

    private Worker worker;

    public WorkerUpdator(Worker worker){
        this.worker=worker;
    }

    /** Find worker and update, if worker not found create new */
    public void store() throws Exception{
        log("processing worker " + worker.getFname() + " " + worker.getLname());
        PolicySF psf = ServiceLocator.getPolicySFLocal();

        Person person =new Person(); person.setParty(new Party());
        if(worker.getOrganization()!=null){
            //Identify Organization.
            Organization organization = psf.getOrganizationByName(worker.getOrganization());
            if(organization==null){
                throw new Exception ("Not valid organization " + worker.getOrganization());
            }else{
                person.setOrganizationId(organization.getId());
            }
        }
        
        person.setActive(Constants.YES);
        person.setUsername(worker.getFname().toLowerCase().trim()+worker.getLname().toLowerCase().trim());
        Party party = person.getParty();
        party.setName(worker.getFname() + " " + worker.getLname());
        party.setEmail(worker.getEmail());
        person.setBillingRate(worker.getBillingRate());
        Long  personId;
        try{
            Person personVo = psf.getPersonByName(person.getUsername());
            person.setId(personVo.getId());
            //update person
            log("Update existing id="+personVo.getId());
            log(ConvertUtils.toString("\t", worker));
            psf.update(person);
            personId=personVo.getId();
        }catch(Exception fe){
            //then create person
            log("Create new");
            if(worker.getOrganization()==null){
                log("Organization required for new worker ");
            }
            log(ConvertUtils.toString("\t", worker));
            personId = psf.create(person);
        }

        //Now process the skills if present
        if(worker.getSkills()!=null){
            List <org.mlink.iwm.entity3.Skill> newskills = new ArrayList<org.mlink.iwm.entity3.Skill>();
            for (int i = 0; i < worker.getSkills().size(); i++) {
                Skill skill =  worker.getSkills().get(i);
                int skillTypeId = SkillTypeRef.getIdByLabel(skill.getType());
                int skillLevelId = SkillLevelRef.getIdByLabel(skill.getLevel());
                org.mlink.iwm.entity3.Skill skillvo = new org.mlink.iwm.entity3.Skill();
                skillvo.setSkillTypeId((long)skillTypeId);
                skillvo.setSkillLevelId((long)skillLevelId);
                newskills.add(skillvo);
            }
            psf.updatePersonSkills(personId,newskills);
        }

    }
}
