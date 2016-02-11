package org.mlink.iwm.exception;

import org.apache.log4j.Logger;

/**
 * Created by Andrei Povodyrev
 * Date: Jul 22, 2004
 */
public class BusinessException extends Exception{          //business exception MUST be checked exception!
    private static final Logger logger = Logger.getLogger(BusinessException.class);

    private String businessMessage;

    public String getBusinessMessage() {
        return businessMessage;
    }

    public void setBusinessMessage(String businessMessage) {
        logger.debug("BusinessException:" + businessMessage);
        this.businessMessage = businessMessage;
    }

    public BusinessException() {
        super(null,null);
    }

    public BusinessException(String s) {
        super(s,null);
        businessMessage = s;
        logger.debug("BusinessException:" + s);
    }
}
