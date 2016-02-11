package org.mlink.iwm.base;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.log4j.Logger;
import org.mlink.agent.WorldConnection;
import org.mlink.iwm.util.CopyUtils;
import org.mlink.iwm.util.DoubleConverter;
import org.mlink.iwm.util.FloatConverter;
import org.mlink.iwm.util.IntegerConverter;
import org.mlink.iwm.util.LongConverter;
import org.mlink.iwm.util.SQLDateConverter;
import org.mlink.iwm.util.SQLTimestampConverter;
import org.mlink.iwm.util.StringConverter;
import org.mlink.iwm.util.UtilDateConverter;

public class BaseAccess{
    private static final Logger logger = Logger.getLogger(BaseAccess.class);
    private static WorldConnection worldConnection;

    static{
        ConvertUtils.register(new UtilDateConverter(), java.util.Date.class);
        ConvertUtils.register(new SQLDateConverter(), java.sql.Date.class);
        ConvertUtils.register(new SQLTimestampConverter(), java.sql.Timestamp.class);
        ConvertUtils.register(new StringConverter(), java.lang.String.class);
        ConvertUtils.register(new LongConverter(), java.lang.Long.class);
        ConvertUtils.register(new DoubleConverter(), java.lang.Double.class);
        ConvertUtils.register(new FloatConverter(), java.lang.Float.class);
        ConvertUtils.register(new IntegerConverter(), java.lang.Integer.class);
    }

    public static WorldConnection getWorldConnection(){return worldConnection;}
    public static void setWorldConnection(WorldConnection connection){worldConnection=connection;}
    
    protected static boolean isNullAlias(java.util.Date value){
        return CopyUtils.isNullAlias(value);
    }

    protected static boolean isNullAlias(java.sql.Date value){
        return CopyUtils.isNullAlias(value);
    }

    protected static boolean isNullAlias(java.sql.Timestamp value){
        return CopyUtils.isNullAlias(value);
    }
    protected static boolean isNullAlias(Integer value){
        return CopyUtils.isNullAlias(value);
    }
    protected static boolean isNullAlias(Float value){
        return CopyUtils.isNullAlias(value);
    }
    protected static boolean isNullAlias(Double value){
        return CopyUtils.isNullAlias(value);
    }
    protected static boolean isNullAlias(Long value){
        return CopyUtils.isNullAlias(value);
    }
    protected static boolean isNullAlias(String value){
        return false;
    }
    protected static boolean isNullAlias(Object value){
        if(value instanceof java.sql.Date) return isNullAlias((java.sql.Date)value);
        else if(value instanceof java.sql.Timestamp) return isNullAlias((java.sql.Timestamp)value);
        else if(value instanceof java.util.Date) return isNullAlias((java.util.Date)value);
        else if(value instanceof Integer) return isNullAlias((Integer)value);
        else if(value instanceof Float) return isNullAlias((Float)value);
        else if(value instanceof Double) return isNullAlias((Double)value);
        else if(value instanceof Long) return isNullAlias((Long)value);
        else
            return false;
    }
}

