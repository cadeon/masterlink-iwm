package org.mlink.iwm.jms;

import org.mlink.iwm.access3.ServiceLocator;
import org.mlink.iwm.entity3.TaskGroupDefinition;
import org.mlink.iwm.rules.DeletionVisitor;
import org.mlink.iwm.session.ControlSF;


/**
 * Created by IntelliJ IDEA.
 * User: andreipovodyrev
 * Date: Jul 5, 2007
 */
public class DeleteTaskGroupTemplate implements Command {

    private Long taskGroupDefId;

    public DeleteTaskGroupTemplate(Long taskGroupDefId) {
        this.taskGroupDefId = taskGroupDefId;
    }


    public void execute() throws Exception{
    	ControlSF csf = ServiceLocator.getControlSFLocal();
        DeletionVisitor.delete(csf.get(TaskGroupDefinition.class, taskGroupDefId));
    }
}
