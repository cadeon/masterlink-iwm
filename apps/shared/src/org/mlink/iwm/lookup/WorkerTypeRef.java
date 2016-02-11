package org.mlink.iwm.lookup;

import org.apache.log4j.Logger;

import java.util.Map;
import java.util.Collection;
import java.util.ArrayList;
import java.util.HashMap;

import org.mlink.iwm.lookup.CodeLookupValues;

public class WorkerTypeRef extends CodeLookupValues{
    private static final Logger logger = Logger.getLogger(WorkerTypeRef.class);
    public static final String INTERNAL   = "I";

    public String getSql() {
        return "SELECT * from  WORKER_TYPE_REF ORDER BY DISP_ORD";
    }

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
        WorkerTypeRef me = (WorkerTypeRef) LookupMgr.getInstance(WorkerTypeRef.class);
        return  me.getLabel(id==null?null:String.valueOf(id));
    }


    public static int getIdByCode(String code){
        WorkerTypeRef me = (WorkerTypeRef) LookupMgr.getInstance(WorkerTypeRef.class);
        return  me.getValueByCode(code).intValue();
    }

    public Collection getDefault() {
        return null;
    }

}
