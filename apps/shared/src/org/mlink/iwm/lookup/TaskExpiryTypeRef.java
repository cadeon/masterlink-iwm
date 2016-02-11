package org.mlink.iwm.lookup;

import java.util.Collection;
import java.util.Map;

import org.apache.log4j.Logger;

public class TaskExpiryTypeRef extends CodeLookupValues{
    private static final Logger logger = Logger.getLogger(TaskExpiryTypeRef.class);
    
    public enum Status {EOM, EOY, REL, NEX, EOW;
    
    	public boolean equals(String str){
	        return this.toString().equals(str);
	    }
	}
    
    public String getSql() {
        return "SELECT * from TASK_DEF_EXPIRY_TYPE_REF ORDER BY DISP_ORD";
    }
    public void init() {
        //Long dummy = null;
        //addOption(new OptionItem(dummy,""));
    }

    public void addOption(Map map){
        Object value = map.get("ID");
        String code = (String)map.get("CODE");
        Object label = map.get("DESCRIPTION");
        if(value == null || label == null || code==null)
            logger.error(getSql() + " returned invalid data " + map);
        else{
            OptionItem item = new OptionItem(value.toString(),label.toString(), code.toString());
            addOption(item);
        }
    }

    public static String getLabel(Integer id){
        TaskExpiryTypeRef me = (TaskExpiryTypeRef) LookupMgr.getInstance(TaskExpiryTypeRef.class);
        return  me.getLabel(id==null?null:String.valueOf(id));
    }

    public static int getIdByCode(String code){
        TaskExpiryTypeRef me = (TaskExpiryTypeRef) LookupMgr.getInstance(TaskExpiryTypeRef.class);
        return  me.getValueByCode(code).intValue();
    }
    
    public static int getIdByCode(TaskExpiryTypeRef.Status code){
        return  getIdByCode(code.toString());
    }
    
    public static String getCode(Integer id){
        TaskExpiryTypeRef me = (TaskExpiryTypeRef) LookupMgr.getInstance(TaskExpiryTypeRef.class);
        return  me.getCode(id==null?null:String.valueOf(id));
    }

    public Collection getDefault() {
        return null;
    }
}
