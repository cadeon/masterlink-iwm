package org.mlink.iwm.jms;

import org.mlink.iwm.access3.ServiceLocator;
import org.mlink.iwm.session.PolicySF;

/**
 * Created by IntelliJ IDEA.
 * User: andreipovodyrev
 * Date: Jul 5, 2007
 */
public class PropagateTaskTemplate implements Command {

    private Long taskDefId;

    public PropagateTaskTemplate(Long taskDefId) {
        this.taskDefId = taskDefId;
    }

    public void execute() throws Exception{
        PolicySF psf = ServiceLocator.getPolicySFLocal( );
        psf.propagateTaskDefinition(taskDefId);
    }
}
