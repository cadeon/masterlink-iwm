package org.mlink.iwm.lookup;

import org.apache.log4j.Logger;
import java.util.Map;

public class ProjectStatusRef extends CodeLookupValues{
    private static final Logger logger = Logger.getLogger(ProjectStatusRef.class);
    public static final String PREPARING = "Preparing";
    public static final String CANCELLED = "Cancelled";
    public static final String COMPLETED = "Completed";
    public static final String STARTED = "Started";


    public String getSql() {
        return "SELECT * from  PROJECT_STATUS_REF ORDER BY DISP_ORD";
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

    public static String getLabel(Integer id){
        ProjectStatusRef me = (ProjectStatusRef) LookupMgr.getInstance(ProjectStatusRef.class);
        return  me.getLabel(id==null?null:String.valueOf(id));
    }

    public static int getIdByCode(String code){
        ProjectStatusRef me = (ProjectStatusRef) LookupMgr.getInstance(ProjectStatusRef.class);
        return  me.getValueByCode(code).intValue();
    }

    public static boolean isFinalState(Integer id){
        String state = getLabel(id);
        return COMPLETED.equals(state) || CANCELLED.equals(state);
    }

}
