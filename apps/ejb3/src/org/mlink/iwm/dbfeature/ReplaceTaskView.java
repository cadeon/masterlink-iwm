
package org.mlink.iwm.dbfeature;
import org.apache.log4j.Logger;
import org.mlink.iwm.util.DBAccess;

import java.sql.SQLException;

public class ReplaceTaskView extends NewDBFeautureSetup {
    private static final Logger logger = Logger.getLogger(ReplaceTaskView.class);

    public boolean isInstalled(){
    	boolean isInstalled = true;
        try {
            DBAccess.execute("SELECT EXPIRYTYPEID FROM TASKVIEW");
            logger.debug("the feature has been installed");
        } catch (SQLException e) {
            isInstalled = false;
        }
        return isInstalled;
    }

    public void install() {
        logger.debug("the feature is being installed");
        try {
            DBAccess.executeUpdate("CREATE OR REPLACE FORCE VIEW TASKVIEW (OBJECT_ID, LOCATOR_ID, PRIORITY, THRESHOLD, TASKTYPE, DESCRIPTION, " +
            		"LASTSERVICED, LASTPLANNED, TARGET, ESTIMATEDTIME, RUNHOURS, SKILLTYPE, SKILLLEVEL, PLAN, SCHEDULEGROUP, GROUPDESCRIPTION, " +
            		"ORGANIZATION_ID, METERRULE, ACTIVE, NUMBERWORKERS, SEQUENCEGROUP, SEQUENCELEVEL, MONTHS, DAYS, EXPIRYTYPEID, EXPIRYTYPE, EXPIRYNUMOFDAYS) AS " +
            		" select ts.id as object_id, tr.locator_id as locator_id, pr.id as priority, decode(nvl(ts.run_hours_threshold_increment,0)," +
            		"	0,0,ts.run_hours_threshold) as threshold, ts.task_type_id as tasktype, ts.description, ts.last_serviced_date as lastserviced," +
            		"  ts.last_planned_date as lastplanned, ts.object_id as target, ts.estimated_time as estimatedtime, decode(nvl(ts.run_hours_threshold_increment,0)," +
            		"   0,0, tr.run_hours) as runhours, ts.skill_type_id as skilltype, ts.skill_level_id  as skilllevel, remove_commas(ts.plan) as plan, " +
            		"  ts.group_id as schedulegroup, sg.description as groupdescription, og.id as organization_id, ts.meter_rule as meterrule," +
            		"  ts.active, ts.number_workers as numberworkers, seq.sequence_id as sequencegroup, seq.sequence_level as sequencelevel," +
            		"  decode(tf.code,null,ts.freq_months,tf.months) as months, decode(tf.code,null,ts.freq_days,tf.days) as days, ts.expiry_type_id expiryTypeId," +
            		"  expirytr.code expiryType, ts.expiry_numOfDays expiryNumOfDays" +
            		" from task ts, object tr, organization og, task_group sg, priority_ref pr, task_frequency_ref tf, task_sequence seq, schedule_responsibility_ref srr, " +
            		"	task_def_expiry_type_ref expirytr" +
            		" where nvl(ts.last_planned_date,now-1) < now and nvl(ts.active,1) = 1 and nvl(tr.active,1) = 1 and srr.code = 'System' and nvl(tr.start_date,now) <= now" +
            		"  and ts.start_date <= now and ts.object_id = tr.id and ts.group_id = sg.id(+) and ts.priority_id = pr.id and ts.frequency_id = tf.id(+)" +
            		"  and ts.id = seq.task_id(+) and ts.organization_id = og.id(+) and ts.schedule_responsibility_id = srr.id and ts.expiry_type_id = expirytr.id" +
            		" order by schedulegroup");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
