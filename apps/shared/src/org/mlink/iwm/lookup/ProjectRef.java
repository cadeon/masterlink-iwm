package org.mlink.iwm.lookup;

import org.apache.log4j.Logger;

import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: andreipovodyrev
 * Date: Aug 27, 2007
 */
public class ProjectRef extends CodeLookupValues{
    private static final Logger logger = Logger.getLogger(ProjectRef.class);

    public void init() {
    }

    public String getSql() {
        return "SELECT P.ID, P.NAME FROM PROJECT P WHERE P.ARCHIVED_DATE IS NULL AND PARENT_ID IS NULL ORDER BY NAME";
    }

    public void addOption(Map map){
        Object value = map.get("ID");
        Object label = map.get("NAME");
        if(value == null || label == null )
            logger.error(getSql() + " returned invalid data " + map);
        else{
            OptionItem option = new OptionItem(value.toString(),label.toString());
            addOption(option);
        }

    }

}
