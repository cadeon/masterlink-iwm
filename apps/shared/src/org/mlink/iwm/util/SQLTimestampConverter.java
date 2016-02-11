package org.mlink.iwm.util;

import org.apache.commons.beanutils.Converter;
import org.apache.commons.beanutils.ConversionException;
import org.apache.log4j.Logger;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.text.ParseException;


/**
 * <p>Standard {@link org.apache.commons.beanutils.Converter} implementation that converts an incoming
 * String into a <code>java.sql.Date</code> object, optionally using a
 * default value or throwing a {@link org.apache.commons.beanutils.ConversionException} if a conversion
 * error occurs.</p>
 */

public final class SQLTimestampConverter implements Converter {
    private static final Logger logger = Logger.getLogger(SQLTimestampConverter.class);

    /**
     * The default value specified to our Constructor, if any.
     */
    private static String datePattern = Config.getProperty(Config.DATE_PATTERN);
    //private static String datePatternT = Config.getProperty(Config.TIME_PATTERN);
    private static SimpleDateFormat sdf = null;



    // ----------------------------------------------------------- Constructors


    /**
     * Create a {@link org.apache.commons.beanutils.Converter} that will throw a {@link org.apache.commons.beanutils.ConversionException}
     * if a conversion error occurs.
     */
    public SQLTimestampConverter() {
        logger.debug("registering " + this.getClass());
        sdf = new SimpleDateFormat(datePattern);

    }


    public SQLTimestampConverter(String pattern) {
        logger.debug("registering " + this.getClass());
        sdf = new SimpleDateFormat(pattern);
    }

    // --------------------------------------------------------- Public Methods


    /**
     * Convert the specified input object into an output object of the
     * specified type.
     *
     * @param type Data type to which this value should be converted
     * @param value The input value to be converted
     *
     * @exception org.apache.commons.beanutils.ConversionException if conversion cannot be performed
     *  successfully
     */

    public Object convert(Class type, Object value) {
        Object rtn;
        if (value == null){  return null;
            //rtn = CopyUtils.isBaseVOTarget()?CopyUtils.IgnoredValues.get(CopyUtils.NullAlias.SQLTime):null;
            //rtn =  CopyUtils.IgnoredValues.get(CopyUtils.NullAlias.SQLTime);
        }else if (value instanceof Date) {
            rtn =  value;
        }else if (value instanceof Timestamp) {
            rtn =  value;
        }else if (value instanceof String) {
            String str = ((String)value).trim();
            if(str.length()==0){
                rtn = CopyUtils.isBaseVOTarget()?CopyUtils.NullAliasValues.get(CopyUtils.NullAlias.SQLTime):null;
            }else{
                rtn = parse(str);
            }
        }else{
            throw new ConversionException("TimestampConverter: unsupported class type " + value.getClass());
        }
        return rtn;

    }


    private Object parse(String date) throws ConversionException{
        java.util.Date convValue;
        try {
            convValue = sdf.parse(date);
        } catch (ParseException e) {
            throw new ConversionException(e.getMessage() +  ". TimestampConverter: string does not represent valid date " + date);
        }
        if(!date.equals(sdf.format(convValue)))
            throw new ConversionException("TimestampConverter: string does not represent valid date " + date);
        return new java.sql.Timestamp(convValue.getTime());
    }

    public static String getDatePattern(){
        return datePattern;
    }

    public static void setDatePattern(String pattern){
        datePattern = pattern;
        sdf.applyPattern(datePattern);
    }


}
