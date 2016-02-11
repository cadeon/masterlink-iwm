package org.mlink.iwm.dao;

import org.mlink.iwm.util.Constants;
import org.mlink.iwm.lookup.ScheduleResponsibilityRef;
import org.mlink.iwm.lookup.JobStatusRef;
import org.mlink.iwm.lookup.SkillTypeRef;
import org.apache.commons.lang.time.DateUtils;

import java.util.*;
import java.sql.SQLException;

/**
 * User: andrei
 * Date: Oct 31, 2006
 */
public class JobsDAO extends ListDAOTemplate {
    protected static Properties nameToColumnMap = new Properties();
    static {
        nameToColumnMap.put("id","ID");
        nameToColumnMap.put("status","JOB_STATUS");
        nameToColumnMap.put("objectRef","OBJECT_REF");
        nameToColumnMap.put("objectClassId","CLASS_ID");
        nameToColumnMap.put("locatorId","LOCATOR_ID");
        nameToColumnMap.put("description","DESCRIPTION");
        nameToColumnMap.put("fullLocator","FULL_LOCATOR");
        nameToColumnMap.put("skillType","SKILL_TYPE");
        nameToColumnMap.put("jobType","JOB_TYPE");
        nameToColumnMap.put("taskCount","TASK_COUNT");
        nameToColumnMap.put("createdDate","CREATED_DATE");
        nameToColumnMap.put("estTime","ESTIMATEDTIME");
        nameToColumnMap.put("sequenceLevel","SEQUENCE_LEVEL");
        nameToColumnMap.put("earliestStart","EARLIEST_START");
        nameToColumnMap.put("startedDate","STARTED_DATE");
        nameToColumnMap.put("totalTime","TOTAL_TIME");
        nameToColumnMap.put("organizationId","ORGANIZATION_ID");
        nameToColumnMap.put("refId","REF_ID");
        nameToColumnMap.put("objectId","OBJECT_ID");
    }

    /**
     * Get all objects that recursively traverse to the given class and locator
     * @param request
     * @return
     * @throws java.sql.SQLException
     */
    public PaginationResponse getData(SearchCriteria criteria, PaginationRequest request) throws SQLException {
        JobsCriteria cr = (JobsCriteria)criteria;
        List params = new ArrayList();
        if(cr.getClassId()!=null)  params.add(cr.getClassId());
        if(cr.getLocatorId()!=null) params.add(cr.getLocatorId());
        if(cr.getOrganizationId()!=null) params.add(cr.getOrganizationId());
        if(cr.getJobType()!=null) params.add(cr.getJobType());
        if(cr.getJobStatus()!=null) params.add(cr.getJobStatus());
        if(cr.getSkillType()!=null) params.add(cr.getSkillType());
        if(cr.getObjectId()!=null) params.add(cr.getObjectId());
        if(cr.getProjectId()!=null) params.add(cr.getProjectId());
        switch(cr.getFailureMode()){
            case NoWorker:
            case WorkerAvailableNotAutoAssigned:
                cr.setJobCategory(Constants.ACTIVE_JOBS_CATEGORY); // only active interested
                params.add(new java.sql.Date(System.currentTimeMillis()));
                break;
            case SoonToExpire:
                Date dt = DateUtils.addWeeks(new java.sql.Date(System.currentTimeMillis()),1);
                params.add(new java.sql.Date(dt.getTime()));
                break;
            case Expired:
                params.add(new java.sql.Date(System.currentTimeMillis()));
                break;
            default:
        }
        return process(params,cr,request);
    }

    /**
     * need to override parent's process method to optimize since jobsDAO is the heaviest one of all
     * @param params
     * @param request
     * @return
     * @throws SQLException
     */
    protected PaginationResponse process(List params, SearchCriteria criteria, PaginationRequest request) throws SQLException {
        String countSql = "SELECT COUNT(*) CNT FROM ("+ getSql(criteria, true)+ ")";
        Object cnt = getData(params,countSql).get(0).get("CNT");
        int count = Integer.parseInt(cnt.toString());
        List <Map> data = getData(params,prepareDataSql(getSql(criteria,false),request));
        return new PaginationResponse(data,count,request.getOffset(),request.getPageSize());
    }

