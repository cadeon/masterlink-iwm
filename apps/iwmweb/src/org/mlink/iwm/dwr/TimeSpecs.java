package org.mlink.iwm.dwr;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.mlink.iwm.access3.ServiceLocator;
import org.mlink.iwm.base.WebException;
import org.mlink.iwm.bean.ResponsePage;
import org.mlink.iwm.bean.TimeSpec;
import org.mlink.iwm.bean.TimeSpecRequest;
import org.mlink.iwm.dao.DaoFactory;
import org.mlink.iwm.dao.PaginationRequest;
import org.mlink.iwm.dao.PaginationResponse;
import org.mlink.iwm.dao.TimeSpecsCriteria;
import org.mlink.iwm.entity3.Person;
import org.mlink.iwm.entity3.WorkSchedule;
import org.mlink.iwm.exception.BusinessException;
import org.mlink.iwm.session.ControlSF;
import org.mlink.iwm.session.PolicySF;
import org.mlink.iwm.util.DateUtil;

/**
 * User: John Mirick
 * Note: throwing Exceptions outside of methods allows use of DWR error handling (alerts in UI)
 * Note DWR does not support method oveloading well. Avoid that!
 */

public class TimeSpecs implements MaintainanceTable{
    final  String WS_DELETED    ="Work schedule is removed";
    final  String WS_RESET      ="Work schedule is reset";

    private static final Logger logger = Logger.getLogger(TimeSpecs.class);

    public ResponsePage getData(HashMap criteria, int offset, int pageSize, String orderBy, String orderDirection) throws Exception {
        TimeSpecsCriteria cr = new TimeSpecsCriteria(criteria);
        SessionUtil.setAttribute("TimeSpecsCriteria",cr);    //store in session, reuse in later when coming back        
        PaginationRequest request = new PaginationRequest(offset,pageSize,orderBy,orderDirection);
        PaginationResponse response = DaoFactory.process(DaoFactory.NAME.TimeSpecsDAO,cr,request);
        List lst =  response.convertRowsToClasses(org.mlink.iwm.bean.TimeSpec.class);
        return new ResponsePage(response.getTotalCount(),lst);
    }

    public String deleteItem(Long wsId) throws Exception{
        String rtn = "";
        try{
            PolicySF psf = ServiceLocator.getPolicySFLocal( );
            psf.removeWorkSchedule(wsId);
        }catch(BusinessException be){
            rtn = be.getMessage();
        }
        return rtn;
    }

    public TimeSpec getItem(Long personId) throws Exception{
        //NA
        return null;
    }

    /**
     * Creates TimesSpecs Work Calendar netries
     * @param map
     * @return
     * @throws Exception
     */
    public String saveItem(HashMap map) throws Exception{
        TimeSpecRequest request = new TimeSpecRequest(map);
        String rtn = ITEM_SAVED_OK_MSG;
        Long personId=request.getPersonId();
        Long locatorId = request.getLocatorId();
        java.sql.Date start = request.getStartDate();
        java.sql.Date end = request.getEndDate();
        Integer shift = request.getShiftId();
        int time = request.getTime();

        try{
            PolicySF psf = ServiceLocator.getPolicySFLocal( );
            psf.generateTimeSpecs(personId,locatorId,start,end,shift,time, getIncludedWeekDays(request));
        }catch(BusinessException be){
            rtn = be.getMessage();
        }catch(Exception e){
            e.printStackTrace();
            throw new WebException(e);
        }
        return rtn;
    }

    public String endShift(HashMap map) throws Exception{
        TimeSpecRequest request = new TimeSpecRequest(map);
        String rtn = ITEM_SAVED_OK_MSG;
        Long personId=request.getPersonId();
        Long wsId=request.getWorkScheduleId();
        //Integer shift = request.getShiftId();
        int time = request.getTime();
        
        ControlSF csf = ServiceLocator.getControlSFLocal( );
        Person person = csf.get(Person.class, personId);
        WorkSchedule ws = csf.get(WorkSchedule.class, wsId);
        Date eventTime = ws.getDay();
        Calendar cal = new GregorianCalendar();
        cal.setTime(eventTime);
        cal.add(Calendar.MINUTE, time);
        try{
	        csf.endShift(person, DateUtil.displayShortDate(ws.getDay()), cal.getTime());
        }catch(Exception e){
        	rtn=e.getMessage();
        }
        return rtn;
    }
    
    private int [] getIncludedWeekDays(TimeSpecRequest request) {
        int [] res = new int []{0,0,0,0,0,0,0};
        if(request.getMonday()) res[0]=Calendar.MONDAY;
        if(request.getTuesday()) res[1]=Calendar.TUESDAY;
        if(request.getWednesday()) res[2]=Calendar.WEDNESDAY;
        if(request.getThursday()) res[3]=Calendar.THURSDAY;
        if(request.getFriday()) res[4]=Calendar.FRIDAY;
        if(request.getSaturday()) res[5]=Calendar.SATURDAY;
        if(request.getSunday()) res[6]=Calendar.SUNDAY;
        return res;
    }

    public String resetWorkschedule(Long wsId) throws Exception{
        String rtn = WS_RESET;
        PolicySF psf = ServiceLocator.getPolicySFLocal( );
        psf.resetWorkSchedule(wsId);
        return rtn;
    }

    /**
     * In case of seek worker allows to remove ws even with scheduled jobs
     * @param wsId
     * @return
     * @throws Exception
     */
    public String makeAbsent(Long wsId) throws Exception{
        String rtn = WS_DELETED;
        try{
            PolicySF psf = ServiceLocator.getPolicySFLocal( );
            psf.removeWorkSchedule(wsId);
        }catch(BusinessException be){
            rtn = be.getMessage();
        }
        return rtn;
    }
}
