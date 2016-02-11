package org.mlink.iwm.lookup;

import org.apache.log4j.Logger;

import java.util.Map;
import java.util.Collection;
import java.util.ArrayList;
import java.util.HashMap;

import org.mlink.iwm.lookup.CodeLookupValues;

public class OrganizationTypeRef extends CodeLookupValues{
    private static final Logger logger = Logger.getLogger(OrganizationTypeRef.class);


    public String getSql() {
        return "SELECT * from  ORGANIZATION_TYPE_REF ORDER BY DISP_ORD";
    }

    public void init() {
        //addOption(new OptionItem("0","All"));
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
        OrganizationTypeRef me = (OrganizationTypeRef) LookupMgr.getInstance(OrganizationTypeRef.class);
        return  me.getLabel(id==null?null:String.valueOf(id));
    }

}
