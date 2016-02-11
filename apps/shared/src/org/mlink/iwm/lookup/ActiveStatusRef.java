package org.mlink.iwm.lookup;

import org.apache.log4j.Logger;

import java.util.Map;
import java.util.Collection;
import java.util.ArrayList;
import java.util.HashMap;

import org.mlink.iwm.lookup.CodeLookupValues;

public class ActiveStatusRef extends CodeLookupValues{
    private static final Logger logger = Logger.getLogger(ActiveStatusRef.class);
    public static int ACTIVE       =  1;
    public static int NOT_ACTIVE   =  0;


    public String getSql() {
        return "SELECT * from ...";
    }

    public void addOption(Map map){
        Object value = map.get("id");
        Object label = map.get("value");
        if(value == null || label == null )
            logger.error(getSql() + " returned invalid data " + map);
        else
            addOption(new OptionItem(value.toString(),label.toString()));
    }

    public static String getLabel(Integer id){
        ActiveStatusRef me = (ActiveStatusRef) LookupMgr.getInstance(ActiveStatusRef.class);
        return  me.getLabel(id==null?null:String.valueOf(id));
    }

    public Collection getDefault() {
        Collection col = new ArrayList();
        Map map = new HashMap();
        map.put("id", "-1");map.put("value", "");col.add(map);
        map = new HashMap();
        map.put("id", "1");map.put("value", "Active");col.add(map);
        map = new HashMap();
        map.put("id", "0");map.put("value", "Not Active");col.add(map);
        return col;
    }

}
