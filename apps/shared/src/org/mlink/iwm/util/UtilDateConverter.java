package org.mlink.iwm.util;

import org.apache.commons.beanutils.Converter;
import org.apache.commons.beanutils.ConversionException;
import org.apache.log4j.Logger;
import org.mlink.iwm.util.CopyUtils;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.text.ParseException;


/**
 * <p>Standard {@link org.apache.commons.beanutils.Converter} implementation that converts an incoming
 * String into a <code>java.sql.Date</code> object, optionally using a
 * default value or throwing a {@link org.apache.commons.beanutils.ConversionException} if a conversion
 * error occurs.</p>
 */

public final class UtilDateConverter implements Converter {
    private static final Logger logger = Logger.getLogger(UtilDateConverter.class);


    /**
     * The default value specified to our Constructor, if any.
     */
    private static String datePattern = Config.getProperty(Config.YEAR4D_DATE_PATTERN);
    private static SimpleDateFormat sdf = null;



    // ----------------------------------------------------------- Constructors


    /**
     * Create a {@link org.apache.commons.beanutils.Converter} that will throw a {@link org.apache.commons.beanutils.ConversionException}
     * if a conversion error occurs.
     */
    public UtilDateConverter() {
        logger.debug("registering " + this.getClass());
        sdf = new SimpleDateFormat(datePattern);
    }



    public UtilDateConverter(String pattern) {
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

        if (value == null){
            rtn = null;
        }else if (value instanceof Date) {
            rtn =  value;
        }else if (value instanceof String) {
            String str = ((String)value).trim();
            if(str.length()==0){
                rtn = CopyUtils.isBaseVOTarget()?CopyUtils.NullAliasValues.get(CopyUtils.NullAlias.UtilDate):null;
            }else{
                rtn = parse(str);
            }
        }else{
            throw new ConversionException("SqlDateConverter: unsupported class type " + value.getClass());
        }
        return rtn;

    }

    private Object parse(String date) throws ConversionException{
        java.util.Date convValue;
        try {
            convValue = sdf.parse(date);
        } catch (ParseException e) {
            throw new ConversionException(e.getMessage() +  ". UtilDateConverter: string does not represent valid date " + date);
        }
//        if(!date.equals(sdf.format(convValue))) //I don't see why this check is needed -Chris
//            throw new ConversionException("UtilDateConverter: string does not represent valid date " + date);
        return new java.util.Date(convValue.getTime());
    }

    public static boolean  isValidDate(String date){
        try{
            java.util.Date convValue = sdf.parse(date);
             if(!date.equals(sdf.format(convValue))) return false;
        } catch (ParseException e) {
            return false;
        }
        return true;
    }


    public static String getDatePattern(){
        return datePattern;
    }

    public static void setDatePattern(String pattern){
        datePattern = pattern;
        sdf.applyPattern(datePattern);
    }


}
