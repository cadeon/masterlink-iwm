package org.mlink.iwm.timer;

import org.mlink.iwm.base.BaseAccess;

public class RunAgents extends IWMTask {
    private long interval;

    /**
     * Set interval in millisecs
     * @param interval
     */
    public RunAgents(long interval) {
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
        BaseAccess.getWorldConnection().runAll();
    }
}
