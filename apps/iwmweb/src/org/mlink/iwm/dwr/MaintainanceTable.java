package org.mlink.iwm.dwr;

import org.mlink.iwm.bean.ResponsePage;
import java.util.HashMap;

/**
 * User: jmirick
 * Date: Oct 18, 2006
 */
public interface MaintainanceTable extends ReturnCodes{
    /**
     * @param criteria
     * @param offset
     * @param pageSize
     * @param orderBy
     * @param orderDirection
     * @return
     * @throws Exception
     */
    public ResponsePage getData(HashMap criteria, int offset, int pageSize, String orderBy, String orderDirection)  throws Exception;

    /**
     *
     * @param itemId
     * @return  String returns optional message
     * @throws Exception
     */
    public String deleteItem(Long itemId) throws Exception;

    //public Object getItem(Long itemId) throws Exception;    //causes log clutter due to overloading in children which confuses DWR js to java conversion-> [ExecuteQuery] Warning multiple matching methods. Using first match

    /**
     *  DWR cannot use reflection on interface. Using HashMap to get around
     * @param bean
     * @throws Exception
     * @return  String returns optional message
     * Todo save methods should return String in case server side validation messages
     * UI should check for return and throw alert if needed. Need to discuss with John M
     */
    public String saveItem(HashMap bean) throws Exception;
}