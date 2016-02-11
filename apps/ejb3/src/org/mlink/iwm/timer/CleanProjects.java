package org.mlink.iwm.timer;

import org.mlink.iwm.access3.ServiceLocator;
import org.mlink.iwm.session.ControlSF;

/**
 * User: andrei
 * Date: Feb 14, 2007
 */
public class CleanProjects extends IWMTask {

    public void execute() throws Exception{
        ControlSF csf = ServiceLocator.getControlSFLocal();
        csf.completeProjects();
    }

    public long getInterval() {
        //return HOUR*4 + MINUTE*30;// once every 4.5 hours;
        return HOUR*12 ;// once every 12 hours;
    }

}
