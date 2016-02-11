package org.mlink.iwm.base;

import javax.ejb.EJBLocalObject;
import java.io.Serializable;


public class BaseVO implements Serializable{
    protected boolean isMerged = false; // a utility filed to indicate that  has been already merged with entity to avoid duplicate processing
    public Long getPkField(){
        return null;
    }
      

}
