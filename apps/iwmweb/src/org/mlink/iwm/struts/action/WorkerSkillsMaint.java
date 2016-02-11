package org.mlink.iwm.struts.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.mlink.iwm.access3.ServiceLocator;
import org.mlink.iwm.base.WebException;
import org.mlink.iwm.bean.Skill;
import org.mlink.iwm.lookup.DummyDropdown;
import org.mlink.iwm.lookup.LookupMgr;
import org.mlink.iwm.lookup.OptionItem;
import org.mlink.iwm.lookup.SkillLevelRef;
import org.mlink.iwm.lookup.SkillTypeRef;
import org.mlink.iwm.session.PolicySF;
import org.mlink.iwm.util.CopyUtils;
import org.mlink.iwm.util.EqualsUtils;

/**
 * User: andrei
 * Date: Dec 6, 2006
 */
public class WorkerSkillsMaint extends BaseAction {
    private static final Logger logger = Logger.getLogger(WorkerSkillsMaint.class);

    public ActionForward executeLogic(ActionMapping mapping,
                                      ActionForm aform,
                                      HttpServletRequest request,
                                      HttpServletResponse response)  throws WebException {
        Skill form  = (Skill)aform;
        ActionForward forward = findForward(mapping, request);

        logger.debug("execute "+forward.getName());

        if("addSkill".equals(form.getForward())){
            processAddSkill(mapping, form,request, response);
        }
        else if("removeSkill".equals(form.getForward())){
            processRemoveSkill(mapping, form,request, response);
        }
        else if("updateSkillLevel".equals(form.getForward())){
            processUpdateSkillLevel(mapping, form,request, response);
        }
        else if("save".equals(form.getForward())){
            processSave(mapping, form,request, response);
        }
        else{
            processRead(mapping, form,request, response);
        }

        return forward;
    }

    private void processAddSkill(ActionMapping mapping, Skill form, HttpServletRequest request, HttpServletResponse response) throws WebException{
        try{
            Skill skill = new Skill();
            skill.setSkillTypeId(form.getSkillTypeId());
            skill.setSkillLevelId(String.valueOf(SkillLevelRef.getIdByCode(SkillLevelRef.ONE)));
            String personId = null;
            if(!"undefined".equals(form.getPersonId())){ //new user wizard in progress
            	skill.setPersonId(form.getPersonId());
            }else{
            	skill.setPersonId("0");
            }
            
            Collection <Skill> skills =  (Collection<Skill>)get(request,"skills");
            skills.add(skill);
            prepareForm(form,request);
        } catch (Exception e) {
            throw new WebException(e);
        }
    }
    private void processRemoveSkill(ActionMapping mapping, Skill form, HttpServletRequest request, HttpServletResponse response) throws WebException{
        try{
            Collection <Skill> skills =  (Collection<Skill>)get(request,"skills");
            for (Iterator<Skill> iterator = skills.iterator(); iterator.hasNext();) {
                Skill skill =  iterator.next();
                if(EqualsUtils.areEqual(skill.getSkillTypeId(),form.getSkillTypeId())){
                    iterator.remove();
                }
            }
            prepareForm(form,request);
        } catch (Exception e) {
            throw new WebException(e);
        }
    }

    private void processSave(ActionMapping mapping, Skill form, HttpServletRequest request, HttpServletResponse response) throws WebException{
        try{
            PolicySF psf = ServiceLocator.getPolicySFLocal();
            psf.updatePersonSkills(Long.valueOf(form.getPersonId()),(List<org.mlink.iwm.entity3.Skill>)CopyUtils.copyProperties(org.mlink.iwm.entity3.Skill.class,(List<Skill>)get(request,"skills")));
            prepareForm(form,request);
        } catch (Exception e) {
            throw new WebException(e);
        }
    }

    private void processUpdateSkillLevel(ActionMapping mapping, Skill form, HttpServletRequest request, HttpServletResponse response) throws WebException{
        try{
            Collection <Skill> skills =  (Collection<Skill>)get(request,"skills");
            for (Skill skill : skills) {
                if(EqualsUtils.areEqual(skill.getSkillTypeId(),form.getSkillTypeId())){
                    skill.setSkillLevelId(form.getSkillLevelId());
                }
            }
            prepareForm(form,request);
        } catch (Exception e) {
            throw new WebException(e);
        }
    }

    private void processRead(ActionMapping mapping, Skill form, HttpServletRequest request, HttpServletResponse response) throws WebException{
        try {
        	PolicySF psf = ServiceLocator.getPolicySFLocal();
            Collection <Skill> skills;
            if(form.getPersonId()==null){ //new user wizard in progress
                skills= new ArrayList <Skill> ();
            }else{
                skills = CopyUtils.copyProperties(Skill.class,psf.getSkillsbyPersonId(Long.parseLong(form.getPersonId())));
            }
            put(request,"skills",skills);
            Collections.sort((List)skills);            
            prepareForm(form,request);
        } catch (Exception e) {
            throw new WebException(e);
        }
    }

    private void prepareForm(Skill form, HttpServletRequest request) throws WebException{
        try {
            Collection <Skill> skills =  (Collection<Skill>)get(request,"skills");
            //Collections.sort((List)skills);
            form.getItems().addAll(skills);
            SkillTypeRef skillTypetRef = (SkillTypeRef) LookupMgr.getInstance(SkillTypeRef.class);

            // prepare skills dropdown: all skills minus skills already present
            List <OptionItem> filteredOptions = new ArrayList<OptionItem>();
            filteredOptions.addAll(skillTypetRef.getOptions());
            Iterator <OptionItem> i = filteredOptions.iterator();
            while (i.hasNext()) {
                OptionItem option =  i.next();
                for (Object skill : skills) {
                    if(option.getValue().toString().equals(((Skill)skill).getSkillTypeId())) {
                        i.remove(); break;
                    }
                }
            }

            request.setAttribute("SkillTypeRefDyna",new DummyDropdown(filteredOptions));

        } catch (Exception e) {
            throw new WebException(e);
        }
    }

}
