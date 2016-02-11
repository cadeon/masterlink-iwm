package org.mlink.iwm.dwr;

import org.mlink.iwm.util.DBAccess;
import org.mlink.iwm.bean.KeyValuePair;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;

/**
 * User: andrei
 * Date: Nov 20, 2006
 * This class is used to retireve additional information for  the List pages as for count of actions, tasks
 * Not in use yet!
 */
public class Counters {

    public KeyValuePair getNumberOfActionsForTask(Long taskId) throws Exception{
        List params = new ArrayList(); params.add(taskId);
        List <Map> result = DBAccess.executeQuery("SELECT A.TASK_ID ID, COUNT(A.ID) CNT FROM ACTION A WHERE A.TASK_ID=? GROUP BY A.TASK_ID",params,false);
        //return Integer.parseInt(result.get(0).get("CNT").toString());
        return new KeyValuePair(taskId, result.get(0).get("CNT"));
    }
}
