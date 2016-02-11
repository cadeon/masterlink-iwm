package org.mlink.iwm.util;



import javax.servlet.ServletRequest;
import java.util.Map;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.Iterator;

import org.apache.struts.action.ActionForm;
import org.apache.commons.beanutils.PropertyUtils;

public class ForwardUtils {

    private static final char
            QUESTION_MARK = '?',
            EQUALS = '=';

    /*----------------------------------------*
    * class methods
    *----------------------------------------*/


    private static void debug(String str){
        System.out.println(str);
    }

    /**
     * This method parses the query string and fills in missing prametrer values using the current form
     * object. That is /ReadFoo?fooId= will be transformed to  /ReadFoo?fooId=value if fooId property is
     * present in the current form
     */
    public static String fillInQueryString(String path, ServletRequest request)
    {
        Map mapParams = new HashMap();

        // tokenize the name=value pairs

        int queryStart = path.indexOf(QUESTION_MARK);

        if (queryStart == -1)          {
            return path;
        }

        StringTokenizer paramTokens = new StringTokenizer(path.substring(queryStart + 1), "&");
        while (paramTokens.hasMoreTokens())
        {
            String queryPair = paramTokens.nextToken();
            int equalsIndex = queryPair.indexOf(EQUALS);
            if (equalsIndex ==  -1)
            {
                debug("name=value pair badly formatted");
                continue;
            }

            // get the name and value and put in the map
            String name = queryPair.substring(0, equalsIndex);
            equalsIndex++;
            String  value = queryPair.substring(equalsIndex);
            if(value.length() == 0){
                ActionForm aform = (ActionForm)request.getAttribute("CURRENT_FORM");
                if(aform != null){
                    try {
                        Object propValue = PropertyUtils.getProperty(aform, name);
                        if(propValue!=null) value = propValue.toString();
                    } catch (Exception e) {
                        System.out.println("\nWarning! property "  + name + " not found in form " + aform.getClass().getName());
                    }
                }else{
                    value = request.getParameter(name);
                }

            }
            mapParams.put(name,value==null?"":value);
        }

        Iterator it = mapParams.keySet().iterator();
        StringBuffer newPath = new StringBuffer(path.substring(0,queryStart + 1));
        String separator = "";
        while (it.hasNext()) {
            String key =(String) it.next();
            String value =(String)mapParams.get(key);
            if(value !=null){
                newPath.append(separator + key + EQUALS + mapParams.get(key));
                separator = "&";
            }
        }
        return newPath.toString();
    }

}


