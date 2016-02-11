package org.mlink.iwm.lookup;

import org.apache.log4j.Logger;

import java.util.Map;
import java.util.Collection;

import org.mlink.iwm.lookup.CodeLookupValues;

public class TaskFrequencyRef extends CodeLookupValues{
    private static final Logger logger = Logger.getLogger(TaskFrequencyRef.class);

    public void init() {
        //Long dummy = null;
        //addOption(new OptionItem(dummy,""));
    }

    public String getSql() {
        return "SELECT * FROM TASK_FREQUENCY_REF ORDER BY DISP_ORD";
    }

    public void addOption(Map map){
        Object value = map.get("ID");
        Object label = map.get("DESCRIPTION");
        if(value == null || label == null )
            logger.error(getSql() + " returned invalid data " + map);
        else
            addOption(new OptionItem(value.toString(),label.toString()));
    }

    public static String getLabel(Integer id){
        TaskFrequencyRef me = (TaskFrequencyRef) LookupMgr.getInstance(TaskFrequencyRef.class);
        return  me.getLabel(id==null?null:String.valueOf(id));
    }

}
