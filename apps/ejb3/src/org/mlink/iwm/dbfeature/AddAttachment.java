package org.mlink.iwm.dbfeature;

import org.apache.log4j.Logger;
import org.mlink.iwm.util.DBAccess;

import java.sql.SQLException;

/**
 * Created by IntelliJ IDEA.
 * User: andreipovodyrev
 * Date: Jul 19, 2007
 * Time: 10:53:39 AM
 * To change this template use File | Settings | File Templates.
 */
public class AddAttachment extends NewDBFeautureSetup{
    private static final Logger logger = Logger.getLogger(AddAttachment.class);

    public boolean isInstalled(){
        boolean isInstalled = true;
        try {
            DBAccess.execute("SELECT count(*) from attachment");
            logger.debug("the feature has been installed");
        } catch (SQLException e) {
            isInstalled = false;
        }
        return isInstalled;
    }

    public void install() {
        logger.debug("the feature is being installed");
        try {

            DBAccess.executeUpdate("CREATE TABLE ATTACHMENT (\n" +
                    "  ID  NUMBER (38)   NOT NULL,\n" +
                    "  FILE_NME  VARCHAR2 (30),\n" +
                    "  DESCRIPTION  VARCHAR2 (200),\n" +
                    "  MIME_TYP  VARCHAR2 (30),\n" +
                    "  BIN_DATA  BLOB,\n" +
                    "  CREATED_DATE   DATE,\n" +
                    "  BYTES   NUMBER,\n" +
                    "  CONSTRAINT ATTACHMENT_PK\n" +
                    "  PRIMARY KEY ( ID ) ) ");

            DBAccess.executeUpdate("create or replace\n" +
                    "TRIGGER INS_ATTACHMENT\n" +
                    "  BEFORE INSERT\n" +
                    "     on ATTACHMENT\n" +
                    " REFERENCING NEW AS NEW OLD AS OLD\n" +
                    "  FOR EACH ROW\n" +
                    "BEGIN\n" +
                    //"  SELECT  (NVL(MAX(ID),0)+1) INTO :NEW.ID FROM ATTACHMENT;\n" +
                    "  SELECT  SYSDATE INTO :NEW.CREATED_DATE FROM DUAL;\n" +
                    "END;");

            DBAccess.executeUpdate("CREATE SEQUENCE ATTACHMENT_SEQ  START WITH  1000 INCREMENT BY  1");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

