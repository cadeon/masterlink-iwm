package org.mlink.iwm.lookup;

import org.apache.log4j.Logger;

import java.util.Map;
import java.util.Collection;

public class TaskTypeRef extends CodeLookupValues{
    private static final Logger logger = Logger.getLogger(TaskTypeRef.class);
    public static final String ROUTINE_MAINT    = "Routine";
    public static final String ASSESSMENT       = "Assessment";
    public static final String COMBINED         = "Combined";
    public static final String URGENT         = "Urgent";


    public String getSql() {
        // changed due to defect 218 commentring this return "SELECT * FROM TASK_TYPE_REF ORDER BY DISP_ORD";
        return "SELECT * FROM TASK_TYPE_REF ORDER BY CODE";
    }

    public void init() {
        //Long dummy = null;
        //addOption(new OptionItem(dummy,""));
    }

    public void addOption(Map map){
        Object value = map.get("ID");
        //Object label = map.get("DESCRIPTION");
        Object label = map.get("CODE");
        Object code = map.get("CODE");
        if(value == null || label == null || code == null)
            logger.error(getSql() + " returned invalid data " + map);
        else{
            OptionItem option = new OptionItem(value.toString(),label.toString(), code.toString());
            addOption(option);
        }

    }

    public static String getLabel(Integer id){
        TaskTypeRef me = (TaskTypeRef) LookupMgr.getInstance(TaskTypeRef.class);
        return  me.getLabel(id==null?null:String.valueOf(id));
    }

    public static int getIdByCode(String code){
        TaskTypeRef me = (TaskTypeRef) LookupMgr.getInstance(TaskTypeRef.class);
        return  me.getValueByCode(code).intValue();
    }

    public static int getIdByLabel(String code){
        TaskTypeRef me = (TaskTypeRef) LookupMgr.getInstance(TaskTypeRef.class);
        return  me.getValueByLabel(code).intValue();
    }

    public Collection getDefault() {
        return null;
    }

}
