package org.mlink.iwm.dwr;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.mlink.iwm.access3.ServiceLocator;
import org.mlink.iwm.bean.ResponsePage;
import org.mlink.iwm.bean.ServicePlan;
import org.mlink.iwm.dao.DaoFactory;
import org.mlink.iwm.dao.PaginationRequest;
import org.mlink.iwm.dao.PaginationResponse;
import org.mlink.iwm.dao.TasksCriteria;
import org.mlink.iwm.entity3.ObjectEntity;
import org.mlink.iwm.entity3.Task;
import org.mlink.iwm.session.ImplementationSF;
import org.mlink.iwm.session.PolicySF;
import org.mlink.iwm.util.Config;
import org.mlink.iwm.util.Constants;
import org.mlink.iwm.util.CopyUtils;

/**
 * User: andrei
 * Date: Oct 30, 2006
 */
public class ObjectTasks implements MaintainanceTable{
    private static final Logger logger = Logger.getLogger(ObjectTasks.class);

    public ResponsePage getData(HashMap criteria, int offset, int pageSize, String orderBy, String orderDirection) throws Exception {
        TasksCriteria cr = new TasksCriteria(criteria);
        PaginationRequest request = new PaginationRequest(offset,pageSize,orderBy,orderDirection);
        PaginationResponse response = DaoFactory.process(DaoFactory.NAME.ObjectTasksDAO,cr,request);
        List lst =  response.convertRowsToClasses(org.mlink.iwm.bean.ObjectTask.class);
        return new ResponsePage(response.getTotalCount(),lst);
    }

    public String deleteItem(Long itemId) throws Exception {
        String rtn = ITEM_DELETED_MSG;
        ImplementationSF isf = ServiceLocator.getImplementationSFLocal();
        Task task = new Task();
		task.setId(itemId);
		isf.remove(task);
        return rtn;
    }

    public org.mlink.iwm.bean.ObjectTask getItem(Long itemId) throws Exception {
    	org.mlink.iwm.bean.ObjectTask form = new org.mlink.iwm.bean.ObjectTask();
        ImplementationSF isf = ServiceLocator.getImplementationSFLocal();
        Task vo = isf.get(Task.class, itemId);
        CopyUtils.copyProperties(form,vo);
        form.setObjectId(Long.toString(vo.getObject().getId()));
        Long orgId= vo.getOrganizationId();
        if(orgId!=null){
        	form.setOrganizationId(Long.toString(orgId));
        }
        form.setDescription(vo.getTaskDescription());
        form.getServicePlan().decodePlanData(vo.getPlan());
        SimpleDateFormat df = new SimpleDateFormat(Config.getProperty(Config.DATE_PATTERN));
        Date startDt = vo.getStartDate();
        if(startDt!=null){
        	form.setStartDate(df.format(startDt));
        }
        return form;
    }

    public String saveItem(HashMap map) throws Exception {
        String rtn = ITEM_SAVED_OK_MSG;
        ImplementationSF isf = ServiceLocator.getImplementationSFLocal();
        PolicySF policySF = ServiceLocator.getPolicySFLocal();
        Task vo = new Task();
        CopyUtils.copyProperties(vo,map);
        ObjectEntity oe = isf.get(ObjectEntity.class, Long.parseLong((String)map.get("objectId")));
        vo.setObject(oe);
        vo.setTaskDescription((String)map.get("description"));
        
        Date now = new Date();
        if(vo.getId() > 0){
        	//this is an update, grab plan from db
        	Task oldVo = isf.get(Task.class, vo.id);
        	vo.setPlan(oldVo.getPlan());
        	//Always make sure the task org is set properly.
        	vo.setOrganizationId(vo.getObject().getOrganizationId());
        	isf.update(vo);
		}else{
			if(new Integer(1).equals(vo.getActive())){
        		vo.setStartDate(now);
        	}
			ServicePlan newPlan = new ServicePlan(true);
			vo.setPlan(newPlan.codePlanData());
            policySF.addDefaultActions(vo);
            //Set org
            vo.setOrganizationId(vo.getObject().getOrganizationId());
            isf.create(vo);
        }
        return rtn;
    }

    public String saveCalendar(HashMap map) throws Exception {
        ServicePlan form = new ServicePlan(map);
        ImplementationSF isf = ServiceLocator.getImplementationSFLocal();
        Task vo = isf.get(Task.class, Long.parseLong((String)map.get("id")));
        vo.setPlan(form.codePlanData());
        //CopyUtils.copyProperties(vo,map);
        String rtn=ITEM_SAVED_OK_MSG;
        isf.update(vo);
        return rtn;
    }
}
