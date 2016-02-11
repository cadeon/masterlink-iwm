package org.mlink.iwm.lookup;

import java.util.Collection;
import java.util.Map;

import org.apache.log4j.Logger;

public class JobStatusRef extends CodeLookupValues{
    private static final Logger logger = Logger.getLogger(JobStatusRef.class);
    
    public enum Status {CIA, DJO, DPD, DUN, EJO, INS, NYA, PJO, RFS, SPD, VSJ, WFP, WFS, WSR, SJO, JIP;
    
    	public boolean equals(String str){
	        return this.toString().equals(str);
	    }
	}
    
    public static String finalStatusesSQLClause = "('"+Status.DUN+"','"+Status.EJO+"','"+ Status.CIA+"','"+ Status.NYA+"') ";
    
	public static String finalStatusesSQLClauseMinusNIA = "('"+Status.DUN+"','"+Status.EJO+"','"+ Status.CIA+"') ";
	
	public static String pendingStatusesSQLClause = "('"+Status.WFP+"','"+Status.PJO+"') ";
	
	public static String jobCompletedSQLClause = "('"+Status.DUN+"') ";
	
	public static String activeStatusesSQLClause = "('"+Status.INS+"','"+Status.RFS+"','"+Status.DPD+"','"+Status.DJO+"','"+Status.SJO+"','"+Status.NYA+"') ";
	
	public static String queryIncompletes = "('"+Status.DJO+"','"+Status.DPD+"','"+Status.RFS+"') ";
	
    public String getSql() {
        return "SELECT * from JOB_STATUS_REF ORDER BY DISP_ORD";
    }
    public void init() {
        //Long dummy = null;
        //addOption(new OptionItem(dummy,""));
    }

    public void addOption(Map map){
        Object value = map.get("ID");
        String code = (String)map.get("CODE");
        Object label = map.get("DESCRIPTION");
        if(value == null || label == null || code==null)
            logger.error(getSql() + " returned invalid data " + map);
        else{
            OptionItem item = new OptionItem(value.toString(),label.toString(), code.toString());
            if(Status.INS.equals(code) || Status.NYA.equals(code)|| Status.DUN.equals(code) || Status.CIA.equals(code)){
                item.setDisabled(false);
            }else{
                item.setDisabled(true);                
            }
            addOption(item);
        }
    }

    public static String getLabel(Integer id){
        JobStatusRef me = (JobStatusRef) LookupMgr.getInstance(JobStatusRef.class);
        return  me.getLabel(id==null?null:String.valueOf(id));
    }

    public static int getIdByCode(String code){
        JobStatusRef me = (JobStatusRef) LookupMgr.getInstance(JobStatusRef.class);
        return  me.getValueByCode(code).intValue();
    }
    
    public static int getIdByCode(JobStatusRef.Status code){
        return  getIdByCode(code.toString());
    }
    
    public static String getCode(Integer id){
        JobStatusRef me = (JobStatusRef) LookupMgr.getInstance(JobStatusRef.class);
        return  me.getCode(id==null?null:String.valueOf(id));
    }

    public Collection getDefault() {
        return null;
    }

}
