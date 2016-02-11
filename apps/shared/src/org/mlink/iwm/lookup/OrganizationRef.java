package org.mlink.iwm.lookup;

import org.apache.log4j.Logger;

import java.util.Map;

/**
 * Created by Andrei Povodyrev
 * Date: Jul 17, 2004
 */
public class OrganizationRef extends CodeLookupValues{
    private static final Logger logger = Logger.getLogger(OrganizationRef.class);

    public void init() {
    }

    public String getSql() {
        return "SELECT O.ID, P.NAME, O.SCHEMA_ID, O.PARENT_ID from  ORGANIZATION O, PARTY P WHERE O.PARTY_ID=P.ID AND O.ARCHIVED_DATE IS NULL ORDER BY NAME";
    }


    public void addOption(Map map){
        Object value = map.get("ID");
        Object label = map.get("NAME");
        Object schema = map.get("SCHEMA_ID");
        Object parent = map.get("PARENT_ID");
        Long parentId = parent==null?null:(new Long(parent.toString()));
        if(value == null || label == null )
            logger.error(getSql() + " returned invalid data " + map);
        else{
            OptionItem option = new OptionItem(value.toString(),label.toString());
            option.addProperty("schema",schema==null?null:Integer.parseInt(schema.toString()));
            addOption(option);
            option.addProperty("parentId", parentId);
        }

    }

    public static Integer getSchemaId(Long id){
        OrganizationRef me = (OrganizationRef) LookupMgr.getInstance(OrganizationRef.class);
        OptionItem item =me.getOptionItem(String.valueOf(id));
        return (Integer)item.getProperty("schema");
    }

    public static String getLabel(Long id){
        OrganizationRef me = (OrganizationRef) LookupMgr.getInstance(OrganizationRef.class);
        return  me.getLabel(id==null?null:String.valueOf(id));
    }
    
    public static Long getParentId(Long id){
    	OrganizationRef me = (OrganizationRef) LookupMgr.getInstance(OrganizationRef.class);
    	OptionItem item =me.getOptionItem(id.toString());
    	Long parent = (Long) item.getProperty("parentId");
    	return parent;	
    }

}
