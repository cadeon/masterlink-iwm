/**
 * Created by Andrei Povodyrev
 * User: andrei 
 * Date: Mar 21, 2004
 */
package org.mlink.iwm.lookup;


import org.mlink.iwm.util.DBAccess;
import org.apache.log4j.Logger;

import java.util.Collection;
import java.sql.SQLException;

public class CDLVLoader {
	//FIXME:  why create an instance of a class that has only static
	//        methods? (apart from a private constructor)
    private static CDLVLoader ourInstance;
    private static final Logger logger = Logger.getLogger(CDLVLoader.class);


    public synchronized static CDLVLoader getInstance() {
        if (ourInstance == null) {
            ourInstance = new CDLVLoader();
        }
        return ourInstance;
    }

    private CDLVLoader() {
    }

    public static Collection load(CodeLookupValues obj) {
        //List results = DBAccess.execute(cdlv.getSql());
        Collection results = obj.getDefault();
        if(results == null){
             try{
                 results = DBAccess.execute(obj.getSql(),false);
             }catch(SQLException e){
                 e.printStackTrace();
                 logger.error("error while loading " + obj.getClass().getName() + ":" + e.getMessage());
             }
        }
        return results;
    }
}

