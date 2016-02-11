package org.mlink.agent.util;

import java.util.Properties;

import org.apache.log4j.Logger;

public class AgentConfig {
	private static final Logger logger = Logger.getLogger(AgentConfig.class);
	private static final String PROPERTY_FILE = "agentconfig.properties"; 
	private static Properties properties;
	
	private static int batchPage=-999;
	private static int shiftConst=-999;
	private static int location=-999;
	private static int building=-999;
	private static int zone=-999;
	private static int floor=-999;
	private static int room=-999;
	
	static {
		try {
			properties = new Properties();
			properties.load(PropertyStream.getPropertyResourceAsStream(PROPERTY_FILE));
		} catch (Exception e) {
			logger.error("Error loading agent config properties",e);
		}
	}
	
	public static String get(String property){
		if (properties==null) throw new RuntimeException("Agent config properties not loaded");
		return properties.getProperty(property);
	}
	
	// Batch paging size constant
	public static int batchPageConst(){
		if (batchPage!=-999) return batchPage;
		String s = properties.getProperty("batchPage");
		try {
			batchPage = Integer.parseInt(s);
		} catch (Exception e) { 
			logger.error("Tried to parse "+s+" as batchPage",e);
			batchPage = 20;
		}
		return batchPage;
	}
	
	// Shift end constant
	public static int shiftConst() {
		if (shiftConst!=-999) return shiftConst;
		String s = properties.getProperty("shiftConst");
		try {
			shiftConst = Integer.parseInt(s);
		} catch (Exception e) { 
			logger.error("Tried to parse "+s+" as shiftConst",e);
			shiftConst = 6;
		}
		return shiftConst;
	}

	// Travel time constants
	public static int locationConst() {
		if (location!=-999) return location;
		String s = properties.getProperty("location");
		try {
			location = Integer.parseInt(s);
		} catch (Exception e) { 
			logger.error("Tried to parse "+s+" as location travel time const",e);
			location = 30;
		}
		return location;
	}
	public static int buildingConst() {
		if (building!=-999) return building;
		String s = properties.getProperty("building");
		try {
			building = Integer.parseInt(s);
		} catch (Exception e) {  
			logger.error("Tried to parse"+s+" as building travel time const",e);
			building = 30;
		}
		return building;
	}
	public static int zoneConst() {
		if (zone!=-999) return zone;
		String s = properties.getProperty("zone");
		try {
			zone = Integer.parseInt(s);
		} catch (Exception e) { 
			logger.error("Tried to parse "+s+" as zone travel time const",e);
			zone = 5;
		}
		return zone;
	}
	public static int floorConst() {
		if (floor!=-999) return floor;
		String s = properties.getProperty("floor");
		try {
			floor = Integer.parseInt(s);
		} catch (Exception e) { 
			logger.error("Tried to parse "+s+" as floor travel time const",e);
			floor = 1;
		}
		return floor;
	}
	public static int roomConst() {
		if (room!=-999) return room;
		String s = properties.getProperty("room");
		try {
			room = Integer.parseInt(s);
		} catch (Exception e) { 
			logger.error("Tried to parse "+s+" as room travel time const",e);
			room = 1;
		}
		return room;
	}
	
	// Utility calculation constants
    public static String kValueConst(){
    	String s = properties.getProperty("kValue");
    	if (s==null) s = "0.5";
    	return s;
    }
    public static String userNptConst(){
    	String s = properties.getProperty("userNpt");
		if (s==null) s = "0.9";
		return s;
	}
    public static String userGradeConst(){
    	String s = properties.getProperty("userGrade");
    	if (s==null) s = "0.9";
    	return s;
    }
    public static String maxGradeConst(){
    	String s = properties.getProperty("maxGrade");
    	if (s==null) s = "5";
    	return s;
    }
    public static String maxPriorityConst(){
    	String s = properties.getProperty("maxPriority");
    	if (s==null) s = "25";
    	return s;
    }
    
    private static void log(Object o) {
    	logger.debug(o);
    }
}
