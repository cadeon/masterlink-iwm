package org.mlink.iwm.dbfeature;

import org.apache.log4j.Logger;
import org.mlink.iwm.lookup.JobStatusRef;
import org.mlink.iwm.util.DBAccess;

import java.sql.SQLException;

/**
 * Created by IntelliJ IDEA.
 * User: andreipovodyrev
 * Date: Nov 17, 2007
 */
public class UpdateAGT_PRECEDES_SUMView extends NewDBFeautureSetup{
    private static final Logger logger = Logger.getLogger(UpdateAGT_PRECEDES_SUMView.class);

    public boolean isInstalled(){
        return false;  //run every time
    }

    public void install() {
        logger.debug("the feature is being installed.");
        try {
            DBAccess.executeUpdate("   CREATE OR REPLACE FORCE VIEW AGT_PRECEDES_SUM\n" +
                    "(ID,PROJECT_ID, INCOMPLETE, COMPLETE)\n" +
                    "AS\n" +
                    "   select id,\n" +
                    "            project_id,\n" +
                    "            (select count(*)\n" +
                    "            from job j2\n" +
                    "            where j2.project_id = j1.project_id\n" +
                    "            and j2.sequence_level < j1.sequence_level\n" +
                    "            and j2.status_id not  in (\n" +
                    "            select id from job_status_ref where code in "+JobStatusRef.finalStatusesSQLClauseMinusNIA+"\n" +
                    "            ))                                 as incomplete,\n" +
                    "            (select count(*)\n" +
                    "            from job j2\n" +
                    "            where j2.project_id = j1.project_id\n" +
                    "            and j2.sequence_level < j1.sequence_level\n" +
                    "            and j2.status_id  in (\n" +
                    "            select id from job_status_ref where code in "+JobStatusRef.finalStatusesSQLClauseMinusNIA+"\n" +
                    "            ))                                  as complete\n" +
                    "   from job j1\n" +
                    "order by project_id, sequence_level, id");
            logger.debug("Done");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}