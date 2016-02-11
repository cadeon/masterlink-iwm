package org.mlink.iwm.lookup;

import org.apache.log4j.Logger;

import java.util.Map;
import java.util.Collection;
import java.util.ArrayList;
import java.util.HashMap;

import org.mlink.iwm.lookup.CodeLookupValues;

public class PriorityRef extends CodeLookupValues{
    private static final Logger logger = Logger.getLogger(PriorityRef.class);

    public static final String MEDIUM         = "Medium";


    public String getSql() {
        return "SELECT * FROM PRIORITY_REF ORDER BY DISP_ORD";
    }

    public void addOption(Map map){
        Object value = map.get("ID");
        Object label = map.get("DESCRIPTION");
        Object code  = map.get("CODE");
        if(value == null || label == null )
            logger.error(getSql() + " returned invalid data " + map);
        else
            addOption(new OptionItem(value.toString(),label.toString(),code.toString()));
    }
    
    public static String getCode(Integer id){
        PriorityRef me = (PriorityRef) LookupMgr.getInstance(PriorityRef.class);
        return  me.getCode(id==null?null:String.valueOf(id));
    }
    
    public static String getLabel(Integer id){
        PriorityRef me = (PriorityRef) LookupMgr.getInstance(PriorityRef.class);
        return  me.getLabel(id==null?null:String.valueOf(id));
    }

    public static int getIdByCode(String code){
    	PriorityRef me = (PriorityRef) LookupMgr.getInstance(PriorityRef.class);
        return  me.getValueByCode(code).intValue();
    }

    public static int getIdByLabel(String code){
    	PriorityRef me = (PriorityRef) LookupMgr.getInstance(PriorityRef.class);
        return  me.getValueByLabel(code).intValue();
    }
    
    public Collection getDefault() {
        return null;
    }

}
