package org.mlink.iwm.dwr;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.mlink.iwm.access3.ServiceLocator;
import org.mlink.iwm.bean.ObjectTask;
import org.mlink.iwm.bean.ProjectStencilTask;
import org.mlink.iwm.bean.ResponsePage;
import org.mlink.iwm.dao.DaoFactory;
import org.mlink.iwm.dao.ObjectsCriteria;
import org.mlink.iwm.dao.PaginationRequest;
import org.mlink.iwm.dao.PaginationResponse;
import org.mlink.iwm.dao.TasksCriteria;
import org.mlink.iwm.entity3.ObjectEntity;
import org.mlink.iwm.entity3.TaskSequence;
import org.mlink.iwm.exception.BusinessException;
import org.mlink.iwm.session.ImplementationSF;
import org.mlink.iwm.util.EqualsUtils;

/**
 * User: andrei
 * Date: Feb 12, 2007
 */
public class ProjectStencilTasks extends ObjectTasks{

    public ResponsePage getData(HashMap criteria, int offset, int pageSize, String orderBy, String orderDirection) throws Exception {
        TasksCriteria cr = new TasksCriteria(criteria);
        PaginationRequest request = new PaginationRequest(offset,pageSize,orderBy,orderDirection);
        PaginationResponse response = DaoFactory.process(DaoFactory.NAME.ProjectStencilTasksDAO,cr,request);
        List <ProjectStencilTask>lst =  response.convertRowsToClasses(org.mlink.iwm.bean.ProjectStencilTask.class);
        return new ResponsePage(response.getTotalCount(),lst);
    }

    public ResponsePage getOrderedTasks(Long projectStencilId) throws Exception {
        TasksCriteria cr = new TasksCriteria(); cr.setId(projectStencilId);
        PaginationRequest request = new PaginationRequest("sequenceLevel");
        PaginationResponse response = DaoFactory.process(DaoFactory.NAME.ProjectStencilTasksDAO,cr,request);
        List<ProjectStencilTask> lst =  response.convertRowsToClasses(org.mlink.iwm.bean.ProjectStencilTask.class);
        initDisplayOrderNummbers(lst);        
        SessionUtil.setAttribute("tasksForOrdering",lst);
        return new ResponsePage(response.getTotalCount(),lst);
    }

    /**
     * reorder task sequence based on new sequence number for given task
     * @param displayOrder
     * @param  sequenceNumber
     * @return
     * @throws Exception
     */
    public ResponsePage updateTaskSequence(int displayOrder, int sequenceNumber) throws Exception {
        List <ProjectStencilTask> lst = (List <ProjectStencilTask>) SessionUtil.getAttribute("tasksForOrdering");
        for (ProjectStencilTask item : lst) {
            if(EqualsUtils.areEqual(item.getDisplayOrder(),displayOrder)){
                item.setSequenceLevel(sequenceNumber);
            }
        }
        Collections.sort(lst);
        initDisplayOrderNummbers(lst);
        return new ResponsePage(lst.size(),lst);
    }

    public String saveOrderedTasks() throws Exception {
    	ImplementationSF isf = ServiceLocator.getImplementationSFLocal();
        List <ProjectStencilTask> lst = (List <ProjectStencilTask>) SessionUtil.getAttribute("tasksForOrdering");
        //Collection<TaskSequence> vos = CopyUtils.copyProperties(TaskSequence.class,lst);
        Collection<TaskSequence> vos = new ArrayList<TaskSequence>();
        TaskSequence taskSequence;
        for(ProjectStencilTask pst : lst){
        	taskSequence = isf.get(TaskSequence.class, pst.getTaskSequenceId());
        	taskSequence.setSequenceLevel(pst.getSequenceLevel());
        	vos.add(taskSequence);
        }
        isf.updateTaskSequences(vos);
        SessionUtil.removeAttribute("tasksForOrdering");
        return ITEM_SAVED_OK_MSG;
    }

    public String deleteItem(Long itemId) throws Exception {
        String rtn = ITEM_DELETED_MSG;
        ImplementationSF isf = ServiceLocator.getImplementationSFLocal();
        TaskSequence ts = new TaskSequence(itemId);
        isf.remove(ts);
        return rtn;
    }

