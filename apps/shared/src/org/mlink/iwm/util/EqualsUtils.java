package org.mlink.iwm.util;

public final class EqualsUtils {

    static public boolean areEqual(boolean aThis, boolean aThat){
        return aThis == aThat;
    }

    static public boolean areEqual(char aThis, char aThat){
        return aThis == aThat;
    }

    static public boolean areEqual(long aThis, long aThat){
        /*
        * Implementation Note
        * Note that byte, short, and int are handled by this method, through
        * implicit conversion.
        */
        return aThis == aThat;
    }

    static public boolean areEqual(float aThis, float aThat){
        return Float.floatToIntBits(aThis) == Float.floatToIntBits(aThat);
    }

    static public boolean areEqual(double aThis, double aThat){
        return Double.doubleToLongBits(aThis) == Double.doubleToLongBits(aThat);
    }

    /**
     * Possibly-null object field.
     *
     * Includes type-safe enumerations and collections, but does not include
     * arrays. See class comment.
     */
    static public boolean areEqual(Object aThis, Object aThat){
        return aThis == null ? aThat == null : aThis.equals(aThat);
    }
    
    /**
     * Determines if the given Object is null, or contains the empty string
     * 
     * @param o Object to test
     * @return true if the the Object o is null, or if "".equals(o)
     * author mike
     *
     */
    public static boolean noe(Object o)  {
    	if (o==null || "".equals(o))
    		return true;
    	return false;	
    }
}
