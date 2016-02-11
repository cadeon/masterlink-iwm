package org.mlink.iwm.dwr;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.mlink.iwm.access3.ServiceLocator;
import org.mlink.iwm.bean.ResponsePage;
import org.mlink.iwm.dao.DAOResponse;
import org.mlink.iwm.dao.DaoFactory;
import org.mlink.iwm.dao.ObjectsCriteria;
import org.mlink.iwm.dao.PaginationRequest;
import org.mlink.iwm.dao.PaginationResponse;
import org.mlink.iwm.entity3.ObjectClassification;
import org.mlink.iwm.entity3.ObjectDefinition;
import org.mlink.iwm.entity3.ObjectEntity;
import org.mlink.iwm.entity3.ObjectStructHistory;
import org.mlink.iwm.session.ImplementationSF;
import org.mlink.iwm.util.CopyUtils;

/**
 * User: andrei
 * Date: Oct 25, 2006
 */
public class Objects implements MaintainanceTable{

    public ResponsePage getData(HashMap criteria, int offset, int pageSize, String orderBy, String orderDirection) throws Exception {
        ObjectsCriteria cr = new ObjectsCriteria(criteria);
        SessionUtil.setAttribute("ObjectsCriteria",cr);
        PaginationRequest request = new PaginationRequest(offset,pageSize,orderBy,orderDirection);
        PaginationResponse response = DaoFactory.process(DaoFactory.NAME.ObjectsDAO,cr,request);
        List lst =  response.convertRowsToClasses(org.mlink.iwm.bean.ObjectInstance.class);
        return new ResponsePage(response.getTotalCount(),lst);
    }
    
    public ResponsePage getUnattachedData(HashMap criteria) throws Exception {
    	ObjectsCriteria cr = new ObjectsCriteria(criteria);  //locatorId might be present
        DAOResponse response = DaoFactory.process(DaoFactory.NAME.ObjectsDAO, cr);
        List lst =  response.convertRowsToClasses(org.mlink.iwm.bean.ObjectInstance.class);
        return new ResponsePage(lst.size(),lst);
    }
    

    public String deleteItem(Long itemId) throws Exception {
        String rtn = ITEM_DELETED_MSG;
        ImplementationSF isf = ServiceLocator.getImplementationSFLocal();
		isf.removeObject(itemId);
        return rtn;
    }

    public org.mlink.iwm.bean.ObjectInstance getItem(Long itemId) throws Exception {   
    	org.mlink.iwm.bean.ObjectInstance form = new org.mlink.iwm.bean.ObjectInstance();
        ImplementationSF isf = ServiceLocator.getImplementationSFLocal();
        ObjectEntity vo = isf.get(ObjectEntity .class, itemId);
        CopyUtils.copyProperties(form,vo);
       
        form.setObjectId(Long.toString(vo.getId()));
        form.setObjectDefId(Long.toString(vo.getObjectDefinition().getId()));
        form.setClassId(Long.toString(vo.getClassId()));
        form.setHasCustomData(Integer.toString(vo.getHasCustomData()));
        form.setHasCustomTask(Integer.toString(vo.getHasCustomTask()));
        form.setHasCustomTaskGroup(Integer.toString(vo.getHasCustomTaskGroup()));
        return form;
    }

    public String saveItem(HashMap bean) throws Exception {
        ImplementationSF isf = ServiceLocator.getImplementationSFLocal();
        long objectId = Long.parseLong((String)bean.get("objectId"));
        
        ObjectEntity vo;
        Long locatorId=null, parentObjectId=null;
        if(objectId>0){
        	vo = isf.get(ObjectEntity.class, objectId);
        	locatorId=vo.getLocatorId();
        	parentObjectId=vo.getParentObjectId();
        }else{
        	vo = new ObjectEntity();
        }
        
        CopyUtils.copyProperties(vo,bean);
        
        String rtn = ITEM_SAVED_OK_MSG;
        ObjectDefinition od = isf.get(ObjectDefinition.class, Long.parseLong((String)bean.get("objectDefId")));
    	ObjectClassification oc = isf.get(ObjectClassification.class, od.getClassId());
        vo.setObjectRef(oc.getAbbr()+"."+bean.get("tag"));
        if(vo.getId() > 0){
        	isf.updateObjectTasksOrg(vo.getId(), vo.getOrganizationId());
        	isf.update(vo);
        	
        }else{
        	vo.setObjectDefinition(od);
        	vo.setClassId(oc.getId());
        	
        	//Date munging. Taskview doesn't allow loading of object that were started today... so start new objects yesterday.
        	Date today = new Date();
        	Calendar cal = Calendar.getInstance();
        	cal.setTime(today);
        	cal.add(Calendar.DATE, -1);
        	Date yesterday = cal.getTime();
        	
            vo.setCreatedDate(today);
        	vo.setStartDate(yesterday);
        	vo.setHasCustomData(0);
            vo.setHasCustomTask(0);
            vo.setHasCustomTaskGroup(0);
            isf.createObjectEntity(vo);
            isf.updateObjectTasksOrg(vo.getId(), vo.getOrganizationId());
            
        }
        return rtn;
    }
}
