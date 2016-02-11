package org.mlink.iwm.lookup;

import org.apache.log4j.Logger;

import java.util.Map;
import java.util.Collection;
import java.util.ArrayList;
import java.util.HashMap;

import org.mlink.iwm.lookup.CodeLookupValues;

public class DataTypeRef extends CodeLookupValues{
    private static final Logger logger = Logger.getLogger(DataTypeRef.class);


    public String getSql() {
        return "SELECT * from  DATA_TYPE_REF ORDER BY DISP_ORD";
    }
    public void init() {
        //Long dummy = null;
        //addOption(new OptionItem(dummy,""));
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
        DataTypeRef me = (DataTypeRef) LookupMgr.getInstance(DataTypeRef.class);
        return  me.getLabel(id==null?null:String.valueOf(id));
    }

    public Collection getDefault() {
        return null;
    }

}
