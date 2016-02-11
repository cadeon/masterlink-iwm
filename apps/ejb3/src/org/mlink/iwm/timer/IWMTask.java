package org.mlink.iwm.timer;

import org.apache.log4j.Logger;
import org.mlink.iwm.notification.MailUtils;

import java.util.TimerTask;
import java.util.Date;
import java.util.Timer;
import java.util.Calendar;

/**
 * Created by IntelliJ IDEA.
 * User: andreipovodyrev
 * Date: Jul 31, 2007
 */
public abstract class IWMTask extends TimerTask {
    protected final long MINUTE = 1000L*60;
    protected final long HOUR = 1000L*60*60;
    protected final long DAY = 1000L*60*60*24;
    private static final Logger logger = Logger.getLogger(IWMTask.class);
    private Timer timer;


    /**
     * Return Date when task starts for the first time
     * @return  Date
     */
    public Date getStartTime() {
        Calendar firstTime = Calendar.getInstance();
        firstTime.set(Calendar.HOUR_OF_DAY,1);
        firstTime.set(Calendar.MINUTE,5);
        firstTime.set(Calendar.SECOND,0);
        return firstTime.getTime();
    }


    /**
     *
     * @return def interval in millisecs
     */
    public abstract long getInterval();



    synchronized  void start() {
        if(timer!=null) stop();
        if(getInterval()<=0){
            logger.info("Did not start " + getClass().getName() + " Interval is <= 0");
            return;
        }
        timer = new Timer(getClass().getName());
        long t = getStartTime().getTime();
        while(t<System.currentTimeMillis()){
            t+=getInterval();
        }
        Date nextStart = new Date(t);
        logger.info("Starting Timer service " + System.identityHashCode(this)
                + ". Task " + getClass().getName() + " which will run every " + formatDuration(getInterval()) + " starting " + nextStart);

        timer.schedule(this, nextStart, getInterval());
    }

     synchronized void stop() {
        if(timer == null) return;
        logger.info("Stopping Timer service " + System.identityHashCode(this));
        timer.cancel(); timer = null;
        logger.info("Timer service for " + getClass().getName() + " is stopped");
    }


    public void run() {
        logger.info("Task " + getClass().getName() +  " started");
        try {
            execute();
        } catch (Exception e) {
            logger.error("Error executing " + getClass().getName(), e);   //print to server.log
            MailUtils.informSupport(e);
        }
        logger.info("Task " + getClass().getName() +  " completed. Next run in " + formatDuration(getInterval()));
    }

    private static String formatDuration(long durationMillisecs){
        long minutes = durationMillisecs/1000L/60 % 60;
        long hours = (durationMillisecs/1000L/60-minutes)/60;
        String rtn="";
        if(hours>0) rtn=hours+"h";
        return rtn+String.valueOf(minutes % 60 + 100).substring(1,3)+ "m";
    }

    public abstract void execute() throws Exception;
}
