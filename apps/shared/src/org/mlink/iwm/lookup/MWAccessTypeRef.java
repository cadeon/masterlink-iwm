package org.mlink.iwm.lookup;

import java.util.Collection;
import java.util.Map;

import org.apache.log4j.Logger;

/**
 * User: andreipovodyrev
 * Date: Nov 15, 2007
 */
public class MWAccessTypeRef extends CodeLookupValues{
    private static final Logger logger = Logger.getLogger(MWAccessTypeRef.class);
    
    public enum Type {UIA, MWL, MWD, MWU, MWSS, MWES, MWSB, MWEB, MWST, MWET;
    
		public boolean equals(String str){
	        return this.toString().equals(str);
	    }
    }
    
    public static final String UI_TYPE = "UI";
    public static final String MW_TYPE = "MW";

        public String getSql() {
        	return "SELECT ID,CODE,DESCRIPTION FROM MW_ACCESS_TYPE_REF ORDER BY DISP_ORD";
    }

    public void addOption(Map map){
        Object value = map.get("ID");
        Object label = map.get("DESCRIPTION");
        Object code = map.get("CODE");
        if(value == null || label == null )
            logger.error(getSql() + " returned invalid data " + map);
        else
            addOption(new OptionItem(value.toString(),label.toString(), code.toString()));
    }

    public static String getType(Integer id){
        MWAccessTypeRef me = (MWAccessTypeRef) LookupMgr.getInstance(MWAccessTypeRef.class);
        return  me.getCode(id==null?null:String.valueOf(id));
    }

    public static String getLabel(Integer id){
        MWAccessTypeRef me = (MWAccessTypeRef) LookupMgr.getInstance(MWAccessTypeRef.class);
        return  me.getLabel(id==null?null:String.valueOf(id));
    }

    public Collection getDefault() {
        return null;
    }
    
    public static int getIdByCode(String code){
        MWAccessTypeRef me = (MWAccessTypeRef) LookupMgr.getInstance(MWAccessTypeRef.class);
        return  me.getValueByCode(code).intValue();
    }
    
    public static int getIdByCode(MWAccessTypeRef.Type code){
        return  getIdByCode(code.toString());
    }
}
