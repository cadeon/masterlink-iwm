package org.mlink.iwm.util;

import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * Created by IntelliJ IDEA.
 * User: andreipovodyrev
 * Date: Jul 13, 2007
 */
public class FlashLog {
    private StringBuilder log = new StringBuilder();
    private static final DateFormat mTimeFmt = new SimpleDateFormat("MM/dd HH:mm:ss");


    public void clear(){
        log = new StringBuilder();
    }

    public void log(String str){
        log.append("\n").append(mTimeFmt.format(new Date())).append(": ").append(str);
    }

    public String getLog(){
        return log.toString();
    }
}
