package org.mlink.iwm.dwr;

import java.util.HashMap;
import java.util.List;

import org.mlink.iwm.bean.ResponsePage;
import org.mlink.iwm.bean.Template;
import org.mlink.iwm.dao.DAOResponse;
import org.mlink.iwm.dao.DaoFactory;
import org.mlink.iwm.dao.SearchCriteria;

/**
 * User: andrei
 * Date: Jan 9, 2007
 */
public class ObjectCreateWizard implements ReturnCodes{
    public ResponsePage getData(HashMap criteria) throws Exception {
        SearchCriteria cr = new SearchCriteria(criteria);
        DAOResponse response = DaoFactory.process(DaoFactory.NAME.TemplatesForObjectCreateDAO,cr);
        List lst =  response.convertRowsToClasses(org.mlink.iwm.bean.Template.class);
        return new ResponsePage(lst.size(),lst);
    }

    public String selectTemplate(Long templateId) throws Exception{
        Templates dwrAction = new Templates();
        Template template = dwrAction.getItem(templateId);
        SessionUtil.setAttribute("templateForObjectSelect",template);
        return ITEM_SAVED_OK_MSG;
    }

    public String saveItem(HashMap bean) throws Exception {
        Template template = (Template) SessionUtil.getAttribute("templateForObjectSelect");
        bean.put("objectDefId",template.getId());
        Objects dwrAction = new Objects();
        return dwrAction.saveItem(bean);
    }

}
