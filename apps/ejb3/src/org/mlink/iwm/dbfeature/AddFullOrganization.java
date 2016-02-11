
package org.mlink.iwm.dbfeature;
import org.apache.log4j.Logger;
import org.mlink.iwm.util.DBAccess;

import java.sql.SQLException;

public class AddFullOrganization extends NewDBFeautureSetup {
    private static final Logger logger = Logger.getLogger(AddFullOrganization.class);

    public boolean isInstalled(){
        boolean isInstalled = true;
        try {
            DBAccess.execute("SELECT full_organization FROM organization");
            logger.debug("the feature has been installed");
        } catch (SQLException e) {
            isInstalled = false;
        }
        return isInstalled;
    }

    public void install() {
        logger.debug("the feature is being installed");
        try {
            DBAccess.executeUpdate("alter table organization add full_organization varchar(300)");

            DBAccess.executeUpdate("update organization set full_organization = (Select party.name from party where party.id = organization.party_id)");

            DBAccess.executeUpdate("CREATE OR REPLACE FORCE VIEW AA_WORKERVIEW_SHORTVERSION " +
            		"(PERSON_ID, W_NAME, O_ID, O_NAME, ACTIVE)" +
            		" AS" +
            		" (SELECT p.id          AS person_id," +
            		" wp.name             AS w_name, " +
            		" o.id                AS o_id," +
            		" o.full_organization AS o_name," +
            		" p.active " +
            		"FROM party op," +
            		" party wp," +
            		" organization o," +
            		" person p " +
            		"WHERE wp.id           = p.party_id " +
            		"AND p.organization_id = o.id " +
            		"AND op.id             = o.party_id)");
  
            DBAccess.executeUpdate("CREATE OR REPLACE FORCE VIEW AA_WORKERVIEW (PERSON_ID, W_NAME, O_NAME) " +
            		"AS" +
            		" SELECT p.id          AS person_id," +
            		" wp.name             AS w_name," +
            		" o.full_organization AS o_name " +
            		"FROM party op," +
            		" party wp," +
            		" organization o," +
            		" person p " +
            		"WHERE wp.id           = p.party_id " +
            		"AND p.organization_id = o.id " +
            		"AND op.id             = o.party_id");
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
