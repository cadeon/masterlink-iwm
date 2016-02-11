package org.mlink.iwm.dwr;

import org.mlink.iwm.util.UtilDateConverter;

/**
 * User: andreipovodyrev
 * Date: Nov 19, 2007
 */
public class ValidationUtil {

    public static boolean isValidDate(String date){
        return UtilDateConverter.isValidDate(date);
    }
}
