package org.mlink.iwm.lookup;

import org.apache.log4j.Logger;
import java.util.*;


public class LocatorRef extends CodeLookupValues{
    private static final Logger logger = Logger.getLogger(LocatorRef.class);


    public String getSql() {
        return "SELECT ID, NAME, PARENT_ID, SCHEMA_ID, FULL_LOCATOR FROM LOCATOR ORDER BY NAME";
    }

    public void addOption(Map map){
        Object value = map.get("ID");
        Object label = map.get("NAME");
        Object parent = map.get("PARENT_ID");
        Object schema = map.get("SCHEMA_ID");
        Object fullLocator = map.get("FULL_LOCATOR");
        Long parentId = parent==null?null:(new Long(parent.toString()));
        if(value == null || label == null || schema == null )
            logger.error(getSql() + " returned invalid data " + map);
        else
            addOption(new LocatorOptionItem(Long.valueOf(value.toString()),
                        label.toString(), parentId,
                        Integer.valueOf(schema.toString()),
                        fullLocator==null?"":fullLocator.toString()
                    ));
    }

    public static LocatorRef getInstance(){
        return (LocatorRef) LookupMgr.getInstance(LocatorRef.class);
    }

    public static String getLabel(Long id){
        LocatorRef me =  getInstance();
        return  me.getLabel(id==null?null:String.valueOf(id));
    }


    public static Integer getSchemaId(Long id){
        LocatorRef me =  getInstance();
        LocatorOptionItem item = (LocatorOptionItem)me.getOptionItem(String.valueOf(id));
        return item==null?null:item.getSchemaId();
    }

    public static String getSchemaName(Long id){
        Integer schemaId = LocatorRef.getSchemaId(id);
        return LocatorSchemaRef.getLabel(schemaId);
    }

    public static String getFullLocator(Long id){
        LocatorRef me =  getInstance();
        LocatorOptionItem item = (LocatorOptionItem)me.getOptionItem(String.valueOf(id));
        return item==null?"":item.getFullLocator();
    }

    public static String getDescription(Long id){
        LocatorRef me =  getInstance();
        Stack <String> tmp = new Stack<String>();
        LocatorOptionItem item;
        do {
            item = (LocatorOptionItem)me.getOptionItem(String.valueOf(id));
            tmp.push(item==null?"":item.getLabel());
            id=item.getParentId();
        }while (id!=null);

        StringBuilder desc = new StringBuilder();
        while(!tmp.empty()) {
            if (desc.length() != 0) desc.append("/");
            desc.append(tmp.pop());
        }
        return desc.toString();
    }

    public Collection getDefault() {
        return null;   // getSql will be used instead
    }

}
