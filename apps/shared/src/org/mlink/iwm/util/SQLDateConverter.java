package org.mlink.iwm.util;
import org.apache.commons.beanutils.Converter;
import org.apache.commons.beanutils.ConversionException;
import org.apache.log4j.Logger;
import org.mlink.iwm.util.CopyUtils;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.text.ParseException;


/**
 * <p>Standard {@link org.apache.commons.beanutils.Converter} implementation that converts an incoming
 * String into a <code>java.sql.Date</code> object, optionally using a
 * default value or throwing a {@link org.apache.commons.beanutils.ConversionException} if a conversion
 * error occurs.</p>
 */

public final class SQLDateConverter implements Converter {
    private static final Logger logger = Logger.getLogger(SQLDateConverter.class);

    /**
     * Should we return the default value on conversion errors?
     */
    private static String datePattern = Config.getProperty(Config.YEAR4D_DATE_PATTERN);
    private static SimpleDateFormat sdf = new SimpleDateFormat(datePattern);

    // ----------------------------------------------------------- Constructors


    /**
     * Create a {@link org.apache.commons.beanutils.Converter} that will throw a {@link org.apache.commons.beanutils.ConversionException}
     * if a conversion error occurs.
     */
    public SQLDateConverter() {
        logger.debug("registering " + this.getClass());
    }

    public SQLDateConverter(String pattern) {
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
                rtn = CopyUtils.isBaseVOTarget()?CopyUtils.NullAliasValues.get(CopyUtils.NullAlias.SQLDate):null;
            }else{
                rtn = parse(str);
            }
        }else{
            throw new ConversionException("SqlDateConverter: unsupported class type " + value.getClass());
        }

        /*if (value instanceof DateBean) {
            java.util.Date date = ((DateBean)value).getDate();
            return date==null?null:new java.sql.Date(date.getTime());

        }*/

        return rtn;
    }

    private Object parse(String date) throws ConversionException{
        java.util.Date convValue;
        try {
            convValue = sdf.parse(date);
        } catch (ParseException e) {
            throw new ConversionException(e.getMessage() +  ". SqlDateConverter: string does not represent valid date " + date);
        }
        if(!date.equals(sdf.format(convValue)))
            throw new ConversionException("SqlDateConverter: string does not represent valid date " + date);
        return new java.sql.Date(convValue.getTime());
    }

    public static String getDatePattern(){
        return datePattern;
    }

    public static void setDatePattern(String pattern){
        datePattern = pattern;
        sdf.applyPattern(datePattern);
    }
}
