package org.mlink.iwm.dwr;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.mlink.iwm.access3.ServiceLocator;
import org.mlink.iwm.bean.ResponsePage;
import org.mlink.iwm.dao.DAOResponse;
import org.mlink.iwm.dao.DaoFactory;
import org.mlink.iwm.dao.PaginationRequest;
import org.mlink.iwm.dao.PaginationResponse;
import org.mlink.iwm.dao.ProjectStencilsCriteria;
import org.mlink.iwm.dao.SearchCriteria;
import org.mlink.iwm.entity3.Sequence;
import org.mlink.iwm.session.ImplementationSF;
import org.mlink.iwm.util.Config;
import org.mlink.iwm.util.CopyUtils;

import uk.ltd.getahead.dwr.WebContextFactory;


public class ProjectStencils implements MaintainanceTable{
    private static final Logger logger = Logger.getLogger(ProjectStencils.class);

    public ResponsePage getData(HashMap criteria, int offset, int pageSize, String orderBy, String orderDirection) throws Exception {
        ProjectStencilsCriteria cr = new ProjectStencilsCriteria(criteria);
        SessionUtil.setAttribute("ProjectStencilsCriteria",cr);    //store in session, reuse in later when coming back
        PaginationRequest request = new PaginationRequest(offset,pageSize,orderBy,orderDirection);
        PaginationResponse response = DaoFactory.process(DaoFactory.NAME.ProjectStencilsDAO,cr,request);
        List lst =  response.convertRowsToClasses(org.mlink.iwm.bean.ProjectStencil.class);
        return new ResponsePage(response.getTotalCount(),lst);
    }

    /**
     * Get projects created of off the given project stencil
     * @param criteria
     * @return
     * @throws Exception
     */
    public ResponsePage getHistory(HashMap criteria) throws Exception {
        SearchCriteria cr = new SearchCriteria(criteria);  ////projectStencilId
        DAOResponse response = DaoFactory.process(DaoFactory.NAME.ProjectStencilHistoryDAO,cr);
        List lst =  response.convertRowsToClasses(org.mlink.iwm.bean.Project.class);
        return new ResponsePage(lst.size(),lst);
    }

    public String deleteItem(Long projectStencilId) throws Exception{
        String rtn = ITEM_DELETED_MSG;
        ImplementationSF isf = ServiceLocator.getImplementationSFLocal();
        org.mlink.iwm.entity3.Sequence ps = isf.get(org.mlink.iwm.entity3.Sequence.class, projectStencilId);
        isf.remove(ps);
        return rtn;
    }

    public org.mlink.iwm.bean.ProjectStencil getItem(Long projectStencilId) throws Exception{
    	org.mlink.iwm.bean.ProjectStencil form = new org.mlink.iwm.bean.ProjectStencil();
        ImplementationSF isf = ServiceLocator.getImplementationSFLocal();
        Sequence vo = isf.get(Sequence.class, projectStencilId);
        CopyUtils.copyProperties(form,vo);
        SimpleDateFormat df = new SimpleDateFormat(Config.getProperty(Config.TIME_PATTERN));
        Date createDt = vo.getCreatedDate();
        if(createDt!=null){
        	form.setCreatedDate(df.format(createDt));
        }
        return form;
    }

    /**
     *
     * @param map
     * @return Optional Business message
     * @throws Exception
     */
    public String saveItem(HashMap map) throws Exception{
        ImplementationSF isf = ServiceLocator.getImplementationSFLocal();
        Sequence vo = new Sequence();
        String createDt = (String)map.get("createdDate");
        map.put("createdDate", "");
        
        CopyUtils.copyProperties(vo,map);
        String rtn = ITEM_SAVED_OK_MSG;
        if(vo.getId() > 0){
        	SimpleDateFormat df = new SimpleDateFormat(Config.getProperty(Config.TIME_PATTERN));
        	if(createDt!=null && createDt.length()>0){
        		vo.setCreatedDate(df.parse(createDt));
        	}
        	
            isf.update(vo);
        }else{
        	vo.setCreatedDate(new java.sql.Timestamp(System.currentTimeMillis()));
        	String createdBy = WebContextFactory.get().getHttpServletRequest().getUserPrincipal().getName();
        	vo.setCreatedBy(createdBy);
            isf.create(vo);
        }
        return rtn;
    }
}
