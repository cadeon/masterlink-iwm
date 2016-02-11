package org.mlink.iwm.lookup;

import org.apache.log4j.Logger;

import java.util.Map;
import java.util.Collection;

import org.mlink.iwm.lookup.CodeLookupValues;

public class WorkScheduleStatusRef extends CodeLookupValues{
    private static final Logger logger = Logger.getLogger(JobStatusRef.class);

    public enum Status {NYS, IP, DUN, BRK;
		public boolean equals(String str){
	        return this.toString().equals(str);
	    }
	}
	    
	public String getSql() {
        return "SELECT * from WORK_SCHEDULE_STATUS_REF ORDER BY DISP_ORD";
    }

    /* does not seem to be used as a dropdown
       public void init() {
        addOption(new OptionItem("0",""));
    }*/

    public void addOption(Map map){
        Object value = map.get("ID");
        Object code = map.get("CODE");
        Object label = map.get("DESCRIPTION");
        if(value == null || label == null || code ==null)
            logger.error(getSql() + " returned invalid data " + map);
        else
            addOption(new OptionItem(value.toString(),label.toString(), code.toString()));
    }

    public static String getLabel(Integer id){
        WorkScheduleStatusRef me = (WorkScheduleStatusRef) LookupMgr.getInstance(WorkScheduleStatusRef.class);
        return  me.getLabel(id==null?null:String.valueOf(id));
    }

    public static int getIdByCode(String code){
        WorkScheduleStatusRef me = (WorkScheduleStatusRef) LookupMgr.getInstance(WorkScheduleStatusRef.class);
        return  me.getValueByCode(code).intValue();
    }

    public static int getIdByCode(Status code){
    	return getIdByCode(code.toString());
    }
    
	public static String getCode(Integer id) {
		WorkScheduleStatusRef me = (WorkScheduleStatusRef) LookupMgr.getInstance(WorkScheduleStatusRef.class);
		String code = me.getCode(id==null?null:String.valueOf(id));
		return code;
	}

    public Collection getDefault() {
        return null;
    }

}
