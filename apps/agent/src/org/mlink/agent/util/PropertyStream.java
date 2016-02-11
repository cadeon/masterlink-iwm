package org.mlink.agent.util;

import java.io.InputStream;

public class PropertyStream {

	private PropertyStream(){}
    public static InputStream getPropertyResourceAsStream(String fileName) throws Exception {
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        InputStream is =  cl.getResourceAsStream(fileName);
        if(is==null) { // or try this one
            is = PropertyStream.class.getClassLoader().getResourceAsStream(fileName);
        }
        if(is==null) {
        	is = PropertyStream.class.getResourceAsStream(fileName);
        }
        if(is==null) throw new Exception("Property file "+ fileName +" not found");
        return is;
    }
}
