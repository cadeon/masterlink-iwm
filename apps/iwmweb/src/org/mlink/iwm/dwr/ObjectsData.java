package org.mlink.iwm.dwr;

import java.util.HashMap;
import java.util.List;

import org.mlink.iwm.access3.ServiceLocator;
import org.mlink.iwm.bean.ResponsePage;
import org.mlink.iwm.dao.DaoFactory;
import org.mlink.iwm.dao.DataSearchCriteria;
import org.mlink.iwm.dao.PaginationRequest;
import org.mlink.iwm.dao.PaginationResponse;
import org.mlink.iwm.entity3.ObjectData;
import org.mlink.iwm.entity3.ObjectDataDefinition;
import org.mlink.iwm.entity3.ObjectEntity;
import org.mlink.iwm.session.ImplementationSF;
import org.mlink.iwm.util.CopyUtils;

/**
 * User: andrei
 * Date: Oct 30, 2006
 */
public class ObjectsData implements MaintainanceTable{

    public ResponsePage getData(HashMap criteria, int offset, int pageSize, String orderBy, String orderDirection) throws Exception {
        DataSearchCriteria cr = new DataSearchCriteria(criteria);
        PaginationRequest request = new PaginationRequest(offset,pageSize,orderBy,orderDirection);
        PaginationResponse response = DaoFactory.process(DaoFactory.NAME.ObjectDataDAO,cr,request);
        List lst =  response.convertRowsToClasses(org.mlink.iwm.bean.ObjectData.class);
        return new ResponsePage(response.getTotalCount(),lst);
    }

    public String deleteItem(Long itemId) throws Exception {
        String rtn = ITEM_DELETED_MSG;
        ImplementationSF isf = ServiceLocator.getImplementationSFLocal();
        ObjectData od = new ObjectData();
		od.setId(itemId);
		isf.remove(od);
        return rtn;
    }

    public org.mlink.iwm.bean.ObjectData getItem(Long itemId) throws Exception {
    	org.mlink.iwm.bean.ObjectData form = new org.mlink.iwm.bean.ObjectData();
        ImplementationSF isf = ServiceLocator.getImplementationSFLocal();
        ObjectData vo = isf.get(ObjectData.class, itemId);
        CopyUtils.copyProperties(form,vo);
        form.setObjectId(Long.toString(vo.getObject().getId()));
        form.setDataDefId(Long.toString(vo.getObjectDataDef().getId()));
        return form;
    }

    public String saveItem(HashMap bean) throws Exception {
        ImplementationSF isf = ServiceLocator.getImplementationSFLocal();
        ObjectData vo = new ObjectData();
        CopyUtils.copyProperties(vo,bean);
        ObjectEntity oe = isf.get(ObjectEntity.class, Long.parseLong((String)bean.get("objectId")));
        vo.setObject(oe);
        if(vo.getId() > 0){
        	Long dataDefId = Long.parseLong((String)bean.get("dataDefId"));
            vo.setObjectDataDef(new ObjectDataDefinition(dataDefId));
            isf.update(vo);
        }else{
        	ObjectDataDefinition vo1 = new ObjectDataDefinition();
        	CopyUtils.copyProperties(vo1,bean);
        	vo1.setObjectDefinition(oe.getObjectDefinition());
        	vo.setObjectDataDef(vo1);
        	isf.create(vo1);
            isf.create(vo);
        }
        return null;
    }
}
