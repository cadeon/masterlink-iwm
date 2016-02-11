package org.mlink.iwm.lookup;

import org.apache.log4j.Logger;

import java.util.Map;
import java.util.Collection;

/**
 * User: andreipovodyrev
 * Date: Nov 15, 2007
 */
public class CQLSRef extends CodeLookupValues{
    private static final Logger logger = Logger.getLogger(CQLSRef.class);

    public static final String CERTIFICATION_TYPE = "CERTIFICATION";
    public static final String LICENSE_TYPE = "LICENSE";
    public static final String QUALIFICATION_TYPE = "QUALIFICATION";

        public String getSql() {
        return "SELECT ID,CODE,TYPE FROM CQLS_REF ORDER BY DISP_ORD";
    }

    public void addOption(Map map){
        Object value = map.get("ID");
        Object label = map.get("CODE");
        Object type = map.get("TYPE");
        if(value == null || label == null )
            logger.error(getSql() + " returned invalid data " + map);
        else
            addOption(new OptionItem(value.toString(),label.toString(), type.toString()));
    }

    public static String getType(Integer id){
        CQLSRef me = (CQLSRef) LookupMgr.getInstance(CQLSRef.class);
        return  me.getCode(id==null?null:String.valueOf(id));
    }

    public static String getLabel(Integer id){
        CQLSRef me = (CQLSRef) LookupMgr.getInstance(CQLSRef.class);
        return  me.getLabel(id==null?null:String.valueOf(id));
    }

    public Collection getDefault() {
        return null;
    }

}
