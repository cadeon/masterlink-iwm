
package org.mlink.iwm.dbfeature;
import org.apache.log4j.Logger;
import org.mlink.iwm.util.DBAccess;

import java.sql.SQLException;

public class AddTenantRequestUniqueView extends NewDBFeautureSetup {
    private static final Logger logger = Logger.getLogger(AddTenantRequestUniqueView.class);

    public boolean isInstalled(){
        boolean isInstalled = true;
        try {
            DBAccess.execute("SELECT * FROM TENANT_REQUEST_UNIQUE");
            logger.debug("the feature has been installed");
        } catch (SQLException e) {
            isInstalled = false;
        }
        return isInstalled;
    }

    public void install() {
        logger.debug("the feature is being installed");
        try {
            DBAccess.executeUpdate("CREATE OR REPLACE FORCE VIEW TENANT_REQUEST_UNIQUE" +
            		"(ID, JOB_ID, LOCATOR_ID, TENANT_NAME, PROBLEM_ID, EMAIL, NOTE, PHONE, REQUEST_TYPE, LOCATION_COMMENT, URGENT, CREATED_DATE) as (" +
            		" select T.ID, T.JOB_ID, T.LOCATOR_ID, T.TENANT_NAME, T.PROBLEM_ID, T.EMAIL, T.NOTE, " +
            		" 	T.PHONE, T.REQUEST_TYPE, T.LOCATION_COMMENT, T.URGENT, T.CREATED_DATE " +
            		" from TENANT_REQUEST T, JOB J where J.tenant_request_id = T.ID)");
            
            DBAccess.executeUpdate("CREATE OR REPLACE FORCE VIEW AA_JOB_TENANTREQUEST " +
            		" (JOB_ID, CREATEDBY, DESCRIPTION, NOTE, ESTIMATEDTIME, WORKERS, PRIORITY, SKILLTYPE, SKILLLEVEL, " +
            		" JOBTYPE, ORG_ID, SCHEDULERESPONSIBILITY, STATUS, CREATED, EARLIESTSTART, LATESTSTART, STARTED, " +
            		" DISPATCHED, COMPLETED, SCHEDULED, OBJECT_ID, LOCATOR, SEQUENCE, ARCHIVED, ID, LOCATOR_ID," +
            		" TENANT_NAME, PROBLEM_ID, EMAIL, TR_NOTE, PHONE) AS (" +
            		" select  j.job_id, j.createdby, j.description, j.note, j.estimatedtime, j.workers, j.priority, j.skilltype, j.skilllevel, j.jobtype, j.org_id," +
            		"	j.scheduleresponsibility, j.status, j.created, j.earlieststart, j.lateststart, j.started, j.dispatched, j.completed, j.scheduled, j.object_id," +
            		"   j.locator, j.sequence, j.archived, null as id, null as locator_id, null as tenant_name, null as problem_id, null as email, null as tr_note, null as phone" +
            		" from aa_job j, tenant_request_unique t" +
            		" where  j.job_id = t.job_id(+))");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
