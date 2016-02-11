package org.mlink.iwm.lookup;

import org.apache.log4j.Logger;

import java.util.Map;
import java.util.Collection;

/**
 * Created by IntelliJ IDEA.
 * User: andreipovodyrev
 * Date: Nov 15, 2007
 */
public class WorkerCertificationRef extends CQLSRef{
    private static final Logger logger = Logger.getLogger(WorkerCertificationRef.class);
    public static final String SYSTEM = "System";
    public static final String MANUAL = "Manual";

    public String getSql() {
        return "SELECT * FROM CQLS_REF ORDER BY DISP_ORD WHERE TYPE='"+CERTIFICATION_TYPE+ "'";
    }

    public void addOption(Map map){
        Object value = map.get("ID");
        Object label = map.get("CODE");
        if(value == null || label == null )
            logger.error(getSql() + " returned invalid data " + map);
        else
            addOption(new OptionItem(value.toString(),label.toString(), label.toString()));
    }

    public static String getLabel(Integer id){
        WorkerCertificationRef me = (WorkerCertificationRef) LookupMgr.getInstance(WorkerCertificationRef.class);
        return  me.getLabel(id==null?null:String.valueOf(id));
    }
    public static int getIdByCode(String code){
        WorkerCertificationRef me = (WorkerCertificationRef) LookupMgr.getInstance(WorkerCertificationRef.class);
        return  me.getValueByCode(code).intValue();
    }

    public Collection getDefault() {
        return null;
    }
}
