package org.mlink.iwm.struts.form;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.ArrayList;

/**
 * User: andrei
 * Date: Oct 12, 2006
 */
public class BaseForm extends ActionForm {
    private PageContext pageContext = null;
    private String forward;
    private String message;    //used to display js messages

    // below for backward support
    private String dispatch;
    private String  cancelPath;

    private String id;    //generic id used to navigate through pages.
    private String name;    //generic id used to navigate through pages.
    private List <Object> items = new ArrayList <Object>();  // generic List
    private String selectedItemId;    //generic useage for storing id of an elemnet form items collection

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List <Object> getItems() {
        return items;
    }

    public void setItems(List  <Object> items) {
        this.items = items;
    }

    public String getSelectedItemId() {
        return selectedItemId;
    }

    public void setSelectedItemId(String selectedItemId) {
        this.selectedItemId = selectedItemId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getForward() {
        return forward;
    }

    public void setForward(String forward) {
        this.forward = forward;
    }

    public PageContext getPageContext() {
        return pageContext==null?new PageContext(""):pageContext;
    }

    public void setPageContext(PageContext pageContext) {
        this.pageContext = pageContext;
    }
    public void reset(ActionMapping mapping, HttpServletRequest request){
        pageContext = null;
        forward=null;
        message=null;
        taskId=null;
        dispatch=null;
        cancelPath = null;
        id=null;
        projectId=null;
        selectedItemId=null;
        items = new ArrayList <Object>();
    }


    // subclass candidates
    private String taskId;

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    // subclass candidates
    private String projectId;
    public String getProjectId() {
        return projectId;
    }
    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    // below for backward support
    public String getDispatch() {
        return dispatch;
    }

    public void setDispatch(String dispatch) {
        this.dispatch = dispatch;
    }

    public String getCancelPath() {
        return cancelPath;
    }

    public void setCancelPath(String cancelPath) {
        this.cancelPath = cancelPath;
    }

}
