package org.mlink.iwm.dwr;

import org.apache.log4j.Logger;
import org.mlink.agent.AgentFlashLog;
import org.mlink.iwm.util.StringUtils;

/**
 * Created by IntelliJ IDEA.
 * User: andreipovodyrev
 * Date: Jul 13, 2007
 */
public class AgentToolsLog {
    private static final Logger logger = Logger.getLogger(AgentToolsLog.class);

    public String getLog(){
        return StringUtils.htmlWithBreaks(AgentFlashLog.getInstance().toString());
    }

}
