package org.mlink.iwm.rules;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.mlink.iwm.access3.ServiceLocator;
import org.mlink.iwm.entity3.Action;
import org.mlink.iwm.entity3.ActionDefinition;
import org.mlink.iwm.entity3.Job;
import org.mlink.iwm.entity3.ObjectData;
import org.mlink.iwm.entity3.ObjectDefinition;
import org.mlink.iwm.entity3.ObjectEntity;
import org.mlink.iwm.entity3.Project;
import org.mlink.iwm.entity3.Task;
import org.mlink.iwm.entity3.TaskDefinition;
import org.mlink.iwm.entity3.TaskGroup;
import org.mlink.iwm.exception.IWMException;
import org.mlink.iwm.lookup.ActiveStatusRef;
import org.mlink.iwm.lookup.TargetClassRef;
import org.mlink.iwm.session.ControlSF;
import org.mlink.iwm.session.ImplementationSF;
import org.mlink.iwm.session.PolicySF;
import org.mlink.iwm.util.Constants;
import org.mlink.iwm.util.EqualsUtils;

/**
 * User: Andrei
 * Date: Mar 6, 2005
 * Loops through every ObjectEntity and its children inits initcode, active and custom fields
 * This class should be used for new data migrated from a V1 schema to V2 schema
 */
public class MigratedDataCleaner  extends Thread implements Serializable{

    private static final Logger logger = Logger.getLogger(MigratedDataCleaner.class);
    Iterator it = null;
    //StringBuffer report = new StringBuffer();
    int totalCount = 0;
    int invalidTaskDefCount = 0;
    int invalidTaskCount = 0;
    int taskStatusChangedCount = 0;

    PrintWriter out;
    File file;

    public static final String MARKER = "(*)";


    public String getReportFileName() {
        return file.getAbsolutePath();
    }

    public MigratedDataCleaner() {
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(System.currentTimeMillis());
            String filename = "MigratedDataCleanup_" +
                    cal.get(Calendar.DAY_OF_MONTH) + "_" +
                    (1+cal.get(Calendar.MONTH)) + "_" +
                    cal.get(Calendar.YEAR);
            filename=filename+".log";
            file = new File(filename);
    }



    private void done() {
        StringBuffer summary = new StringBuffer();
        summary.append("\n invalidTaskDefCount="+invalidTaskDefCount);
        summary.append("\n invalidTaskCount="+invalidTaskCount);
        summary.append("\n taskStatusChangedCount="+taskStatusChangedCount);
        out.print(summary);
        out.flush();
        out.close();
    }
    public void init() {
        try{
            out = new PrintWriter(new FileWriter(file));
        }catch(IOException io){
            io.printStackTrace();
        }
    }


    public void run(){
        init();
        long t0 = System.currentTimeMillis();
        prepareIterator();
        PolicySF psf = ServiceLocator.getPolicySFLocal();
        while(hasNext()){
            try{
                psf.cleanNext(this);
            }catch(Exception e){
                e.printStackTrace();
            }
        }

        long t1 = System.currentTimeMillis();
        logger.debug("cleanup completed in " + (t1-t0)/1000L/60 + " mins");
        out.println("cleanup completed in " + (t1-t0)/1000L/60 + " mins");
        out.println("Report file: " + getReportFileName());
        logger.debug(getReportFileName());
        done();
    }


    public void destroy() {
        super.destroy();
        logger.debug("I am finished and being destroyed. Bye!");
    }

