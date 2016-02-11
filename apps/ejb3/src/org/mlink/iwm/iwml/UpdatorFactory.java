package org.mlink.iwm.iwml;

/**
 * Created by IntelliJ IDEA.
 * User: andreipovodyrev
 * Date: Jun 2, 2007
 */
public class UpdatorFactory {
    public static Updator getUpdator(Object obj){
        if(obj instanceof Worker){
            return new WorkerUpdator((Worker)obj);
        }else if(obj instanceof SitarJob){
            return new SitarJobUpdator((SitarJob)obj);
        }else
            throw new RuntimeException("No updator for object" + obj.getClass().getName());
    }

}