    /**
     * Get objects for selection. (First Part of Add Task to Stencil wizard)
     * @param criteria  (classId and locatorId)
     * @param offset
     * @param pageSize
     * @param orderBy
     * @param orderDirection
     * @return
     * @throws Exception
     */
    public ResponsePage getObjects(HashMap criteria, int offset, int pageSize, String orderBy, String orderDirection) throws Exception {
        ObjectsCriteria cr = new ObjectsCriteria(criteria);
        PaginationRequest request = new PaginationRequest(offset,pageSize,orderBy,orderDirection);
        PaginationResponse response = DaoFactory.process(DaoFactory.NAME.ObjectsForSelectDAO,cr,request);
        List lst =  response.convertRowsToClasses(org.mlink.iwm.bean.ObjectInstance.class);
        return new ResponsePage(response.getTotalCount(),lst);
    }

    /**
     * Make object selection for job creation
     * @param objectId
     * @return
     * @throws Exception
     */
    public String selectObject(Long objectId) throws Exception{
        ImplementationSF isf = ServiceLocator.getImplementationSFLocal();
        ObjectEntity vo = isf.get(ObjectEntity.class, objectId);
        SessionUtil.setAttribute("objectSelectedForProjectStencilTasks",vo);
        return ITEM_SAVED_OK_MSG;
    }

    /**
     * get tasks of the seleted object for selection
     * @return
     * @throws Exception
     */
    public ResponsePage getTasks() throws Exception {
        TasksCriteria cr = new TasksCriteria();
        ObjectEntity object = (ObjectEntity) SessionUtil.getAttribute("objectSelectedForProjectStencilTasks");
        cr.setId(object.getId());
        PaginationRequest request = new PaginationRequest("description");
        PaginationResponse response = DaoFactory.process(DaoFactory.NAME.ObjectTasksDAO,cr,request);
        List lst =  response.convertRowsToClasses(org.mlink.iwm.bean.ObjectTask.class);
        SessionUtil.setAttribute("taskSelectedForProjectStencilTasks",new ArrayList<ObjectTask>());
        return new ResponsePage(response.getTotalCount(),lst);
    }

    /**
     * Select task to be assocoated with the project stencil
     * @param taskId
     * @return
     * @throws Exception
     */
    public String selectTask(Long taskId) throws Exception{
        String rtn = ITEM_SAVED_OK_MSG;
        ObjectTasks dwrAction = new ObjectTasks();
        ObjectTask task = dwrAction.getItem(taskId);
        List <ObjectTask> selectedTasks = (List <ObjectTask>)SessionUtil.getAttribute("taskSelectedForProjectStencilTasks");
        selectedTasks.add(task);
        return rtn;
    }

    /**
     * Disgard task previously selected
     * @param taskId
     * @return
     * @throws Exception
     */
    public String unselectTask(Long taskId) throws Exception{
        String rtn = ITEM_SAVED_OK_MSG;
        List <ObjectTask> selectedTasks = (List<ObjectTask>)SessionUtil.getAttribute("taskSelectedForProjectStencilTasks");
        for (Iterator iterator = selectedTasks.iterator(); iterator.hasNext();) {
            ObjectTask task =  (ObjectTask)iterator.next();
            if(String.valueOf(taskId).equals(task.getId())) iterator.remove();
        }
        return rtn;
    }

    /**
     * Persist (add) selected task to Project Stencil
     * @param projectStencilId
     * @return
     * @throws Exception
     */
    public String saveTasks(Long projectStencilId) throws Exception {
        ImplementationSF isf = ServiceLocator.getImplementationSFLocal();
        String rtn = ITEM_SAVED_OK_MSG;
        try{
            List <ObjectTask> selectedTasks = (List<ObjectTask>)SessionUtil.getAttribute("taskSelectedForProjectStencilTasks");
            if(selectedTasks==null || selectedTasks.size()==0){
                throw new BusinessException("Must select at least one task!");
            }
            for (ObjectTask task : selectedTasks) {
                isf.addTaskToSequence(projectStencilId, Long.parseLong(task.getId()));
            }
        } catch (BusinessException e) {
            rtn = e.getMessage();
        }
        return rtn;
    }

    /**
     * same task can be placed in stencil miltiple times. Therefore taskId is not good ID for item in a sequence when users need to reorder sequence. Use list index for as the unique id
     */
    private void initDisplayOrderNummbers(List <ProjectStencilTask> lst){
        for (int i = 0; i < lst.size(); i++) {
            ProjectStencilTask projectStencilTask =  lst.get(i);
            projectStencilTask.setDisplayOrder(i);
        }
    }
}
