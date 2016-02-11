package org.mlink.iwm.timer;

import java.util.Calendar;
import java.util.Date;

import org.mlink.iwm.access3.ServiceLocator;
import org.mlink.iwm.session.PolicySF;

/**
 * Created by IntelliJ IDEA.
 * User: andreipovodyrev
 * Date: Sep 30, 2007
 */
public class CreateWorkSchedules extends IWMTask {
    private final int numberOfDays = 7; //generate schedules for 1 week

    public void execute() throws Exception{
        PolicySF psf = ServiceLocator.getPolicySFLocal();
        psf.generateTimeSpecs(new java.sql.Date(System.currentTimeMillis()),numberOfDays);
    }

    public Date getStartTime() {
        Calendar firstTime = Calendar.getInstance();
        firstTime.set(Calendar.DAY_OF_WEEK,Calendar.MONDAY);
        firstTime.set(Calendar.HOUR_OF_DAY,2);
        firstTime.set(Calendar.MINUTE,5);
        firstTime.set(Calendar.SECOND,0);
        return firstTime.getTime();
    }

    public long getInterval() {
        return numberOfDays*DAY;
    }
}
