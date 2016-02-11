package org.mlink.iwm.lookup;

import org.apache.log4j.Logger;
import java.util.*;


public class ObjectClassificationRef extends CodeLookupValues{
    private static final Logger logger = Logger.getLogger(ObjectClassificationRef.class);


    public String getSql() {
        return "SELECT ID, ABBR, PARENT_ID, SCHEMA_ID FROM Object_Classification ORDER BY ABBR";
    }

    public void addOption(Map map){
        Object value = map.get("ID");
        Object label = map.get("ABBR");
        Object parent = map.get("PARENT_ID");
        Object schema = map.get("SCHEMA_ID");
        Long parentId = parent==null?null:(new Long(parent.toString()));
        if(value == null || label == null || schema == null )
            logger.error(getSql() + " returned invalid data " + map);
        else
            addOption(new ObjectClassificationOptionItem(Long.valueOf(value.toString()),
                        label.toString(), parentId,
                        Integer.valueOf(schema.toString())));
    }

    public static ObjectClassificationRef getInstance(){
        return (ObjectClassificationRef) LookupMgr.getInstance(ObjectClassificationRef.class);
    }

    public static String getLabel(Long id){
        ObjectClassificationRef me =  getInstance();
        return  me.getLabel(id==null?null:String.valueOf(id));
    }


    public static Integer getSchemaId(Long id){
        ObjectClassificationRef me =  getInstance();
        ObjectClassificationOptionItem item = (ObjectClassificationOptionItem)me.getOptionItem(String.valueOf(id));
        return item==null?null:item.getSchemaId();
    }

    public static String getSchemaName(Long id){
        Integer schemaId = ObjectClassificationRef.getSchemaId(id);
        return ObjectClassSchemaRef.getLabel(schemaId);
    }

    public static String getDescription(Long id){
        ObjectClassificationRef me =  getInstance();
        Stack <String> tmp = new Stack<String>();
        ObjectClassificationOptionItem item;
        do {
            item = (ObjectClassificationOptionItem)me.getOptionItem(String.valueOf(id));
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
