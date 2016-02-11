package org.mlink.iwm.lookup;

import org.apache.log4j.Logger;

import java.util.*;

public class ShiftRef extends CodeLookupValues{
    private static final Logger logger = Logger.getLogger(ShiftRef.class);


    public String getSql() {
        return "SELECT * from SHIFT_REF WHERE archived_date is null ORDER BY DISP_ORD";
    }

    public void addOption(Map map){
        Object value = map.get("ID");
        Object label = map.get("CODE");
        Object shiftStart = map.get("SHIFTSTART");
        Object shiftEnd = map.get("SHIFTEND");
        Object shiftTime = map.get("TIME");
        if(value == null || label == null )
            logger.error(getSql() + " returned invalid data " + map);
        else{
            OptionItem option = new OptionItem(value.toString(),label.toString());
            option.addProperty("shiftStart",shiftStart==null?"":shiftStart.toString());
            option.addProperty("shiftEnd",shiftEnd==null?"":shiftEnd.toString());
            option.addProperty("shiftTime",shiftTime==null?"":shiftTime.toString());
            addOption(option);
        }
    }

    public static String getCode(Integer id){
        PriorityRef me = (PriorityRef) LookupMgr.getInstance(PriorityRef.class);
        return  me.getCode(id==null?null:String.valueOf(id));
    }
    
    public static String getLabel(Integer id){
        ShiftRef me = (ShiftRef) LookupMgr.getInstance(ShiftRef.class);
        return  me.getLabel(id==null?null:String.valueOf(id));
    }

    public static String getShiftStart(Integer id){
        ShiftRef me = (ShiftRef) LookupMgr.getInstance(ShiftRef.class);
        OptionItem option = me.getOptionItem(String.valueOf(id));
        return (String)option.getProperty("shiftStart");
    }


    public static String getShiftTime(Integer id){
        ShiftRef me = (ShiftRef) LookupMgr.getInstance(ShiftRef.class);
        OptionItem option = me.getOptionItem(String.valueOf(id));
        return (String)option.getProperty("shiftTime");
    }

    public static String getShiftEnd(Integer id){
        ShiftRef me = (ShiftRef) LookupMgr.getInstance(ShiftRef.class);
        OptionItem option = me.getOptionItem(String.valueOf(id));
        return (String)option.getProperty("shiftEnd");
    }




}
