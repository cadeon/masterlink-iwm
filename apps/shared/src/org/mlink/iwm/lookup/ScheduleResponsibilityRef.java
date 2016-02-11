
package org.mlink.iwm.lookup;

import org.apache.log4j.Logger;

import java.util.Map;
import java.util.Collection;

public class ScheduleResponsibilityRef extends CodeLookupValues{
    private static final Logger logger = Logger.getLogger(ScheduleResponsibilityRef.class);
    public static final String SYSTEM = "System";
    public static final String MANUAL = "Manual";

    public String getSql() {
        return "SELECT * from SCHEDULE_RESPONSIBILITY_REF ORDER BY DISP_ORD";
    }

    public void addOption(Map map){
        Object value = map.get("ID");
        Object label = map.get("CODE");
        if(value == null || label == null )
            logger.error(getSql() + " returned invalid data " + map);
        else
            addOption(new OptionItem(value.toString(),label.toString(), label.toString()));
    }

    public static String getLabel(Integer id){
        ScheduleResponsibilityRef me = (ScheduleResponsibilityRef) LookupMgr.getInstance(ScheduleResponsibilityRef.class);
        return  me.getLabel(id==null?null:String.valueOf(id));
    }
    public static int getIdByCode(String code){
        ScheduleResponsibilityRef me = (ScheduleResponsibilityRef) LookupMgr.getInstance(ScheduleResponsibilityRef.class);
        return  me.getValueByCode(code).intValue();
    }

    public Collection getDefault() {
        return null;
    }

}

