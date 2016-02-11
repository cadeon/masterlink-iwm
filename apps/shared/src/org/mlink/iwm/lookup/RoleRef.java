package org.mlink.iwm.lookup;

import org.apache.log4j.Logger;

import java.util.Map;
import java.util.Collection;


public class RoleRef extends CodeLookupValues{
    private static final Logger logger = Logger.getLogger(RoleRef.class);
    public static final String ConMR = "ConMR"; //Maintenance Request
    public static final String ConMW = "ConMW"; //Mobile Worker
    public static final String RstPass = "RstPass"; //Reset password

    public String getSql() {
        return "SELECT * from  ROLE ORDER BY DESCRIPTION";
    }

    public void addOption(Map map){
        Object value = map.get("ID");
        Object label = map.get("DESCRIPTION");
        Object code = map.get("NAME");
        if(value == null || label == null )
            logger.error(getSql() + " returned invalid data " + map);
        else
            addOption(new OptionItem(value.toString(),label.toString(),code.toString()));
    }

    public static String getLabel(Integer id){
        RoleRef me = (RoleRef) LookupMgr.getInstance(RoleRef.class);
        return  me.getLabel(id==null?null:String.valueOf(id));
    }

    public Collection getDefault() {
        return null;
    }

    public static int getIdByCode(String code){
        RoleRef me = (RoleRef) LookupMgr.getInstance(RoleRef.class);
        return  me.getValueByCode(code).intValue();
    }

    public static Collection <OptionItem> getRoles(){
        RoleRef me = (RoleRef) LookupMgr.getInstance(RoleRef.class);
        return  me.getOptions();
    }

}
