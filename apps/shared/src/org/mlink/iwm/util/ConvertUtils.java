package org.mlink.iwm.util;


import org.apache.log4j.Logger;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.lang.reflect.Method;


public class ConvertUtils {
    private static final SimpleDateFormat mDateFmtShort = new SimpleDateFormat(Config.getProperty(Config.DATE_PATTERN));
    private static final DateFormat mDateFmt = new SimpleDateFormat(Config.getProperty(Config.YEAR4D_DATE_PATTERN));
    private static final DateFormat mTimeFmt = new SimpleDateFormat(Config.getProperty(Config.TIME_PATTERN));
    private static final DecimalFormat mDecFmt = new DecimalFormat("###,###,###,###.##");
    private static final Logger logger = Logger.getLogger(ConvertUtils.class);

    //static initializer ..default 2 digit start year is 1924 ...So if you pass a 2 digit year like
    // 30 date formatter will parse it as 1930 ..So I am setting this start year to 1970 ..So if user
    //passes 69 it will be 2069 will 70 and above will be 1970.

    static {
        try {
            java.util.Date date = mDateFmt.parse(Config.getProperty(Config.
                    TWO_DIGIT_START_YEAR));
            mDateFmtShort.set2DigitYearStart(date);
        } catch (java.text.ParseException pe) {
            pe.printStackTrace();
        }
    }

    private ConvertUtils() {}

    public static Double string2Double(String s) throws ParseException {
        return mDecFmt.parse(s).doubleValue();
    }


    public static java.sql.Date string2Date(String s) throws ParseException {
        return new java.sql.Date(mDateFmt.parse(s).getTime());
    }


    public static java.sql.Date stringShort2Date(String s) throws ParseException {
        return new java.sql.Date(mDateFmtShort.parse(s).getTime());
    }


    public static String formatDate(java.sql.Date date) {
        if (date == null)
            return null;
        else
            return (mDateFmt.format(date));
    }

    public static String formatDate(java.util.Date date) {
        if (date == null)
            return null;
        else
            return (mDateFmt.format(date));
    }
    
    public static String formatDateShort(java.sql.Date date) {
        if (date == null)
            return null;
        else
            return (mDateFmtShort.format(date));
    }

    public static String formatDateShort(java.util.Date date) {
        if (date == null)
            return null;
        else
            return (mDateFmtShort.format(date));
    }
    
    public static String formatDatetime(java.sql.Date date) {
        if (date == null)
            return null;
        else
            return (mTimeFmt.format(date));
    }

    public static String formatDatetime(java.util.Date date) {
        if (date == null)
            return null;
        else
            return (mTimeFmt.format(date));
    }


    public static double doubleVal(Double d) {
        return (d != null ? d : 0);
    }

    public static int intVal(Integer i) {
        return (i != null ? i : 0);
    }

    public static int intVal(String str) {
        int rtn = 0;
        try {
            rtn = Integer.parseInt(str.replaceAll(",",""));
        } catch (NumberFormatException e) {
        }
        return rtn;
    }

    public static DateFormat getCMS2DigitYearDateFormat() {
        return mDateFmtShort;
    }

    public static DateFormat getDateFormat() {
        return mDateFmt;
    }

    public static DateFormat getTimeFormat() {
        return mTimeFmt;
    }

    public static DecimalFormat getDecimalFormat() {
        return mDecFmt;
    }


    /**
     * @return String - todays date in the format 
     *         <p/>
     *         Month and day will always be 2 digits .
     */

    public static String getCurrentDate() {
        return getDateAsString(new java.util.Date());
    }

    /**
     * @param date Date --any date
     * @return String- returns date in the format 
     *         <p/>
     *         Month and day will always be 2 digits .
     */

    public static String getDateAsString(java.util.Date date) {
        Calendar cal = Calendar.getInstance(java.util.Locale.US);
        cal.setTime(date);
        int year = cal.get(Calendar.YEAR);
        int mon = cal.get(Calendar.MONTH);
        int dy = cal.get(Calendar.DAY_OF_MONTH);

        //month start from 0 ..hence

        mon++;
        //just making it 2 digits for sure
        String month = (mon < 10 ? "0" + mon : "" + mon);
        String day = (dy < 10 ? "0" + dy : "" + dy);
        StringBuffer currentDate = new StringBuffer().append(month).append("/").append(day).append("/").append(year);
        return currentDate.toString();
    }

