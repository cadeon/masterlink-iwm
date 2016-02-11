package org.mlink.iwm.lookup;

import org.apache.log4j.Logger;
import java.util.Map;
import java.util.Collection;
import org.mlink.iwm.lookup.CodeLookupValues;
import org.mlink.iwm.util.Config;

public class ObjectClassSchemaRef extends CodeLookupValues{
    private static final Logger logger = Logger.getLogger(ObjectClassSchemaRef.class);
	public static Integer schemaIdTop() {return 6;}
	public static Integer schemaIdBottom() {return 9;}
	public static int schemaIdTop = 1;
    public static int schemaIdBottom;
    static{
        try{
            schemaIdTop = Integer.parseInt(Config.getProperty(Config.LOCATOR_SCHEMA_TOP));
        }catch(NumberFormatException e){
            logger.error("locator.schema.top property must be defined in the configuration properties system_props table");
        }
    }
    
    public String getSql() {
        return "SELECT * from  SCHEMA_REF WHERE SCHEMA_TYPE='C' ORDER BY DISP_ORD";
    }

    public void addOption(Map map){
        Object value = map.get("ID");
        Object label = map.get("DESCRIPTION");
        if(value == null || label == null )
            logger.error(getSql() + " returned invalid data " + map);
        else
            addOption(new OptionItem(value.toString(),label.toString()));

        schemaIdBottom = Integer.parseInt(value.toString());
    }

    public static String getLabel(Integer id){
        ObjectClassSchemaRef me = (ObjectClassSchemaRef) LookupMgr.getInstance(ObjectClassSchemaRef.class);
        return  me.getLabel(id==null?null:String.valueOf(id));
    }

    public static ObjectClassSchemaRef getInstance(){
        return (ObjectClassSchemaRef) LookupMgr.getInstance(ObjectClassSchemaRef.class);
    }


    public Collection getDefault() {
        return null;
    }
    

}


