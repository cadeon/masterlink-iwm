package org.mlink.iwm.iwml;


/**
 * Created by IntelliJ IDEA.
 * User: andreipovodyrev
 * Date: Jun 2, 2007
 */
public abstract class Updator {
    StringBuilder log = new StringBuilder();
             
    void log(String statetement){
        log.append(statetement).append("\n");
    }

    public String getLog(){
        return log.toString();
    }

    public void process() throws Exception{
        try{
            store();
        }catch(Exception e){
            log("\t" + e.getMessage());
            e.fillInStackTrace();
            throw e;
        }
    }
    abstract protected void store() throws Exception;
}