    public static String substring(String input, int maxLength) {
        if (input == null)
            return null;
        else
            return (input.length()) <= maxLength ? input : input.substring(0, maxLength);
    }

    /**
     * @param ipformat   String --the format of the date
     * @param datestring String--the actual date in the form of a string
     * @return String--the date in the format
     * @throws ParseException
     */

    public static String convertDateFormat(String ipformat, String datestring) throws ParseException {
        SimpleDateFormat sdf;
        if (ipformat.equals(Config.getProperty(Config.YEAR4D_DATE_PATTERN))) {
            sdf = (SimpleDateFormat) getDateFormat();
        } else if (ipformat.equals(Config.getProperty(Config.DATE_PATTERN))) {
            sdf = (SimpleDateFormat) getCMS2DigitYearDateFormat();
        } else {
            sdf = new SimpleDateFormat(ipformat);
        }
        java.util.Date date = sdf.parse(datestring);
        return getDateAsString(date);

    }

    /**
     * @param date String in the format   ..Month and day should be 2 digits
     * @return String ..
     */

    public static String getMonth(String date) {
        return date.substring(0, 2);
    }

    public static String getDay(String date) {
        return date.substring(3, 5);
    }

    public static String getYear(String date) {
        return date.substring(6);
    }


    /**
     * @param col Collection of objects (usually ids) to stringify
     * @return String Comma-separated list of the items in the collection
     */

    public static String stringify(Collection col) {
        if (col == null || col.size() == 0)
            return "";

        String items = "",delim = "";
        for (Object aCol : col) {
            String next = aCol.toString();
            items = items + delim + next;
            delim = ",".intern();
        }
        return items;
    }


    /**
     * nice little method to replace cariage return characters with html break <br>
     * @param source
     * @return modified string
     */
    public static  String htmlWithBrakes(String source)   {
        if (source==null) return "";
        StringTokenizer st = new StringTokenizer( source, "\n", true );
        StringBuilder target = new StringBuilder();

        String temp;
        while ( st.hasMoreTokens() )    {
            temp = st.nextToken();
            if ( temp.length() != 1 ) { target.append(temp); continue; }
            if ( "\n".equals(temp)) {
                target.append("<br>");
                continue;
            }
            target.append(temp);
        }
        return target.toString();
    }

    /**
     * Generic toString method for an object
     */

    public static String toString(String offset, Object obj){
        if(obj == null) return "";
        StringBuilder rtn = new StringBuilder();
        Method [] methods = obj.getClass().getDeclaredMethods();
        try {
            for (Method method : methods) {
                if(method.getName().startsWith("get")){
                    Object tmp = method.invoke(obj);
                    if (tmp == null) continue;

                    rtn.append(offset).append(method.getName().substring(3)).append(":");
                    if(tmp instanceof Collection){
                        Collection children = (Collection) tmp;
                        rtn.append("\n");
                        for (Object o : children) {
                            rtn.append(toString("\t"+offset,o));
                        }
                    }else{
                        rtn.append(offset).append(tmp.toString()).append("\n");
                    }
                }
            }
        } catch (Exception e) {
            logger.debug(e.getCause()==null?e.getMessage():e.getCause().getMessage());
        }
        return rtn.toString();
    }

    public static String toString(Object obj){
        return toString("",obj);
    }

    public static String formatDuration(int durationMins){
        int minutes = durationMins % 60;
        int hours = (durationMins-minutes)/60;
        return hours+":"+String.valueOf(minutes % 60 + 100).substring(1,3);
    }

    public static String formatDuration(long durationMins){
        long minutes = durationMins % 60;
        long hours = (durationMins-minutes)/60;
        return hours+":"+String.valueOf(minutes % 60 + 100).substring(1,3);
    }


} // end class ConvertUtils

