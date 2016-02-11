package org.mlink.iwm.dao;

import junit.framework.TestCase;
import junit.framework.Test;
import junit.framework.TestSuite;

import org.mlink.iwm.lookup.JobStatusRef;
import org.mlink.iwm.util.DBAccess;
import org.mlink.iwm.util.EnvUtils;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.SQLWarning;
import java.util.Map;
import java.util.HashMap;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: andreipovodyrev
 * Date: Jul 31, 2007
 * Time: 10:03:55 AM
 * To change this template use File | Settings | File Templates.
 */
public class SQLWarningTest extends TestCase {
    public SQLWarningTest(String name) {
        super(name);
    }

    public void setUp() throws Exception {
        super.setUp();
    }

    public void tearDown() throws Exception {
        super.tearDown();
    }

    public void test1() throws Exception {
        System.getProperties().put(DBAccess.DS_NAME,"test_ds");
        System.getProperties().put(EnvUtils.JNDI_PROVIDER_HOST_NAME,"localhost");
        Connection con = DBAccess.getDBConnection();
        Statement st = con.createStatement( ) ;
        //st.executeQuery("select jobschedul0_.ID as ID6_0_, workschedu1_.ID as ID27_1_, person2_.ID as ID12_2_, locator3_.ID as ID11_3_, job4_.ID as ID3_4_, priorityre5_.ID as ID13_5_, jobstatusr6_.ID as ID7_6_, skilltyper7_.ID as ID20_7_, skilllevel8_.ID as ID19_8_, locator9_.ID as ID11_9_, jobschedul0_.CREATED_TIME as CREATED2_6_0_, jobschedul0_.DELETED_TIME as DELETED3_6_0_, jobschedul0_.JOB_ID as JOB4_6_0_, jobschedul0_.WORK_SCHEDULE_ID as WORK5_6_0_, workschedu1_.DAY as DAY27_1_, workschedu1_.NOTSCHEDULABLE as NOTSCHED3_27_1_, workschedu1_.TIME as TIME27_1_, workschedu1_.UTILITY_RATING as UTILITY5_27_1_, workschedu1_.STATUS_ID as STATUS6_27_1_, workschedu1_.LOCATOR_ID as LOCATOR7_27_1_, workschedu1_.PERSON_ID as PERSON8_27_1_, workschedu1_.SHIFT_ID as SHIFT9_27_1_, person2_.ORGANIZATION_ID as ORGANIZA2_12_2_, person2_.PASSWORD as PASSWORD12_2_, person2_.ACTIVE as ACTIVE12_2_, person2_.PARTY_ID as PARTY5_12_2_, person2_.USERNAME as USERNAME12_2_, person2_.SECURITY_LEVEL_ID as SECURITY7_12_2_, person2_.WORKER_TYPE_ID as WORKER8_12_2_, locator3_.SECURITY_LEVEL as SECURITY2_11_3_, locator3_.FULL_LOCATOR as FULL3_11_3_, locator3_.ABBR as ABBR11_3_, locator3_.NAME as NAME11_3_, locator3_.PARENT_ID as PARENT6_11_3_, locator3_.SCHEMA_ID as SCHEMA7_11_3_, locator3_.TOP_PARENT_ID as TOP8_11_3_, job4_.CREATEDBY as CREATEDBY3_4_, job4_.CREATED_DATE as CREATED4_3_4_, job4_.DISPATCHED_DATE as DISPATCHED5_3_4_, job4_.STARTED_DATE as STARTED6_3_4_, job4_.FINISHBY as FINISHBY3_4_, job4_.ESTIMATEDTIME as ESTIMATE8_3_4_, job4_.LAST_UPDATED as LAST9_3_4_, job4_.NUMBER_WORKERS as NUMBER10_3_4_, job4_.COMPLETED_DATE as COMPLETED11_3_4_, job4_.EARLIEST_START as EARLIEST12_3_4_, job4_.LATEST_START as LATEST13_3_4_, job4_.DESCRIPTION as DESCRIP14_3_4_, job4_.SCHEDULED_DATE as SCHEDULED15_3_4_, job4_.SEQUENCE_LEVEL as SEQUENCE16_3_4_, job4_.STICKY as STICKY3_4_, job4_.JOB_TYPE_ID as JOB18_3_4_, job4_.OBJECT_ID as OBJECT19_3_4_, job4_.ORGANIZATION_ID as ORGANIZ20_3_4_, job4_.LOCATOR_ID as LOCATOR21_3_4_, job4_.SKILL_LEVEL_ID as SKILL22_3_4_, job4_.SKILL_TYPE_ID as SKILL23_3_4_, job4_.PRIORITY_ID as PRIORITY24_3_4_, job4_.STATUS_ID as STATUS2_3_4_, job4_.SCHEDULE_RESPONSIBILITY_ID as SCHEDULE25_3_4_, job4_.PROJECT_ID as PROJECT26_3_4_, priorityre5_.CODE as CODE13_5_, jobstatusr6_.CODE as CODE7_6_, jobstatusr6_.DESCRIPTION as DESCRIPT3_7_6_, skilltyper7_.DESCRIPTION as DESCRIPT2_20_7_, skilltyper7_.CODE as CODE20_7_, skilllevel8_.DESCRIPTION as DESCRIPT2_19_8_, skilllevel8_.CODE as CODE19_8_, skilllevel8_.VALUE as VALUE19_8_, locator9_.SECURITY_LEVEL as SECURITY2_11_9_, locator9_.FULL_LOCATOR as FULL3_11_9_, locator9_.ABBR as ABBR11_9_, locator9_.NAME as NAME11_9_, locator9_.PARENT_ID as PARENT6_11_9_, locator9_.SCHEMA_ID as SCHEMA7_11_9_, locator9_.TOP_PARENT_ID as TOP8_11_9_ from JOB_SCHEDULE jobschedul0_, WORK_SCHEDULE workschedu1_, PERSON person2_, LOCATOR locator3_, JOB job4_, PRIORITY_REF priorityre5_, JOB_STATUS_REF jobstatusr6_, SKILL_TYPE_REF skilltyper7_, SKILL_LEVEL_REF skilllevel8_, LOCATOR locator9_, WORK_SCHEDULE_STATUS_REF workschedu12_, JOB_STATUS_REF jobstatusr14_ where job4_.STATUS_ID=jobstatusr14_.ID and workschedu1_.STATUS_ID=workschedu12_.id and job4_.LOCATOR_ID=locator9_.ID(+) and job4_.SKILL_LEVEL_ID=skilllevel8_.ID and job4_.SKILL_TYPE_ID=skilltyper7_.ID and job4_.STATUS_ID=jobstatusr6_.ID and job4_.PRIORITY_ID=priorityre5_.ID and jobschedul0_.JOB_ID=job4_.ID and workschedu1_.LOCATOR_ID=locator3_.ID and workschedu1_.PERSON_ID=person2_.ID and jobschedul0_.WORK_SCHEDULE_ID=workschedu1_.ID and workschedu1_.LOCATOR_ID=20000718228 and workschedu12_.code='"+JobStatusRef.Status.DUN+"' and (jobstatusr14_.CODE in ('"+JobStatusRef.Status.DJO+"','"+JobStatusRef.Status.DPD+"','"+JobStatusRef.Status.RFS+"')) and (jobschedul0_.DELETED_TIME is null) and jobschedul0_.CREATED_TIME=(select max(jobschedul15_.CREATED_TIME) from JOB_SCHEDULE jobschedul15_ where jobschedul15_.JOB_ID=jobschedul0_.JOB_ID and (jobschedul15_.DELETED_TIME is null))");
        /*st.executeQuery("select jobschedul0_.id " +
                "from JOB_SCHEDULE jobschedul0_, WORK_SCHEDULE workschedu1_, PERSON person2_, LOCATOR locator3_, JOB job4_, PRIORITY_REF priorityre5_, JOB_STATUS_REF jobstatusr6_, SKILL_TYPE_REF skilltyper7_, SKILL_LEVEL_REF skilllevel8_, LOCATOR locator9_, WORK_SCHEDULE_STATUS_REF workschedu12_, JOB_STATUS_REF jobstatusr14_ where job4_.STATUS_ID=jobstatusr14_.ID and workschedu1_.STATUS_ID=workschedu12_.id and job4_.LOCATOR_ID=locator9_.ID(+) and job4_.SKILL_LEVEL_ID=skilllevel8_.ID and job4_.SKILL_TYPE_ID=skilltyper7_.ID and job4_.STATUS_ID=jobstatusr6_.ID and job4_.PRIORITY_ID=priorityre5_.ID and jobschedul0_.JOB_ID=job4_.ID and workschedu1_.LOCATOR_ID=locator3_.ID and workschedu1_.PERSON_ID=person2_.ID and jobschedul0_.WORK_SCHEDULE_ID=workschedu1_.ID and workschedu1_.LOCATOR_ID=20000718228 and workschedu12_.code='"+JobStatusRef.Status.DUN+"' and (jobstatusr14_.CODE in ('"+JobStatusRef.Status.DJO+"','"+JobStatusRef.Status.DPD+"','"+JobStatusRef.Status.RFS+"')) and (jobschedul0_.DELETED_TIME is null) and " +
                "jobschedul0_.CREATED_TIME=(select max(jobschedul15_.CREATED_TIME) from JOB_SCHEDULE jobschedul15_ where jobschedul15_.JOB_ID=jobschedul0_.JOB_ID and (jobschedul15_.DELETED_TIME is null) and (jobschedul15_.created_time is not null))");
        */
        st.executeQuery("select workschedu0_.ID as ID27_0_, shiftref1_.ID as ID17_1_, workschedu2_.id as id28_2_, jobschedul3_.ID as ID6_3_, job4_.ID as ID3_4_, workschedu0_.DAY as DAY27_0_, workschedu0_.NOTSCHEDULABLE as NOTSCHED3_27_0_, workschedu0_.TIME as TIME27_0_, workschedu0_.UTILITY_RATING as UTILITY5_27_0_, workschedu0_.STATUS_ID as STATUS6_27_0_, workschedu0_.LOCATOR_ID as LOCATOR7_27_0_, workschedu0_.PERSON_ID as PERSON8_27_0_, workschedu0_.SHIFT_ID as SHIFT9_27_0_, shiftref1_.CODE as CODE17_1_, shiftref1_.DESCRIPTION as DESCRIPT3_17_1_, shiftref1_.SHIFTSTART as SHIFTSTART17_1_, shiftref1_.SHIFTEND as SHIFTEND17_1_, shiftref1_.TIME as TIME17_1_, workschedu2_.description as descript2_28_2_, workschedu2_.code as code28_2_, jobschedul3_.CREATED_TIME as CREATED2_6_3_, jobschedul3_.DELETED_TIME as DELETED3_6_3_, jobschedul3_.JOB_ID as JOB4_6_3_, jobschedul3_.WORK_SCHEDULE_ID as WORK5_6_3_, jobschedul3_.WORK_SCHEDULE_ID as WORK5_0__, jobschedul3_.ID as ID0__, job4_.CREATEDBY as CREATEDBY3_4_, job4_.CREATED_DATE as CREATED4_3_4_, job4_.DISPATCHED_DATE as DISPATCHED5_3_4_, job4_.STARTED_DATE as STARTED6_3_4_, job4_.FINISHBY as FINISHBY3_4_, job4_.ESTIMATEDTIME as ESTIMATE8_3_4_, job4_.LAST_UPDATED as LAST9_3_4_, job4_.NUMBER_WORKERS as NUMBER10_3_4_, job4_.COMPLETED_DATE as COMPLETED11_3_4_, job4_.EARLIEST_START as EARLIEST12_3_4_, job4_.LATEST_START as LATEST13_3_4_, job4_.DESCRIPTION as DESCRIP14_3_4_, job4_.SCHEDULED_DATE as SCHEDULED15_3_4_, job4_.SEQUENCE_LEVEL as SEQUENCE16_3_4_, job4_.STICKY as STICKY3_4_, job4_.JOB_TYPE_ID as JOB18_3_4_, job4_.OBJECT_ID as OBJECT19_3_4_, job4_.ORGANIZATION_ID as ORGANIZ20_3_4_, job4_.LOCATOR_ID as LOCATOR21_3_4_, job4_.SKILL_LEVEL_ID as SKILL22_3_4_, job4_.SKILL_TYPE_ID as SKILL23_3_4_, job4_.PRIORITY_ID as PRIORITY24_3_4_, job4_.STATUS_ID as STATUS2_3_4_, job4_.SCHEDULE_RESPONSIBILITY_ID as SCHEDULE25_3_4_, job4_.PROJECT_ID as PROJECT26_3_4_ from WORK_SCHEDULE workschedu0_, SHIFT_REF shiftref1_, WORK_SCHEDULE_STATUS_REF workschedu2_, JOB_SCHEDULE jobschedul3_, JOB job4_ where jobschedul3_.JOB_ID=job4_.ID and workschedu0_.ID=jobschedul3_.WORK_SCHEDULE_ID and ( jobschedul3_.DELETED_TIME IS NULL) and workschedu0_.STATUS_ID=workschedu2_.id and workschedu0_.SHIFT_ID=shiftref1_.ID and (workschedu2_.code in ('IP' , 'NYS')) and workschedu0_.DAY<=today()                ");
        logWarnings(con.getWarnings());
        con.clearWarnings();
        st.close();
        con.close();
    }

    public static Test suite() {
        return new TestSuite(SQLWarningTest.class);
    }

    public static void logWarnings(SQLWarning warning) {
        for(; warning != null; warning = warning.getNextWarning()) {
            StringBuffer buf = (new StringBuffer(30)).append("SQL Warning: ").append(warning.getErrorCode()).append(", SQLState: ").append(warning.getSQLState());
            System.out.println(buf.toString());
            System.out.println(warning.getMessage());
        }
    }
}

