package org.mlink.iwm.dwr;

import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.mlink.iwm.access3.ServiceLocator;
import org.mlink.iwm.bean.ResponsePage;
import org.mlink.iwm.dao.DaoFactory;
import org.mlink.iwm.dao.PaginationRequest;
import org.mlink.iwm.dao.PaginationResponse;
import org.mlink.iwm.dao.SkillTypesCriteria;
import org.mlink.iwm.entity3.Skill;
import org.mlink.iwm.entity3.SkillType;
import org.mlink.iwm.lookup.LookupMgr;
import org.mlink.iwm.lookup.SkillTypeRef;
import org.mlink.iwm.session.PolicySF;
import org.mlink.iwm.util.CopyUtils;

/**
 * User: Raghavendra Kote
 * Note: throwing Exceptions outside of methods allows use of DWR error handling (alerts in UI)
 * Note DWR does not support method oveloading well. Avoid that!
 */


public class SkillTypes implements MaintainanceTable{
    private static final Logger logger = Logger.getLogger(SkillTypes.class);
    
    public ResponsePage getData(HashMap criteria, int offset, int pageSize, String orderBy, String orderDirection) throws Exception {
    	SkillTypesCriteria cr=null;
    	if(criteria!=null){
    		cr = new SkillTypesCriteria(criteria);
    	}
    	SessionUtil.setAttribute("SkillTypesCriteria",cr);    //store in session, reuse in later when coming back
        PaginationRequest request = new PaginationRequest(offset,pageSize,orderBy,orderDirection);
        PaginationResponse response = DaoFactory.process(DaoFactory.NAME.SkillTypesDAO, cr,request);
        List lst =  response.convertRowsToClasses(org.mlink.iwm.bean.SkillType.class);
        return new ResponsePage(response.getTotalCount(),lst);
    }

    public String deleteItem(Long skillTypeId) throws Exception{
        String rtn = ITEM_DELETED_MSG;
        PolicySF psf = ServiceLocator.getPolicySFLocal();
        List<Skill> skills=(List<Skill>)psf.getWorkerSkillGivenSkillType(skillTypeId);
        SkillType skillType = psf.get(SkillType.class, skillTypeId);
        //archive if any skill-worker present else delete
        if(!skills.isEmpty()){
        	psf.removeWorkerSkillGivenSkillType(skillTypeId);
            skillType.setArchivedDate(new java.util.Date());
        	psf.update(skillType);
        }else{
        	psf.remove(skillType);
        }
        LookupMgr.reloadCDLV(SkillTypeRef.class);
        return rtn;
    }

    /**
     * Get worker for given id
     * @param id
     * @return
     * @throws Exception
     */
    public org.mlink.iwm.bean.SkillType getItem(Long id) throws Exception{
    	org.mlink.iwm.bean.SkillType form = new org.mlink.iwm.bean.SkillType();
        PolicySF psf = ServiceLocator.getPolicySFLocal();
        SkillType skillType = (SkillType)psf.get(SkillType.class, id);
        CopyUtils.copyProperties(form, skillType);
		return form;
    }

    /**
     * Updates/Creates worker
     * @param map HashMap is analaog to SkillType class
     * @return Optional Business message
     * @throws Exception
     */
    public String saveItem(HashMap map) throws Exception{
    	String rtn = ITEM_SAVED_OK_MSG;
		PolicySF psf = ServiceLocator.getPolicySFLocal();
		SkillType skillType = new SkillType();
		CopyUtils.copyProperties(skillType, map);
        long id = skillType.getId();
        
		if (id > 0) {
			psf.update(skillType);
		} else {
			
			id = psf.create(skillType);
			skillType.setId(id);
			if (id <= 0) {
				logger.error("SkillType not created.");
			} else {
				logger.info("SkillType created sucessfully id: "+id);
			}
		}
		
		if(id>0){
			LookupMgr.reloadCDLV(SkillTypeRef.class);
		}
		return rtn;
    }
    
}
