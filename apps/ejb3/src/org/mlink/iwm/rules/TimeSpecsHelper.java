package org.mlink.iwm.rules;

import org.mlink.iwm.exception.BusinessException;

import java.util.*;

public class TimeSpecsHelper {
    /**
     * Generates dates in range set by start and end.
     * @param start
     * @param end
     * @param includeWeekDays  list of included week days as defined by java Calendar week days constants
     * @return list dates
     */
    public static List<Date> getDaysInRange(Date start, Date end,  int [] includeWeekDays) throws BusinessException{
        if(start==null || end==null || start.compareTo(end)>0)
        throw new BusinessException("Date range is not properly defined! start="+start + " end="+end);
        List dates = new ArrayList();
        Arrays.sort(includeWeekDays);    //must be sorted for binarySerach to work!
        Calendar cal = Calendar.getInstance();
        cal.setTime(start);
        Date day = start;
        do {
            //System.out.println(cal.get(Calendar.DAY_OF_WEEK));
            if(Arrays.binarySearch(includeWeekDays, cal.get(Calendar.DAY_OF_WEEK)) >= 0){
                dates.add(day);
            }
            cal.add(Calendar.DATE,1);
            day = new java.sql.Date(cal.getTimeInMillis());
        }
        while(day.compareTo(end) <= 0);

        return dates;
    }
}
