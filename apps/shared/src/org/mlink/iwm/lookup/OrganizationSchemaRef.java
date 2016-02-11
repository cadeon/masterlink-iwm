package org.mlink.iwm.lookup;

import org.apache.log4j.Logger;

import java.util.Map;
import java.util.Collection;

public class OrganizationSchemaRef extends CodeLookupValues{
    private static final Logger logger = Logger.getLogger(OrganizationSchemaRef.class);
    public static int schemaIdTop = 20;
    public static int schemaIdBottom = 23;
    public String getSql() {
        return "SELECT * from  SCHEMA_REF WHERE SCHEMA_TYPE='O' ORDER BY DISP_ORD";
    }

    public void addOption(Map map){

        Object value = map.get("ID");
        Object label = map.get("DESCRIPTION");
        if(value == null || label == null )
            logger.error(getSql() + " returned invalid data " + map);
        else
            addOption(new OptionItem(value.toString(),label.toString()));

        if(schemaIdTop==0) schemaIdTop = Integer.parseInt(value.toString());  // first aoption sets to schema id
        schemaIdBottom = Integer.parseInt(value.toString());
    }

    public static String getLabel(Integer id){
        OrganizationSchemaRef me = (OrganizationSchemaRef) LookupMgr.getInstance(OrganizationSchemaRef.class);
        return  me.getLabel(id==null?null:String.valueOf(id));
    }

    public static OrganizationSchemaRef getInstance(){
        return (OrganizationSchemaRef) LookupMgr.getInstance(OrganizationSchemaRef.class);
    }

    public Collection getDefault() {
        return null;
    }

}