    protected String getSql(SearchCriteria criteria) {
        return getSql(criteria,false);
    }
    private String getSql4(SearchCriteria criteria, boolean isForTotalCount) {
        JobsCriteria cr = (JobsCriteria) criteria;
        String jobCategory;
        if(Constants.ACTIVE_JOBS_CATEGORY.equals(cr.getJobCategory())){
            jobCategory = " AND JS.CODE NOT IN "+JobStatusRef.finalStatusesSQLClauseMinusNIA;
        }else if (Constants.TERMINAL_JOBS_CATEGORY.equals(cr.getJobCategory())){
            jobCategory = " AND JS.CODE IN "+JobStatusRef.finalStatusesSQLClauseMinusNIA;
        }else{
            jobCategory = "";
        }
        String fields = "J.ID, D.OBJECT_REF, D.CLASS_ID,D.LOCATOR_ID, J.DESCRIPTION, L.FULL_LOCATOR, STR.CODE SKILL_TYPE, " +
                "TTR.CODE JOB_TYPE,JS.DESCRIPTION JOB_STATUS, J.CREATED_DATE, J.ESTIMATEDTIME, J.SEQUENCE_LEVEL,J.ORGANIZATION_ID ";
        String tableName = " FROM OBJECT D, JOB J, JOB_STATUS_REF JS, TASK_TYPE_REF TTR, SKILL_TYPE_REF STR ";
        StringBuilder where = new StringBuilder("WHERE TTR.ID=J.JOB_TYPE_ID AND STR.ID=J.SKILL_TYPE_ID AND J.LOCATOR_ID=L.ID " +
                "AND J.STATUS_ID=JS.ID  AND J.ARCHIVED_DATE IS NULL  AND J.OBJECT_ID=D.ID  " +jobCategory) ;

        if(cr.getClassId()!=null){
            tableName += ",(SELECT ID, CODE FROM OBJECT_CLASSIFICATION START WITH ID=?  CONNECT BY PRIOR ID=PARENT_ID) OC ";
            where.append(" AND D.CLASS_ID = OC.ID ");
        }

        if(cr.getLocatorId()==null){
            tableName += ", LOCATOR L ";
        }else{
            tableName += " ,(SELECT ID,FULL_LOCATOR FROM LOCATOR START WITH ID=? CONNECT BY PRIOR ID=PARENT_ID) L ";
        }

        if(!isForTotalCount){
            fields += ", nvl(TASK_COUNT,0) TASK_COUNT";
            where.append(" AND CNT.JOB_ID(+)=J.ID ");
            tableName += ", (SELECT T.JOB_ID, COUNT(T.ID) TASK_COUNT FROM JOB_TASK T  GROUP BY T.JOB_ID) CNT ";
        }

        if(cr.getOrganizationId()!=null){
            tableName += ",(SELECT * FROM ORGANIZATION START WITH ID=? CONNECT BY PRIOR ID=PARENT_ID) ORG ";
            where.append(" AND J.ORGANIZATION_ID(+)=ORG.ID ");
        }

        if(cr.getJobType()!=null){
            where.append(" AND J.JOB_TYPE_ID=? ");
        }

        if(cr.getJobStatus()!=null){
            where.append(" AND J.STATUS_ID=? ");
        }

        if(cr.getSkillType()!=null){
            where.append(" AND J.SKILL_TYPE_ID=?");
        }

        if(cr.getObjectId()!=null){
            where.append(" AND D.ID=?");
        }
        if(cr.getProjectId()!=null){
            where.append(" AND J.PROJECT_ID=?");
        }

        if(cr.getFilterText()!=null){
            String like = " AND (J.ID LIKE '%FTV%' OR upper(D.OBJECT_REF) LIKE '%FTV%' " +
                    "OR upper(J.DESCRIPTION) LIKE '%FTV%' OR upper(L.FULL_LOCATOR) LIKE '%FTV%' " +
                    "OR upper(STR.DESCRIPTION) LIKE '%FTV%' OR upper(TTR.DESCRIPTION) LIKE '%FTV%' " +
                    "OR upper(JS.CODE) LIKE '%FTV%')";
            where.append(like.replaceAll("FTV",cr.getFilterText().toUpperCase()));
        }

        switch(cr.getFailureMode()){
            case NoWorker:
                where.append(" AND NOT EXISTS (" +
                        "SELECT WS.ID  FROM  PERSON PSN,  WORK_SCHEDULE WS, SKILL S, SKILL_LEVEL_REF RP, " +
                        "SKILL_LEVEL_REF RJ, JOB J2  " +
                        "WHERE PSN.ID = WS.PERSON_ID AND S.PERSON_ID=PSN.ID AND S.SKILL_TYPE_ID=J2.SKILL_TYPE_ID AND PSN.ACTIVE = 1  " +
                        "AND S.SKILL_LEVEL_ID=RP.ID AND RJ.ID = J2.SKILL_LEVEL_ID AND RP.VALUE >= RJ.VALUE  " +
                        "AND WS.LOCATOR_ID IN (SELECT ID FROM LOCATOR START WITH ID=J2.LOCATOR_ID CONNECT BY PRIOR PARENT_ID=ID) " +
                        "AND J2.ID = J.ID AND WS.DAY = ? ) ");
                break;
            case WorkerAvailableNotAutoAssigned:
                where.append(" AND J.STATUS_ID="+ JobStatusRef.getIdByCode(JobStatusRef.Status.RFS) + " AND  EXISTS (" +
                        "SELECT WS.ID  FROM  PERSON PSN,  WORK_SCHEDULE WS, SKILL S, SKILL_LEVEL_REF RP, " +
                        "SKILL_LEVEL_REF RJ, JOB J2  " +
                        "WHERE PSN.ID = WS.PERSON_ID AND S.PERSON_ID=PSN.ID AND S.SKILL_TYPE_ID=J2.SKILL_TYPE_ID AND PSN.ACTIVE = 1  " +
                        "AND S.SKILL_LEVEL_ID=RP.ID AND RJ.ID = J2.SKILL_LEVEL_ID AND RP.VALUE >= RJ.VALUE  " +
                        "AND WS.LOCATOR_ID IN (SELECT ID FROM LOCATOR START WITH ID=J2.LOCATOR_ID CONNECT BY PRIOR PARENT_ID=ID) " +
                        "AND J2.ID = J.ID AND WS.DAY = ? ) ");
                break;
            case ManualSchedule:
                where.append(" AND J.SCHEDULE_RESPONSIBILITY_ID="+ScheduleResponsibilityRef.getIdByCode(ScheduleResponsibilityRef.MANUAL));
                break;
            case NYAJobs:
                where.append(" AND J.STATUS_ID="+ JobStatusRef.getIdByCode(JobStatusRef.Status.NYA));
                break;
            case SoonToExpire:
                where.append(" AND J.LATEST_START >= ?");
                break;
            case Expired:
                where.append(" AND J.LATEST_START <= ?");
                break;
            case MustAssignSkill:
                where.append(" AND J.SKILL_TYPE_ID ="+ SkillTypeRef.getIdByCode(SkillTypeRef.MUST_ASSIGN));
                break;
            default:
        }



        //String sql = "SELECT /*+ FIRST_ROWS(20) */ " + fields + " FROM "  + tableName + ", (" +counters +") CNT  WHERE CNT.JOB_ID(+)=J.ID AND " + where;
        /**hints are from Irina to command oracle to retrieve data as it gets fetched and use multiple threads and minimize statistics during compilation*/
        String sql = "SELECT /*+ FIRST_ROWS(50) SPREAD_MIN_ANALYSIS parallel (job,4) */" + fields + tableName  + where;

        return sql;
    }

