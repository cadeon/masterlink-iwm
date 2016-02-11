package org.mlink.iwm.util;

import org.apache.commons.beanutils.Converter;
import org.apache.log4j.Logger;

import java.text.SimpleDateFormat;


/**
 * <p>Standard {@link org.apache.commons.beanutils.Converter} implementation that converts an incoming
 * String into a <code>java.lang.String</code> object, optionally using a
 * default value or throwing a {@link org.apache.commons.beanutils.ConversionException} if a conversion
 * error occurs.</p>
 */

public final class StringConverter implements Converter {
    private static final Logger logger = Logger.getLogger(StringConverter.class);


    // --------------------------------------------------------- Public Methods

    public StringConverter() {
        logger.debug("registering " + this.getClass());
    }

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
            rtn = value;
        }else if(value instanceof java.sql.Date){
            rtn = formatDate((java.sql.Date)value);
        }else if(value instanceof java.sql.Timestamp){
            rtn =  formatDate((java.sql.Timestamp)value);
        }else if(value instanceof java.util.Date){
            rtn =  formatDate((java.util.Date)value);
        }else if (value instanceof String) {
            String str = ((String)value).trim();
            //if(str.length()==0){
            //    rtn = null;
            //}else{
                rtn = str;
            //}
        }else{
            rtn = value.toString();
        }
        return rtn;

    }



    public static String formatDate(java.sql.Timestamp date){
        return (new SimpleDateFormat(SQLTimestampConverter.getDatePattern())).format(date);
    }

    public static String formatDate(java.util.Date date){
        return (new SimpleDateFormat(UtilDateConverter.getDatePattern())).format(date);
    }


}

