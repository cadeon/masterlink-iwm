package org.mlink.iwm.jms;

import org.mlink.iwm.access3.ServiceLocator;
import org.mlink.iwm.session.PolicySF;


/**
 * Created by IntelliJ IDEA.
 * User: andreipovodyrev
 * Date: Jul 5, 2007
 */
public class PropagateTaskGroupTemplate implements Command {
    private Long taskGroupDefId;

    public PropagateTaskGroupTemplate(Long taskGroupDefId) {
        this.taskGroupDefId = taskGroupDefId;
    }


    public void execute() throws Exception{
        PolicySF psf = ServiceLocator.getPolicySFLocal();
        psf.propagateTaskGroupDefinition(taskGroupDefId);
    }
}
