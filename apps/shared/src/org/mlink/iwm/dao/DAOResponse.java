package org.mlink.iwm.dao;

import org.mlink.iwm.util.CopyUtils;

import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * User: andrei
 * Date: Nov 13, 2006
 */
public class DAOResponse {
    protected List <Map> rows = new ArrayList<Map>();

    public DAOResponse() {
    }

    public DAOResponse(List<Map> rows) {
        this.rows = rows;
    }

    public List<Map> getRows() {
        return rows;
    }

    public <E> List <E> convertRowsToClasses(Class <E> clazz) throws Exception{
        return (List <E>) CopyUtils.copyProperties(clazz,getRows());
    }

    public List <String> convertRowsToHtml() throws Exception{
        List <String> rtn = new ArrayList <String> ();
        for (int i = 0; i < rows.size(); i++) {
            Map map =  rows.get(i);
            Iterator it = map.keySet().iterator();
            StringBuilder tmp = new StringBuilder();
            while (it.hasNext()) {
                String key = (String)it.next();
                tmp.append(" ").append(map.get(key));
            }
            rtn.add(tmp.toString());
        }

        return rtn;
    }
}
