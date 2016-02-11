package org.mlink.iwm.dwr;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.mlink.iwm.access3.ServiceLocator;
import org.mlink.iwm.bean.ResponsePage;
import org.mlink.iwm.dao.DaoFactory;
import org.mlink.iwm.dao.PaginationRequest;
import org.mlink.iwm.dao.PaginationResponse;
import org.mlink.iwm.dao.ProjectsCriteria;
import org.mlink.iwm.entity3.Project;
import org.mlink.iwm.exception.BusinessException;
import org.mlink.iwm.lookup.LookupMgr;
import org.mlink.iwm.lookup.ProjectRef;
import org.mlink.iwm.lookup.ProjectStatusRef;
import org.mlink.iwm.session.ControlSF;
import org.mlink.iwm.util.Config;
import org.mlink.iwm.util.Constants;
import org.mlink.iwm.util.CopyUtils;

import uk.ltd.getahead.dwr.WebContextFactory;

/**
 * Note: throwing Exceptions outside of methods allows use of DWR error handling (alerts in UI)
 * Note DWR does not support method oveloading well. Avoid that!
 */


public class Projects implements MaintainanceTable{
    private static final Logger logger = Logger.getLogger(Projects.class);

    public ResponsePage getData(HashMap criteria, int offset, int pageSize, String orderBy, String orderDirection) throws Exception {
        ProjectsCriteria cr = new ProjectsCriteria(criteria);
        SessionUtil.setAttribute("ProjectsCriteria",cr);    //store in session, reuse in later when coming back
        PaginationRequest request = new PaginationRequest(offset,pageSize,orderBy,orderDirection);
        PaginationResponse response = DaoFactory.process(DaoFactory.NAME.ProjectsDAO,cr,request);
        List lst =  response.convertRowsToClasses(org.mlink.iwm.bean.Project.class);
        return new ResponsePage(response.getTotalCount(),lst);
    }


    public String deleteItem(Long projectId) throws Exception{
        String rtn = ITEM_DELETED_MSG;
        ControlSF csf = ServiceLocator.getControlSFLocal( );
		Project project = new Project();
		project.setId(projectId);
		csf.remove(project);
		LookupMgr.reloadCDLV(ProjectRef.class);
        return rtn;
    }

    /**
     * Get project for given projectId
     * @param projectId
     * @return
     * @throws Exception
     */
    public org.mlink.iwm.bean.Project getItem(Long projectId) throws Exception{
    	org.mlink.iwm.bean.Project form = new org.mlink.iwm.bean.Project();
        ControlSF csf = ServiceLocator.getControlSFLocal( );
        Project vo = csf.get(Project.class, projectId);
        CopyUtils.copyProperties(form,vo);
        
        SimpleDateFormat df = new SimpleDateFormat(Config.getProperty(Config.TIME_PATTERN));
        Date createdDt = vo.getCreatedDate();
        if(createdDt!=null){
        	form.setCreatedDate(df.format(createdDt));
        }
        return form;
    }

    /**
     * Starts the project by changing states of the project jobs
     * @param projectId
     * @return
     * @throws Exception
     */
    public String start(Long projectId) throws Exception{
        ControlSF csf = ServiceLocator.getControlSFLocal( );
        String rtn = ITEM_SAVED_OK_MSG;
        try {
            csf.startProject(projectId);
        } catch (BusinessException e) {
            rtn = e.getMessage();
        }
        return rtn;
    }

    /** Cancels the project and its jobs
     * @param projectId
     * @return
     * @throws Exception
     */
    public String cancel(Long projectId) throws Exception{
        ControlSF csf = ServiceLocator.getControlSFLocal( );
        String rtn = ITEM_SAVED_OK_MSG;
        try {
            csf.cancelProject(projectId);
        } catch (BusinessException e) {
            rtn = e.getMessage();
        }
        return rtn;
    }

    /**
     * Updates/Creates project
     * @param map HashMap is analalog to project class
     */
    public String saveItem(HashMap map) throws Exception{
        ControlSF csf = ServiceLocator.getControlSFLocal( );
        Project project = new Project();
        String createdDt = (String)map.get("createdDate");
        map.put("createdDate", "");
        
        CopyUtils.copyProperties(project,map);
        String rtn = ITEM_SAVED_OK_MSG;
        if(project.getId() > 0){
        	SimpleDateFormat df = new SimpleDateFormat(Config.getProperty(Config.TIME_PATTERN));
        	if(createdDt!=null && createdDt.length()>0){
        		project.setCreatedDate(df.parse(createdDt));
        	}
            csf.update(project);
        }else{
        	project.setStatusId(ProjectStatusRef
					.getIdByCode(ProjectStatusRef.PREPARING));
        	project.setCreatedDate(new java.sql.Timestamp(System.currentTimeMillis()));
        	String createdBy = WebContextFactory.get().getHttpServletRequest().getUserPrincipal().getName();
            project.setCreatedBy(createdBy);
            csf.create(project);
            LookupMgr.reloadCDLV(ProjectRef.class);
        }
        return rtn;
    }

    public String createProjectFromStencil(Long projectStencilId) throws Exception{
        ControlSF csf = ServiceLocator.getControlSFLocal( );
        String rtn;
        try{
        	String createdBy = WebContextFactory.get().getHttpServletRequest().getUserPrincipal().getName();
    		long projectId = csf.createProjectFromStencil(projectStencilId, createdBy);
            rtn = "New Project " + projectId + " created";
        } catch(BusinessException e){
            rtn = e.getMessage();
        }
        return rtn;
    }
}
