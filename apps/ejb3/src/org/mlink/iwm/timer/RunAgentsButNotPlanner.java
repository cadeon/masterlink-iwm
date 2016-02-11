package org.mlink.iwm.timer;

import org.mlink.iwm.base.BaseAccess;

/**
 * Created by IntelliJ IDEA.
 * User: andreipovodyrev
 * Date: Aug 5, 2007
 */
public class RunAgentsButNotPlanner extends IWMTask {
    private long interval;

    /**
     * Set interval in millisecs
     * @param interval
     */
    public RunAgentsButNotPlanner(long interval) {
        this.interval=interval;
    }

    /**
     * @return def interval in millisecs
     * @return millisec
     */
    public long getInterval() {
        return interval;
    }

    public void execute() throws Exception {
        BaseAccess.getWorldConnection().runAllButPlanner();
    }
}
