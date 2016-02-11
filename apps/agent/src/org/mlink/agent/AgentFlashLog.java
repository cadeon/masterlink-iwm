/**
 * Created by IntelliJ IDEA.
 * User: andreipovodyrev
 * Date: Jul 13, 2007
 * Time: 2:03:28 PM
 * To change this template use File | Settings | File Templates.
 */
package org.mlink.agent;

import org.mlink.iwm.util.FlashLog;

public class AgentFlashLog {
    private static AgentFlashLog ourInstance = new AgentFlashLog();
    private static FlashLog log = new FlashLog();

    public static AgentFlashLog getInstance() {
        return ourInstance;
    }

    private AgentFlashLog() {
    }

    public synchronized void add(String s){
        log.log(s);
    }

    public synchronized void clear(){
        log.clear();
    }

    public String toString() {
        return log.getLog();
    }
}
