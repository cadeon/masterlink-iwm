package org.mlink.iwm.util;

import org.apache.commons.beanutils.Converter;
import org.apache.commons.beanutils.ConversionException;
import org.apache.log4j.Logger;
import org.mlink.iwm.util.CopyUtils;

import java.text.DecimalFormat;
import java.text.ParseException;

/**
 * updated version of apache's DoublegConverted
 * this one tolerates nulls
 */
public class DoubleConverter implements Converter {
    private static final Logger logger = Logger.getLogger(DoubleConverter.class);
    private static final DecimalFormat mDecFmt1 = new DecimalFormat("#,###,###,###.##");
    private static final DecimalFormat mDecFmt2 = new DecimalFormat("#,###,###,###.##;(#,###,###,###.##)");

    // ----------------------------------------------------------- Constructors


    /**
     * Create a {@link Converter} that will throw a {@link org.apache.commons.beanutils.ConversionException}
     * if a conversion error occurs.
     */
    public DoubleConverter() {
        logger.debug("registering " + this.getClass());
    }

    // --------------------------------------------------------- Public Methods


    /**
     * Convert the specified input object into an output object of the
     * specified type.
     *
     * @param type  Data type to which this value should be converted
     * @param value The input value to be converted
     * @throws org.apache.commons.beanutils.ConversionException
     *          if conversion cannot be performed
     *          successfully
     */

    public Object convert(Class type, Object value) {
        Object rtn;
        if (value == null) {
            rtn = null;
        } else if (value instanceof Double) {
            return (value);
        } else if (value instanceof Number) {
            return ((Number) value).doubleValue();
        } else if (value instanceof String) {
            String str = ((String)value).trim();
            if(str.length()==0){
                rtn = CopyUtils.isBaseVOTarget()?CopyUtils.NullAliasValues.get(CopyUtils.NullAlias.Double):null;
            }else{            // try to parse w/ decimal formatter
                // negative money could  be -1.0 or (1.0)
                DecimalFormat decFmt;
                if (((String) value).indexOf("(") >= 0)
                    decFmt = mDecFmt2;
                else
                    decFmt = mDecFmt1;

                try {
                    rtn = ((decFmt.parse((String) value)).doubleValue());
                } catch (ParseException e) {
                    throw new ConversionException("DoubleConverter: " + e.getMessage());
                }
            }
        } else {
            rtn = (new Double(value.toString()));
        }
        return rtn;
    }


}
