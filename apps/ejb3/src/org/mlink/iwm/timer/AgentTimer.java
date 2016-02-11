package org.mlink.iwm.timer;

import java.util.Timer;
import java.util.Date;
import java.util.Calendar;

import org.apache.log4j.Logger;

/**
 * @deprecated  see IWMTimer and IWMTask
 */
public class AgentTimer {
    private static AgentTimer ourInstance ;
    private Timer timer;
    private static final Logger logger = Logger.getLogger(AgentTimer.class);
    private static final long MINUTE = 1000L * 60;
    private static boolean started = false;
    private static int     interval = 0;

    public static synchronized boolean start(int interval) {
        if (interval<=0) {
        	logger.info("Did not start Agent Timer service. Interval is <= 0");
        	started = false;
        	return started;
        }
        logger.info("Starting Agent Timer service. Agents will run every "+ interval +" minutes");
        if(ourInstance==null) ourInstance = new AgentTimer(interval);
        return started;
    }

    public static synchronized boolean stop() {
        logger.info("Stopping Agent Timer service");
        ourInstance.timer.cancel();
        ourInstance = null;
        started = false;
        logger.info("Agent Timer service is stopped");
        return started;
    }

    public static synchronized boolean isStarted(){return started;}
    public static synchronized int     getInterval(){return interval;}
    
    private AgentTimer(int i) {
        timer = new Timer("AgentTimer");
        //run this task immediatly and in period after
        long delay = 1000L*600*2;     // delay 20 min
    	if (i>0)  {
    		timer.schedule(new RunAgents(i), delay, i*MINUTE);
    		started = true;
    		interval = i;
    	}
    }


    /*private AgentTimer(int i) {
        timer = new Timer("AgentTimer");
        //run this task immediatly and in period after
        long delay = 1000L*600*2;     // delay 20 min
    	if (i>0)  {
            Calendar firstTime = Calendar.getInstance();
            firstTime.set(Calendar.HOUR_OF_DAY,2);   //start at 2 am
            logger.info("First start is scheduled on " + firstTime);
            timer.schedule(new RunAgents(i), firstTime.getTime(), i*MINUTE);
    		started = true;
    		interval = i;
    	}
    }*/
}
