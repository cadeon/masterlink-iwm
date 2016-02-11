package org.mlink.iwm.lookup;

import org.apache.log4j.Logger;

import java.util.Map;
import java.util.Collection;
import java.sql.SQLException;

import org.mlink.iwm.util.DBAccess;

public class TargetClassRef extends CodeLookupValues{
    private static final Logger logger = Logger.getLogger(TargetClassRef.class);


    public String getSql() {
        return "SELECT ID, CODE, DESCRIPTION, SCHEMA_ID, PARENT_ID, ABBR from OBJECT_CLASSIFICATION ORDER BY CODE ";
    }

    public void addOption(Map map){
        Object value = map.get("ID");
        Object label = "[" + map.get("CODE") + "] " + map.get("DESCRIPTION");
        Object parent = map.get("PARENT_ID");
        Object schema = map.get("SCHEMA_ID");
        Object code = map.get("CODE");
        Object abbr = map.get("ABBR");
        Long parentId = parent==null?null:(new Long(parent.toString()));
        if(value == null || label == null )
            logger.error(getSql() + " returned invalid data " + map);
        else{
            OptionItem item = new TreeOptionItem(Long.valueOf(value.toString()),label.toString(), parentId, Integer.valueOf(schema.toString()));
            item.addProperty("code",code);
            item.addProperty("abbr",abbr);
            addOption(item);
        }
    }
    public static TargetClassRef getInstance(){
        return (TargetClassRef) LookupMgr.getInstance(TargetClassRef.class);
    }

    public static String getLabel(Integer id){
        return  getInstance().getLabel(id==null?null:String.valueOf(id));
    }
    public static String getLabel(Long id){
        return  getInstance().getLabel(id==null?null:String.valueOf(id));
    }

    public static String getCode(Long id){
        return (String)getInstance().getOptionItem(String.valueOf(id)).getProperty("code");
    }

    public static String getAbbr(Long id){
        return (String)getInstance().getOptionItem(String.valueOf(id)).getProperty("abbr");
    }
    public static String getHierarchy(Long id) throws SQLException {
        return (String) DBAccess.executeFunction("GET_CLASS_HIERARCHY",id);
    }

    public Collection getDefault() {
        return null;
    }

}