    private String getSql(SearchCriteria criteria, boolean isForTotalCount) {
        JobsCriteria cr = (JobsCriteria) criteria;
        String jobCategory;
        if(Constants.ACTIVE_JOBS_CATEGORY.equals(cr.getJobCategory())){
            jobCategory = " AND JS.CODE IN "+JobStatusRef.activeStatusesSQLClause;
        }else if (Constants.TERMINAL_JOBS_CATEGORY.equals(cr.getJobCategory())){
            jobCategory = " AND JS.CODE IN "+JobStatusRef.finalStatusesSQLClauseMinusNIA;
        }else if (Constants.PENDING_JOBS_CATEGORY.equals(cr.getJobCategory())){
        	jobCategory = " AND JS.CODE IN "+JobStatusRef.pendingStatusesSQLClause;
        }else{
            jobCategory = "";
        }
        String fields = "J.ID, D.OBJECT_REF, D.CLASS_ID,D.LOCATOR_ID, J.DESCRIPTION, L.FULL_LOCATOR, STR.CODE SKILL_TYPE, " +
                "TTR.CODE JOB_TYPE,JS.DESCRIPTION JOB_STATUS, J.CREATED_DATE, J.ESTIMATEDTIME, J.SEQUENCE_LEVEL,J.EARLIEST_START,J.STARTED_DATE,J.TOTAL_TIME, J.ORGANIZATION_ID, J.REF_ID, J.OBJECT_ID  ";
        String tableName = " FROM OBJECT D, JOB J, JOB_STATUS_REF JS, TASK_TYPE_REF TTR, SKILL_TYPE_REF STR ";
        StringBuilder where = new StringBuilder("WHERE TTR.ID=J.JOB_TYPE_ID AND STR.ID=J.SKILL_TYPE_ID AND J.LOCATOR_ID=L.ID " +
                "AND J.STATUS_ID=JS.ID  AND J.ARCHIVED_DATE IS NULL  AND J.OBJECT_ID=D.ID  " +jobCategory) ;

        if(cr.getClassId()!=null){
            tableName += ",(SELECT ID, CODE FROM OBJECT_CLASSIFICATION START WITH ID=?  CONNECT BY PRIOR ID=PARENT_ID) OC ";
            where.append(" AND D.CLASS_ID = OC.ID ");
        }

        if(cr.getLocatorId()==null){
            tableName += ", LOCATOR L ";
        }else{
            tableName += " ,(SELECT ID,FULL_LOCATOR FROM LOCATOR START WITH ID=? CONNECT BY PRIOR ID=PARENT_ID) L ";
        }

        if(!isForTotalCount){
            /*fields += ", nvl(TASK_COUNT,0) TASK_COUNT";
            where.append(" AND CNT.JOB_ID(+)=J.ID ");
            tableName += ", (SELECT T.JOB_ID, COUNT(T.ID) TASK_COUNT FROM JOB_TASK T  GROUP BY T.JOB_ID) CNT ";
            */
            fields += ", (SELECT COUNT(T.ID)  FROM JOB_TASK T WHERE T.JOB_ID=J.ID) TASK_COUNT ";
        }

        if(cr.getOrganizationId()!=null){
            tableName += ",(SELECT * FROM ORGANIZATION START WITH ID=? CONNECT BY PRIOR ID=PARENT_ID) ORG ";
            where.append(" AND J.ORGANIZATION_ID(+)=ORG.ID ");
        }

        if(cr.getJobType()!=null){
            where.append(" AND J.JOB_TYPE_ID=? ");
        }

        if(cr.getJobStatus()!=null){
            where.append(" AND J.STATUS_ID=? ");
        }

        if(cr.getSkillType()!=null){
            where.append(" AND J.SKILL_TYPE_ID=?");
        }

        if(cr.getObjectId()!=null){
            where.append(" AND D.ID=?");
        }
        if(cr.getProjectId()!=null){
            where.append(" AND J.PROJECT_ID=?");
        }

        if(cr.getFilterText()!=null){
            String like = " AND (J.ID LIKE '%FTV%' OR upper(D.OBJECT_REF) LIKE '%FTV%' " +
                    "OR upper(J.DESCRIPTION) LIKE '%FTV%' OR upper(L.FULL_LOCATOR) LIKE '%FTV%' " +
                    "OR upper(STR.DESCRIPTION) LIKE '%FTV%' OR upper(TTR.DESCRIPTION) LIKE '%FTV%' " +
                    "OR upper(JS.CODE) LIKE '%FTV%' OR upper(J.REF_ID) LIKE '%FTV%')";
            where.append(like.replaceAll("FTV",cr.getFilterText().toUpperCase()));
        }
        
        if(cr.getCompletingWorkerId()!=null){
        	//If we're looking for a worker, we only want completed state jobs.
        	where.append(" AND J.ID IN (SELECT JOB_ID FROM JOB_STATUS_HIST WHERE ASSIGNED_WKR_ID="+cr.getCompletingWorkerId()+
        			" AND NEW_STATUS_ID="+JobStatusRef.getIdByCode("DUN")+
        			" AND USER_ID="+cr.getCompletingWorkerId()+")");
        }

        switch(cr.getFailureMode()){
            case NoWorker:
                where.append(" AND NOT EXISTS (" +
                        "SELECT WS.ID  FROM  PERSON PSN,  WORK_SCHEDULE WS, SKILL S, SKILL_LEVEL_REF RP, " +
                        "SKILL_LEVEL_REF RJ, JOB J2  " +
                        "WHERE PSN.ID = WS.PERSON_ID AND S.PERSON_ID=PSN.ID AND S.SKILL_TYPE_ID=J2.SKILL_TYPE_ID AND PSN.ACTIVE = 1  " +
                        "AND S.SKILL_LEVEL_ID=RP.ID AND RJ.ID = J2.SKILL_LEVEL_ID AND RP.VALUE >= RJ.VALUE  " +
                        "AND WS.LOCATOR_ID IN (SELECT ID FROM LOCATOR START WITH ID=J2.LOCATOR_ID CONNECT BY PRIOR PARENT_ID=ID) " +
                        "AND J2.ID = J.ID AND WS.DAY = ? ) ");
                break;
            case WorkerAvailableNotAutoAssigned:
                where.append(" AND J.STATUS_ID="+ JobStatusRef.getIdByCode(JobStatusRef.Status.RFS) + " AND  EXISTS (" +
                        "SELECT WS.ID  FROM  PERSON PSN,  WORK_SCHEDULE WS, SKILL S, SKILL_LEVEL_REF RP, " +
                        "SKILL_LEVEL_REF RJ, JOB J2  " +
                        "WHERE PSN.ID = WS.PERSON_ID AND S.PERSON_ID=PSN.ID AND S.SKILL_TYPE_ID=J2.SKILL_TYPE_ID AND PSN.ACTIVE = 1  " +
                        "AND S.SKILL_LEVEL_ID=RP.ID AND RJ.ID = J2.SKILL_LEVEL_ID AND RP.VALUE >= RJ.VALUE  " +
                        "AND WS.LOCATOR_ID IN (SELECT ID FROM LOCATOR START WITH ID=J2.LOCATOR_ID CONNECT BY PRIOR PARENT_ID=ID) " +
                        "AND J2.ID = J.ID AND WS.DAY = ? ) ");
                break;
            case ManualSchedule:
                where.append(" AND J.SCHEDULE_RESPONSIBILITY_ID="+ScheduleResponsibilityRef.getIdByCode(ScheduleResponsibilityRef.MANUAL));
                break;
            case NYAJobs:
                where.append(" AND J.STATUS_ID="+ JobStatusRef.getIdByCode(JobStatusRef.Status.NYA));
                break;
            case SoonToExpire:
                where.append(" AND J.LATEST_START >= ?");
                break;
            case Expired:
                where.append(" AND J.LATEST_START <= ?");
                break;
            case MustAssignSkill:
                where.append(" AND J.SKILL_TYPE_ID ="+ SkillTypeRef.getIdByCode(SkillTypeRef.MUST_ASSIGN));
                break;
            default:
        }



        //String sql = "SELECT /*+ FIRST_ROWS(20) */ " + fields + " FROM "  + tableName + ", (" +counters +") CNT  WHERE CNT.JOB_ID(+)=J.ID AND " + where;
        /**hints are from Irina to command oracle to retrieve data as it gets fetched and use multiple threads and minimize statistics during compilation*/
        //String sql = "SELECT /*+ FIRST_ROWS(50) SPREAD_MIN_ANALYSIS parallel (job,4) */" + fields + tableName  + where;
        String sql = "SELECT " + fields + tableName  + where;

        return sql;
    }


    protected  Properties getPropertyToColumnMap(){
        return nameToColumnMap;
    }
}
