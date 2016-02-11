package org.mlink.iwm.lookup;

import org.apache.log4j.Logger;
import java.util.Map;
import java.util.Collection;

public class WorkerStatusRef extends CodeLookupValues{
    private static final Logger logger = Logger.getLogger(WorkerStatusRef.class);
    public static final String AVAILABLE   = "AVAILABLE";

    public String getSql() {
        return "SELECT * from  WORKER_STATUS_REF ORDER BY DISP_ORD";
    }

    public void addOption(Map map){
        Object value = map.get("ID");
        Object code = map.get("CODE");
        Object label = map.get("DESCRIPTION");
        if(value == null || label == null || code ==null)
            logger.error(getSql() + " returned invalid data " + map);
        else
            addOption(new OptionItem(value.toString(),label.toString(), code.toString()));
    }

    public static String getLabel(Integer id){
        WorkerStatusRef me = (WorkerStatusRef) LookupMgr.getInstance(WorkerStatusRef.class);
        return  me.getLabel(id==null?null:String.valueOf(id));
    }

    public static int getIdByCode(String code){
        WorkerStatusRef me = (WorkerStatusRef) LookupMgr.getInstance(WorkerStatusRef.class);
        return  me.getValueByCode(code).intValue();
    }

    public Collection getDefault() {
        return null;
    }

}
