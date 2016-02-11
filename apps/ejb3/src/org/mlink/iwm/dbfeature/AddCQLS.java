package org.mlink.iwm.dbfeature;

import org.apache.log4j.Logger;
import org.mlink.iwm.util.DBAccess;

import java.sql.SQLException;

/**
 * Created by IntelliJ IDEA.
 * User: andreipovodyrev
 * Date: Nov 15, 2007
 */
public class AddCQLS extends NewDBFeautureSetup{
    private static final Logger logger = Logger.getLogger(AddCQLS.class);

    public boolean isInstalled(){
        boolean isInstalled = true;
        try {
            DBAccess.execute("SELECT * FROM CQLS_REF");
            logger.debug("the feature has been installed");
        } catch (SQLException e) {
            isInstalled = false;
        }
        return isInstalled;
    }

    public void install() {
        logger.debug("the feature is being installed");
        try {
            DBAccess.executeUpdate("CREATE TABLE CQLS_REF\n" +
                    "( ID NUMBER NOT NULL ENABLE,\n" +
                    "DESCRIPTION VARCHAR2(50) DEFAULT '-' NOT NULL ENABLE,\n" +
                    "DISP_ORD NUMBER (2,0),\n" +
                    "CODE VARCHAR2(50) NOT NULL ENABLE,\n" +
                    "TYPE VARCHAR2(20) NOT NULL ENABLE,\n" +
                    "CONSTRAINT PK_CQLSREF_ID PRIMARY KEY (ID) ENABLE )");



            DBAccess.executeUpdate("INSERT INTO CQLS_REF( ID, DESCRIPTION, CODE, DISP_ORD,TYPE )VALUES (1,'CERT1','QUAL1',10,'CERTIFICATION')");
            DBAccess.executeUpdate("INSERT INTO CQLS_REF( ID, DESCRIPTION, CODE, DISP_ORD,TYPE )VALUES (50,'QUAL1','CERT1',10,'QUALIFICATION')");
            DBAccess.executeUpdate("INSERT INTO CQLS_REF( ID, DESCRIPTION, CODE, DISP_ORD,TYPE )VALUES (100,'LICENSE1','LICENSE1',10,'LICENSE')");


            DBAccess.executeUpdate("CREATE TABLE CQLS\n" +
                    "( ID NUMBER NOT NULL ENABLE,\n" +
                    "PERSON_ID NUMBER,\n" +
                    "CQLS_REF_ID NUMBER,\n" +
                    "EXP_DATE DATE,\n" +
                    "CONSTRAINT PK_CQLS_ID PRIMARY KEY (ID) ENABLE )");

            DBAccess.executeUpdate("ALTER TABLE CQLS add CONSTRAINT FK_CQLS_CQLSREFID FOREIGN KEY (CQLS_REF_ID ) references CQLS_REF (ID) ENABLE");
            DBAccess.executeUpdate("ALTER TABLE CQLS add CONSTRAINT FK_CQLS_PERSON_ID FOREIGN KEY (PERSON_ID ) references PERSON (ID) ENABLE ");

            DBAccess.executeUpdate("create sequence CQLS_SEQ start with 1000");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
