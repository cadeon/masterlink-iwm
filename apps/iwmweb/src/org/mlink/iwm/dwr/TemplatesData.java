package org.mlink.iwm.dwr;

import java.util.HashMap;
import java.util.List;

import org.mlink.iwm.access3.ServiceLocator;
import org.mlink.iwm.bean.ResponsePage;
import org.mlink.iwm.dao.DaoFactory;
import org.mlink.iwm.dao.DataSearchCriteria;
import org.mlink.iwm.dao.PaginationRequest;
import org.mlink.iwm.dao.PaginationResponse;
import org.mlink.iwm.entity3.ObjectDataDefinition;
import org.mlink.iwm.session.PolicySF;
import org.mlink.iwm.util.CopyUtils;

/**
 * User: andrei
 * Date: Oct 30, 2006
 */
public class TemplatesData implements MaintainanceTable{

    public ResponsePage getData(HashMap criteria, int offset, int pageSize, String orderBy, String orderDirection) throws Exception {
        DataSearchCriteria cr = new DataSearchCriteria(criteria);
        PaginationRequest request = new PaginationRequest(offset,pageSize,orderBy,orderDirection);
        PaginationResponse response = DaoFactory.process(DaoFactory.NAME.TemplateDataDAO,cr,request);
        List lst =  response.convertRowsToClasses(org.mlink.iwm.bean.TemplateData.class);
        return new ResponsePage(response.getTotalCount(),lst);
    }

    public String deleteItem(Long itemId) throws Exception {
        String rtn = ITEM_DELETED_MSG;
        PolicySF policySF = ServiceLocator.getPolicySFLocal( );
        policySF.removeObjectDataDefinition(itemId);
        return rtn;
    }

    public org.mlink.iwm.bean.TemplateData getItem(Long itemId) throws Exception {
    	org.mlink.iwm.bean.TemplateData form = new org.mlink.iwm.bean.TemplateData();
        PolicySF policySF = ServiceLocator.getPolicySFLocal( );
        ObjectDataDefinition vo = policySF.get(ObjectDataDefinition.class, itemId);
        CopyUtils.copyProperties(form,vo);
        form.setObjectDefId(Long.toString(vo.getObjectDefinition().getId()));
        return form;
    }

    public String saveItem(HashMap bean) throws Exception {
        PolicySF policySF = ServiceLocator.getPolicySFLocal( );
        ObjectDataDefinition vo = new ObjectDataDefinition();
        CopyUtils.copyProperties(vo,bean);
        //vo.setObjectDefinition(policySF.get(ObjectDefinition.class, Long.parseLong((String)bean.get("objectDefId"))));
        if(vo.getId() > 0){
            policySF.updateObjectDataDefinition(vo, Long.parseLong((String)bean.get("objectDefId")));
        }else{
            policySF.createObjectDataDefinition(vo, Long.parseLong((String)bean.get("objectDefId")));
        }
        return null;
    }
}
