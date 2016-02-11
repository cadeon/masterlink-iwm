package org.mlink.iwm.dwr;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.mlink.iwm.access3.ServiceLocator;
import org.mlink.iwm.bean.ResponsePage;
import org.mlink.iwm.bean.ServicePlan;
import org.mlink.iwm.bean.Template;
import org.mlink.iwm.dao.DaoFactory;
import org.mlink.iwm.dao.PaginationRequest;
import org.mlink.iwm.dao.PaginationResponse;
import org.mlink.iwm.dao.SearchCriteria;
import org.mlink.iwm.entity3.InventoryTrace;
import org.mlink.iwm.entity3.ObjectClassification;
import org.mlink.iwm.entity3.ObjectDefinition;
import org.mlink.iwm.entity3.User;
import org.mlink.iwm.exception.BusinessException;
import org.mlink.iwm.lookup.LookupMgr;
import org.mlink.iwm.lookup.ObjectClassificationRef;
import org.mlink.iwm.lookup.OptionItem;
import org.mlink.iwm.lookup.TargetClassRef;
import org.mlink.iwm.lookup.TreeOptionItem;
import org.mlink.iwm.session.PolicySF;
import org.mlink.iwm.util.CopyUtils;
import org.mlink.iwm.util.DBAccess;

import uk.ltd.getahead.dwr.WebContextFactory;

/**
 * User: andrei
 * Date: Oct 19, 2006
 */
public class Templates implements MaintainanceTable{

    public ResponsePage getData(HashMap criteria, int offset, int pageSize, String orderBy, String orderDirection) throws Exception {
        SearchCriteria cr = new SearchCriteria(criteria);
        SessionUtil.setAttribute("TemplatesCriteria",cr);
        return getData(cr,offset,pageSize,orderBy,orderDirection);
    }

    private ResponsePage getData(SearchCriteria cr, int offset, int pageSize, String orderBy, String orderDirection) throws Exception{
        PaginationRequest request = new PaginationRequest(offset,pageSize,orderBy,orderDirection);
        PaginationResponse response = DaoFactory.process(DaoFactory.NAME.TemplatesDAO,cr,request);
        List lst =  response.convertRowsToClasses(Template.class);
        return new ResponsePage(response.getTotalCount(),lst);
    }

    public String deleteItem(Long itemId) throws Exception {
        String rtn = ITEM_DELETED_MSG;
        try{
            PolicySF policySF = ServiceLocator.getPolicySFLocal( );
            policySF.removeObjectDefinition(itemId);
        }catch(BusinessException be){
            rtn = be.getMessage();
        }
        return rtn;
    }

    public Template getItem(Long itemId) throws Exception {
    	Template form = new Template();
        PolicySF policySF = ServiceLocator.getPolicySFLocal( );
        ObjectDefinition vo = policySF.get(ObjectDefinition.class, itemId);
        BeanUtils.copyProperties(form,vo);
        form.getServicePlan().decodePlanData(vo.getPlan());
        return form;
    }

    public String getUCCode() throws Exception {
    	String sql = "SELECT UC_CODE_SEQ.NEXTVAL from Dual";
    	String UCCode;
    	String UCseq;
    	Connection conn= DBAccess.getDBConnection();
    	Statement stmt= conn.createStatement();
    	ResultSet rs = stmt.executeQuery(sql);
    	rs.next();
    	UCseq=rs.getString("NEXTVAL");
    	DBAccess.close(rs, stmt, conn);
    	while (UCseq.length() < 5) {
    		UCseq="0"+UCseq;
    	}
    	
    	UCCode= "UC-" + UCseq;
    	return UCCode;
    }
    
    public String persistInventory(HashMap map) throws Exception {
    	String rtn = ITEM_SAVED_OK_MSG;
    	PolicySF policySF = ServiceLocator.getPolicySFLocal( );
    	Long id = Long.parseLong((String)map.get("id"));
	    Long presentInventory = Long.parseLong((String)map.get("presentInventory"));
	    Long deltaInventory = Long.parseLong((String)map.get("deltaInventory"));
	    Long newInventory = presentInventory + deltaInventory;
	    if(newInventory.longValue()<0){
	    	rtn="Save unsuccessfull. Inventory level cannot go less than 0.";
	    }else{
	    	ObjectDefinition vo = policySF.get(ObjectDefinition.class, id);
    		vo.setPresentInventory(newInventory);
            policySF.updateObjectDefinition(vo);
        
            InventoryTrace it = new InventoryTrace();
            it.setInventoryDate(new Date());
            it.setInventory(deltaInventory.intValue());
            it.setObjectDefId(id);
            
            String userName = WebContextFactory.get().getHttpServletRequest().getUserPrincipal().getName();
            PolicySF psf = ServiceLocator.getPolicySFLocal();
    		User user = psf.getUserByName(userName);
    		if(user.getPerson()!=null){
    			it.setPersonId(user.getPerson().getId());
    		}
    		it.setUserName(userName);
    		psf.create(it);
	    }
    	return rtn;
    }
    
    /**
     * @param map
     * @throws Exception
     */
    public String saveItem(HashMap map) throws Exception {
    	String rtn = ITEM_SAVED_OK_MSG;
		PolicySF policySF = ServiceLocator.getPolicySFLocal( );
    	ServicePlan form = new ServicePlan(map);
    	ObjectDefinition vo;
    	String idStr = (String)map.get("id");
    	
    	//if edit save only Calendar/Plan info
    	if(idStr!=null && idStr.trim().length()>0){
    		vo = policySF.get(ObjectDefinition.class, Long.parseLong(idStr));
    		vo.setPlan(form.codePlanData());
            policySF.updateObjectDefinition(vo);
        }else{
        	ObjectClassification cvo = new ObjectClassification();
    		//copy the data where it needs to go
    		CopyUtils.copyProperties(cvo,map);
    		policySF.validateObjectClassification(cvo);
    		policySF.create(cvo);	
    		LookupMgr.reloadCDLV(ObjectClassificationRef.class);
    		TargetClassRef targetClassRef = TargetClassRef.getInstance();
    		
    		Object value = cvo.getId();
            Object label = "[" + cvo.getCode() + "] " + cvo.getDescription();
            Object parent = cvo.getParentId();
            Object schema = cvo.getSchemaId();
            Object code = cvo.getCode();
            Object abbr = cvo.getAbbr();
            Long parentId = parent==null?null:(new Long(parent.toString()));
            OptionItem item = new TreeOptionItem(Long.valueOf(value.toString()),label.toString(), parentId, Integer.valueOf(schema.toString()));
            item.addProperty("code",code);
            item.addProperty("abbr",abbr);
            targetClassRef.addOption(item);
    	
            vo = new ObjectDefinition();
            CopyUtils.copyProperties(vo,form);
            vo.setPlan("1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,0,0"); //Everything-enabled plan.
        	vo.setClassId(cvo.getId());
            policySF.create(vo);
        }
        return rtn;
    }
}
