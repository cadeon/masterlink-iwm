package org.mlink.iwm.dbfeature;

import org.apache.log4j.Logger;
import org.mlink.iwm.util.DBAccess;

import java.sql.SQLException;

/**
 * User: chris
 * To change this template use File | Settings | File Templates.
 */
public class AddReportingViews extends NewDBFeautureSetup{
    private static final Logger logger = Logger.getLogger(AddReportingViews.class);
    
    
    public boolean isInstalled(){
        boolean isInstalled = true;
        String sql = "SELECT * FROM LOCATION_TREE_GF";
        try {
            DBAccess.executeUpdate(sql);
        } catch (SQLException e) {
            isInstalled = false;
        }
       
        return isInstalled;
    }
    public void install(){
    	 //Five views to create or update.
    	logger.debug("Creating or updating reporting views");
    	
    	//JOB_SCHEDULED_COUNT
    	String sql = "CREATE OR REPLACE FORCE VIEW JOB_SCHEDULED_COUNT (ID, CREATED_DATE, RESCHEDULED) AS " +
    				 "SELECT J.ID , j.created_date , COUNT(js.id) AS rescheduled " +
    				 "FROM JOB J, JOB_SCHEDULE JS " +
    				 "WHERE J.ID =JS.JOB_ID " +
    				 "AND j.started_date IS NULL " +
    				 "AND j.completed_date IS NULL " +
    				 "GROUP BY J.ID , j.created_date " +
    				 "HAVING COUNT(*) >1";
    	
    	try {
			DBAccess.executeUpdate(sql);
		} catch (SQLException e) {
			logger.debug("DBF Update failed, offending sql:");
			logger.debug(sql);
			e.printStackTrace();
		}
    	
    	
		//JOB_COMPLETED_BY
    	sql = "CREATE OR REPLACE FORCE VIEW JOB_COMPLETED_BY (ID, PERSON_ID, COMPLETED_DATE) AS " +
    			"SELECT DISTINCT job.id, " +
    			"ws.person_id, " +
    			"job.completed_date " +
    			"FROM job, job_schedule js, work_schedule ws " +
    			"WHERE job.id=js.job_id " +    		
    			"AND js.work_schedule_id = ws.id ";
    	
    	try {
			DBAccess.executeUpdate(sql);
		} catch (SQLException e) {	
			logger.debug("DBF Update failed, offending sql:");
			logger.debug(sql);
			e.printStackTrace();
		}
		
		
		//AA_JOBVIEW_REP
    	sql = "CREATE OR REPLACE FORCE VIEW AA_JOBVIEW_REP (OBJECT_ID, PRIORITY, TYPE, DESCRIPTION, JOB_NOTE, FIELD_CONDITION, LASTUPDATE, CREATED, COMPLETE, DISPATCHED, EARLIESTSTART, LATESTSTART, FINISHBY, STATUS, ESTIMATEDTIME, SKILLTYPE, SKILLLEVEL, WORKERACTIVE, WORKSCHEDULE, SECURITYLEVEL, LOCATOR, PARTY, PRECEEDS, INCOMPLETE, READY, STICKY, SCHEDULERESPONSIBILITY, WORKERS, MATERIALREQUIRED, SCHEDULESTATUS, SCHEDULEDATE, SCHEDULED, STARTED, TIME, JOBID, JB_OBJ_ID) AS " +
    			"SELECT DISTINCT j.id          AS object_id, " +
    			"pr.code                     AS priority, " +
    			"ttr.code                    AS type, " +
    			"j.description               AS description, " +
    			"j.note                      AS job_note, " +
    			"ja.field_condition          AS field_condition, " +
    			"j.last_updated              AS lastupdate, " +
    			"j.created_date              AS created, " +
    			"j.completed_date            AS completed, " +
    			"j.dispatched_date           AS dispatched, " +
    			"j.earliest_start            AS earlieststart, " +
    			"j.latest_start              AS lateststart, " +
    			"j.finishby                  AS finishby, " +
    			"jsr.code                    AS status, " +
    			"j.estimatedtime             AS estimatedtime, " +
    			"rtrim(str.code)             AS skilltype, " +
    			"slr.code                    AS skilllevel, " +
    			"js.workeractive             AS workeractive, " +
    			"js.workschedule             AS workschedule, " +
    			"sec.code                    AS securitylevel, " +
    			"j.locator_id                AS locator, " +
    			"og.party_id                 AS party, " +
    			"psv.complete+psv.incomplete AS preceeds, " +
    			"psv.incomplete              AS incomplete, " +
    			"'no'                        AS ready, " +
    			"NULL                        AS sticky, " +
    			"srr.code                    AS scheduleresponsibility, " +
    			"j.number_workers            AS workers, " +
    			"'n'                         AS materialrequired, " +
    			"js.schedulestatus           AS schedulestatus, " +
    			"js.scheduledate             AS scheduledate, " +
    			"j.scheduled_date            AS scheduled, " +
    			"j.started_date              AS started, " +
    			"NVL(jtv.totaltime,0)        AS TIME, " +
    			"j.id                        AS jobid , " +
    			"j.object_id                 AS jb_obj_id " +
    			"" +
    			"FROM job j, locator l, priority_ref pr, task_type_ref ttr, security_type_ref sec, " +
    			"agt_precedes_sum psv, jobtimeview jtv, organization og, job_status_ref jsr, " +
    			"schedule_responsibility_ref srr, skill_level_ref slr, skill_type_ref str, jobview_schedule js, JOB_ACTION JA " +
    			"" +
    			"WHERE j.locator_id               = l.id " +
    			"AND js.job(+)                    = j.id " +
    			"AND psv.id(+)                    = j.id " +
    			"AND ja.id(+)                     =j.id " +
    			"AND jtv.id(+)                    = j.id " +
    			"AND sec.id(+)                    = l.security_level " +
    			"AND og.id(+)                     = j.organization_id " +
    			"AND j.priority_id                = pr.id " +
    			"AND j.skill_level_id             = slr.id " +
    			"AND j.skill_type_id              = str.id " +
    			"AND j.job_type_id                = ttr.id " +
    			"AND j.schedule_responsibility_id = srr.id " +
    			"AND j.status_id                  = jsr.id";
    	
    	try {
			DBAccess.executeUpdate(sql);
		} catch (SQLException e) {
			logger.debug("DBF Update failed, offending sql:");
			logger.debug(sql);
			e.printStackTrace();
		}
		
		//PERSON_NAME
    	sql = "CREATE OR REPLACE FORCE VIEW PERSON_NAME (ID, NAME, ORGANIZATION_ID, ORG) AS " +
    			"SELECT person.id, party.name, person.organization_id, " +
    			"(SELECT party.name FROM party WHERE party.id = person.organization_id) AS org " +
    			"FROM person, party " +
    			"WHERE party.id = person.party_id";
    	
    	try {
			DBAccess.executeUpdate(sql);
		} catch (SQLException e) {	
			logger.debug("DBF Update failed, offending sql:");
			logger.debug(sql);
			e.printStackTrace();
		}
		
		//LOCATION_TREE_GF
    	sql = "CREATE OR REPLACE FORCE VIEW LOCATION_TREE_GF (SCHEMA_ID, LOCATOR_ID, TOP_PARENT_ID, CAMPUS, BDG, BDG_ID, ZONE, FLR, ROOM) AS " +
    			"SELECT DISTINCT LOCATOR.SCHEMA_ID AS SCHEMA_ID, " +
    			"LOCATOR.ID                      AS LOCATOR_ID, " +
    			"LOCATOR.TOP_PARENT_ID           AS TOP_PARENT_ID, " +
    			"DECODE (LOCATOR.SCHEMA_ID,'5', " +
    			"(SELECT l_d.abbr " +
    			"FROM LOCATOR L, " +
    			"LOCATOR L_D " +
    			"WHERE L.ID        = LOCATOR.ID " +
    			"AND l.top_parent_id = l_d.id " +
    			") ,'4', " +
    			"(SELECT l_c.abbr " +
    			"FROM LOCATOR L, " +
    			"LOCATOR L_A, " +
    			"LOCATOR L_B, " +
    			"LOCATOR L_C " +
    			"WHERE L.ID  = LOCATOR.ID " +
    			"AND l_a.id (+)= l.parent_id " +
    			"AND l_b.id (+)= l_a.parent_id " +
    			"AND l_c.id (+)= l_b.parent_id " +
    			"),'3', " +
    			"(SELECT l_b.abbr " +
    			"FROM LOCATOR L, " +
    			"LOCATOR L_A, " +
    			"LOCATOR L_B " +
    			"WHERE L.ID  = LOCATOR.ID " +
    			"AND l_a.id (+)= l.parent_id " +
    			"AND l_b.id (+)= l_a.parent_id " +
    			"),'2', " +
    			"(SELECT l_a.abbr " +
    			"FROM LOCATOR L, " +
    			"LOCATOR L_A " +
    			"WHERE L.ID  = LOCATOR.ID " +
    			"AND l_a.id (+)= l.parent_id " +
    			"), " +
    			"(SELECT l_d.abbr " +
    			"FROM LOCATOR L, " +
    			"LOCATOR L_D " +
    			"WHERE L.ID        = LOCATOR.ID " +
    			"AND l.top_parent_id = l_d.id " +
    			")) AS campus, " +
    			"DECODE (LOCATOR.SCHEMA_ID,'5', " +
    			"(SELECT l_c.abbr " +
    			"FROM LOCATOR L, " +
    			"LOCATOR L_A, " +
    			"LOCATOR L_B, " +
    			"LOCATOR L_C " +
    			"WHERE L.ID  = LOCATOR.ID " +
    			"AND l_a.id (+)= l.parent_id " +
    			"AND l_b.id (+)= l_a.parent_id " +
    			"AND l_c.id (+)= l_b.parent_id " +
    			"),'4', " +
    			"(SELECT l_b.abbr " +
    			"FROM LOCATOR L, " +
    			"LOCATOR L_A, " +
    			"LOCATOR L_B " +
    			"WHERE L.ID  = LOCATOR.ID " +
    			"AND l_a.id (+)= l.parent_id " +
    			"AND l_b.id (+)= l_a.parent_id " +
    			"),'3', " +
    			"(SELECT l_a.abbr " +
    			"FROM LOCATOR L, " +
    			"LOCATOR L_A " +
    			"WHERE L.ID  = LOCATOR.ID " +
    			"AND l_a.id (+)= l.parent_id " +
    			"),'2', " +
    			"(SELECT LOCATOR.NAME " +
    			"FROM LOCATOR L " +
    			"WHERE L.ID = LOCATOR.ID " +
    			") ,'') AS bdg, " +
    			"DECODE (LOCATOR.SCHEMA_ID,'5', " +
    			"(SELECT l_c.id " +
    			"FROM LOCATOR L, " +
    			"LOCATOR L_A, " +
    			"LOCATOR L_B, " +
    			"LOCATOR L_C " +
    			"WHERE L.ID  = LOCATOR.ID " +
    			"AND l_a.id (+)= l.parent_id " +
    			"AND l_b.id (+)= l_a.parent_id " +
    			"AND l_c.id (+)= l_b.parent_id " +
    			"),'4', " +
    			"(SELECT l_b.id " +
    			"FROM LOCATOR L, " +
    			"LOCATOR L_A, " +
    			"LOCATOR L_B " +
    			"WHERE L.ID  = LOCATOR.ID " +
    			"AND l_a.id (+)= l.parent_id " +
    			"AND l_b.id (+)= l_a.parent_id " +
    			"),'3', " +
    			"(SELECT l_a.id " +
    			"FROM LOCATOR L, " +
    			"LOCATOR L_A " +
    			"WHERE L.ID  = LOCATOR.ID " +
    			"AND l_a.id (+)= l.parent_id " +
    			"),'2', " +
    			"(SELECT LOCATOR.id FROM LOCATOR L WHERE L.ID = LOCATOR.ID " +
    			") ,'') AS bdg_id, " +
    			"DECODE (LOCATOR.SCHEMA_ID,'5', " +
    			"(SELECT l_b.abbr " +
    			"FROM LOCATOR L, " +
    			"LOCATOR L_A, " +
    			"LOCATOR L_B " +
    			"WHERE L.ID  = LOCATOR.ID " +
    			"AND l_a.id (+)= l.parent_id " +
    			"AND l_b.id (+)= l_a.parent_id " +
    			"),'4', " +
    			"(SELECT l_a.abbr " +
    			"FROM LOCATOR L, " +
    			"LOCATOR L_A " +
    			"WHERE L.ID  = LOCATOR.ID " +
    			"AND l_a.id (+)= l.parent_id " +
    			"),'3', " +
    			"(SELECT LOCATOR.NAME " +
    			"FROM LOCATOR L " +
    			"WHERE L.ID = LOCATOR.ID " +
    			") ,'') AS zone, " +
    			"DECODE (LOCATOR.SCHEMA_ID,'5', " +
    			"(SELECT l_a.abbr " +
    			"FROM LOCATOR L, " +
    			"LOCATOR L_A " +
    			"WHERE L.ID  = LOCATOR.ID " +
    			"AND l_a.id (+)= l.parent_id " +
    			"),'4', " +
    			"(SELECT LOCATOR.NAME " +
    			"FROM LOCATOR L " +
    			"WHERE L.ID = LOCATOR.ID " +
    			") ,'') AS flr, " +
    			"DECODE (LOCATOR.SCHEMA_ID,'5', " +
    			"(SELECT LOCATOR.NAME " +
    			"FROM LOCATOR L " +
    			"WHERE L.ID = LOCATOR.ID " +
    			") ,'') AS room " +
    			"FROM LOCATOR LOCATOR";
    	
    	try {
			DBAccess.executeUpdate(sql);
		} catch (SQLException e) {	
			logger.debug("DBF Update failed, offending sql:");
			logger.debug(sql);
			e.printStackTrace();
		}
		
        }
}
