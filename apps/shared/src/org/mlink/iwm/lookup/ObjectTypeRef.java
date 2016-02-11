package org.mlink.iwm.lookup;

import org.apache.log4j.Logger;

import java.util.Map;

import org.mlink.iwm.lookup.CodeLookupValues;

public class ObjectTypeRef extends CodeLookupValues{
    private static final Logger logger = Logger.getLogger(ObjectTypeRef.class);
    public static final String FACILITIES = "F"; //Facilities";


    public String getSql() {
        return "SELECT * from  OBJECT_TYPE_REF ORDER BY DISP_ORD";
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
        ObjectTypeRef me = (ObjectTypeRef) LookupMgr.getInstance(ObjectTypeRef.class);
        return  me.getLabel(id==null?null:String.valueOf(id));
    }

    public static int getIdByCode(String code){
        ObjectTypeRef me = (ObjectTypeRef) LookupMgr.getInstance(ObjectTypeRef.class);
        return  me.getValueByCode(code).intValue();
    }
}
