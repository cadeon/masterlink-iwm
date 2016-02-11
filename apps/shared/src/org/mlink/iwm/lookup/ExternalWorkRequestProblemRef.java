package org.mlink.iwm.lookup;

import org.apache.log4j.Logger;
import org.mlink.iwm.util.Config;

import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Andrei
 * Date: May 1, 2006
 */
public class ExternalWorkRequestProblemRef extends CodeLookupValues{
    private static final Logger logger = Logger.getLogger(ExternalWorkRequestProblemRef.class);
    public String getSql() {
        return "SELECT * FROM TASK_DEF WHERE OBJECT_DEF_ID IN("+
                Config.getProperty(Config.EXT_REQUEST_OBJECT_DEFINITION_ID)+"," +
                Config.getProperty(Config.AREA_OBJECT_DEFINITION_ID)+")" +
                " ORDER BY DESCRIPTION ASC";
    }


    public void addOption(Map map){
        Object value = map.get("ID");
        Object label = map.get("DESCRIPTION");
        if(value == null || label == null)
            logger.error(getSql() + " returned invalid data " + map);
        else
            addOption(new OptionItem(value.toString(),label.toString()));
    }

    public static String getLabel(Long id){
        ExternalWorkRequestProblemRef me = (ExternalWorkRequestProblemRef) LookupMgr.getInstance(ExternalWorkRequestProblemRef.class);
        return  me.getLabel(id==null?null:String.valueOf(id));
    }
}