    /**
     * The only actions is to check for childless taskDefs and create default actions for them
     * @param objectDef
     */
    public int clean(ObjectDefinition objectDef) {
    	PolicySF policy = ServiceLocator.getPolicySFLocal();
        String tmp = "Cleaning ObjectDef " +objectDef.getClassId();
        logger.debug(tmp);
        out.print(tmp);
        int count=0;
        try{
            Collection<TaskDefinition> taskDefs = objectDef.getTaskDefs();
            for (TaskDefinition taskDef : taskDefs) {
                Collection<ActionDefinition> actionDefs = policy.getActionDefs(taskDef);
                if(actionDefs.size()==0){ //TaskDef must have at least one Action
                    //create a default actions
                    String msg = "\nTaskDef ID="+taskDef.getId() + " " + taskDef.getTaskDescription() + "(" + TargetClassRef.getLabel(objectDef.getClassId()) + ")" +
                            " has no actionDefs. Adding default and propagating to non custom tasks";
                    out.println(msg);
                    logger.debug(msg);
                    invalidTaskDefCount++;
                    policy.addDefaultActions(taskDef);
                    policy.update(taskDef);
                    //propagates newly created default actions to all non-custom tasks
                    for (Iterator iterator = actionDefs.iterator(); iterator.hasNext();) {
                        ActionDefinition actionDef = (ActionDefinition) iterator.next();
                        PropagationVisitor.propagateCreate(actionDef);
                    }
                }
                count++;
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        out.print(" .... done. Incremental count " + totalCount + "\n");

        return count;

    }
    public int clean(Project project) {
    	ControlSF csf = ServiceLocator.getControlSFLocal( );
        String tmp = "Cleaning Project " +project.getId();
        logger.debug(tmp);
        out.print(tmp);
        int count = 0;
        try{
            Collection<Job> jobs = csf.getJobs(project);
            for (Job o : jobs) {
                if(o.getSequenceLevel()==null){
                    o.setSequenceLevel(new Integer(1));
                    logger.debug("setting sequence level to 1 for project job " + o.getId() + " project " +project.getName());
                }
            }

        }catch(Exception e){
            e.printStackTrace();
        }
        out.print(" .... done. Incremental count " + totalCount + "\n");

        return count;
    }
    /**
     * To clean the data verify
     * 1. that all tasks ahs actions. For those which don't add a default one
     * 2. set Task CUSTOM flag with CustomizationVisitor (CUSTOM flag for actions is also set)
     * 3. update Task Active status (see Task.ejbStore())
     * 4. set ObjectData CUSTOM flag with CustomizationVisitor
     * 5. set TaskGroup CUSTOM flag with CustomizationVisitor
     * 6. set ObjectEntity.ObjectRef
     * @param object
     */
    public int clean(ObjectEntity object) {
        String tmp0 = "Object " +object.getId();
        ImplementationSF isf = ServiceLocator.getImplementationSFLocal();
        ControlSF csf = ServiceLocator.getControlSFLocal( );
        PolicySF psf = ServiceLocator.getPolicySFLocal( );
        
        logger.debug(tmp0);
        //out.print(tmp0);
        int count = 0;
        try{
            Collection<Task> tasks = isf.getTasks(object);
            for (Task task : tasks) {
                count++;
                Integer activeStatus = task.getActive();
                String msg = "Task ID=" +task.getId() + " " + task.getTaskDescription() + "(class=" + TargetClassRef.getLabel(object.getClassId()) + ")";
                Collection<Action> actions = csf.getActions(task);
                count=count+actions.size();

                Integer customFlag =  CustomizationVisitor.visit(task);
                if(Constants.CUSTOMIZED_YES.equals(customFlag)){
                    if(actions.size()==0){
                        //Task must have at least one Action
                        //do it for standalone tasks only
                        //create a default actions
                        out.println(msg + " has no actions. Adding default");
                        logger.debug("\n" +  msg + " has no actions. Adding default");
                        invalidTaskCount++;
                        csf.addDefaultActions(task);
                        //tag the task as modified during migration
                        task.setTaskDescription(task.getTaskDescription()+MARKER);
                    }else if(!CustomizationVisitor.isActionSetIdentical(task)){
                        task.setTaskDescription(task.getTaskDescription()+MARKER);
                        //make standalone
                        task.setTaskDefinition(null);
                        invalidTaskCount++;
                        out.println(msg + ". Converting to from custom to stand-alone -> action sets don't match");
                        logger.debug("\n" +  msg + ". Converting to stand-alone");
                    }
                }else if(Constants.CUSTOMIZED_NO.equals(customFlag) && !CustomizationVisitor.isActionSetIdentical(task)){
                    task.setTaskDescription(task.getTaskDescription()+MARKER);
                    //make standalone
                    task.setTaskDefinition(null); task.setCustom(Constants.CUSTOMIZED_YES);
                    out.println(msg + ". Converting from non-custom to stand-alone -> action sets don't match");
                    logger.debug("\n" +  msg + ". Converting to stand-alone");
                }

                //see if complies with Activation Rules
                //set start date to today on all active (from v1) non-routine tasks. As we know, many tasks in v1 are active with no start date.
                if(EqualsUtils.areEqual(Constants.STATUS_ACTIVE,task.getActive())){
                    psf.makeActive(task);
                }
                if(!task.getActive().equals(activeStatus)){
                    // status changed during cleanup, this needs be reported
                    String tmp = ("\n" +msg + ". Status is changed from " + ActiveStatusRef.getLabel(activeStatus) + " to " + ActiveStatusRef.getLabel(task.getActive()));
                    out.println(tmp);
                    logger.debug(tmp);
                    taskStatusChangedCount++;
                }
                isf.update(task);
            }

            Collection<ObjectData> data = isf.getDatums(object);
            for (ObjectData od : data) {
                CustomizationVisitor.visit(od);
                count++;
            }

            Collection<TaskGroup> groups = isf.getTaskGroups(object);
            for (TaskGroup group : groups) {
                CustomizationVisitor.visit(group);
                count++;
            }

            //ObjectRef is calculated differently in V2. example Meta # All vs Meta.All
            object.setObjectRef(org.mlink.iwm.lookup.TargetClassRef.getAbbr(object.getClassId())+"."+object.getTag());

            //set custom fields for object
            object.updateCustom();
            out.print(tmp0 + " Done. Incremental count " + (totalCount + count) + "\n");
        }catch(Exception e){
            e.printStackTrace();
        }

        return count;


    }

    public void prepareIterator() throws IWMException{
        try{
        	PolicySF policy = ServiceLocator.getPolicySFLocal();
            Collection entities = new ArrayList();
            Collection<ObjectEntity> objectEntities = policy.findAll(ObjectEntity.class);
            Collection<ObjectDefinition> objectDefinitions = policy.findAll(ObjectDefinition.class);

            Collection<Project> projects = policy.findAll(Project.class);
            
            entities.addAll(objectEntities);
            entities.addAll(objectDefinitions);
            entities.addAll(projects);
            it = entities.iterator();
        }catch(Exception e){
            throw new IWMException(e);
        }
    }

    public void prepareIterator2() throws IWMException{
        try{
        	PolicySF policy = ServiceLocator.getPolicySFLocal();
            Collection<ObjectEntity> entities = new ArrayList<ObjectEntity>();
            entities.add(policy.get(ObjectEntity.class, new Long(20000733813L)));
            it = entities.iterator();
        }catch(Exception e){
            throw new IWMException(e);
        }
    }

    public boolean hasNext() {
        return it.hasNext();
    }

    public void cleanNext() {
        int count = 0;
        Object o = it.next();
        if(o instanceof ObjectDefinition){
            ObjectDefinition od = (ObjectDefinition)o;
            count =  clean(od);
            //logger.debug("cleaned ObjectDefinition  " + TargetClassRef.getLabel(od.getClassId()) + " with " + count + " children");
        }
        if(o instanceof ObjectEntity){
            ObjectEntity oe = (ObjectEntity)o;
            count =  clean(oe);
            //logger.debug("cleaned ObjectEntity  class=" + TargetClassRef.getLabel(oe.getClassId()) + " tag="+ oe.getObjectRef() + " with " + count + " children");

        }

        if(o instanceof Project){
            Project p = (Project)o;
            count =  clean(p);

        }
        totalCount  =   totalCount + count + 1;
        logger.debug(" Cleaning data. Incremental count " + totalCount);

        //if(true) throw new IWMException("rollback");

    }

    public int getTotalCount() {
        return totalCount;
    }


    /** task,taskdef migration rules
     Task Def with Action Defs	Complies	None	Migrate unchanged	None
     Task Def with No Action Defs	Not Complies	Needs default actions defs	Create default action defs upon migration	None
     Task Def with Matching Task (instance) and Matching Action (instances)	Complies	None	Migrate unchanged	None
     Task Def with Matching Task (instance) and Mismatching Action (instances)	Not Complies	Actions cannot be customized without first customizing Task	Transform Task (instance) into a Stand Alone task
     Task Def with Matching Task (instance) and No Action (Instances)	Not Complies	Task cannot be instantiated without actions	Create default action defs and inherit to instances upon migration	None
     Tasks (instances) with no matching Task Def 	Complies	None	Migrate unchanged	None
     Task (instance) with no matching Task Def and no Action (instance) 	Not Complies	Task Instances may not exist with no Actions.	Create default actions (instance) and transform into Stand Alone task.
     */


}