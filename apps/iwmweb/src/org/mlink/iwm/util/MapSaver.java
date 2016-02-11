package org.mlink.iwm.util;

import java.io.*;
import java.util.*;
import org.apache.log4j.Logger;

public class MapSaver 
{
	private static final Logger logger = Logger.getLogger(MapSaver.class);
	
	public static synchronized void save ( int uid, Map m, OutputStream os, String who) {
        log("MapSaver:Save: "+ who);
        log("MapSaver:Save: map "+ m);
        save(uid,m,os);
    }
	public static synchronized void save (int uid, Map m, OutputStream os ) {
		save(uid,m,new OutputStreamWriter(os));
	}
	public static synchronized void save( int uid, Map m, Writer w ) {
		try {
			if ( m == null )
				throw new IllegalArgumentException("Map is null");
			if ( w == null )
				throw new IllegalArgumentException("Writer is null");
			PrintWriter ps;
			if ( w instanceof PrintWriter ) {
				ps = (PrintWriter)w;
			} else {
				ps = new PrintWriter(w);
			}
			ps.println(uid);
			for ( Iterator i = m.keySet().iterator(); i.hasNext(); ) {
				Object o = i.next();
				ps.println(o);
				Vector v = (Vector)m.get(o);
				if ( v == null ) {
                    ps.println(".");
					continue;
                }
				for ( Enumeration e = v.elements(); e.hasMoreElements(); ) {
					ps.println(e.nextElement());
				}
				ps.println(".");
			}
			ps.println(".");
			ps.flush();
		} catch ( Exception e ) {
			e.printStackTrace();
		}
	}
	public static synchronized Map load (Reader r ) {
		try {
			LineNumberReader dis = new LineNumberReader(r);
			String line = "";
			HashMap map = new HashMap();
			Vector v = null;
			line=dis.readLine();
			map.put("userid",line);
			while ( (line=dis.readLine())!=null ) {
				if ( v == null ) {
					if ( line.equals(".") )
						break;
					v = new Vector();
					map.put(line,v);
				} else if ( line.equals(".") ) {
					v = null;
				} else {
					v.addElement(line);
				}
			}
			return map;
		} catch ( Exception e ) {
			e.printStackTrace();
            return new HashMap();
		}
	}
	public static synchronized Map load ( InputStream is ) {
		return load(new InputStreamReader(is));
	}
	
	public static synchronized Map load(String param ) {
		return load(new StringReader(param));
	}

	private static void log(String s) {
		logger.debug(s);
	}
}

