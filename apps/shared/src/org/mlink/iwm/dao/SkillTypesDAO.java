package org.mlink.iwm.dao;


import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.mlink.iwm.lookup.JobStatusRef;

/**
 * User: andrei
 * Date: Oct 30, 2006
 */
public class SkillTypesDAO extends ListDAOTemplate {
    protected static Properties nameToColumnMap = new Properties();
    static {
    	nameToColumnMap.put("id","ID");
        nameToColumnMap.put("code","CODE");
        nameToColumnMap.put("displayOrder","DISP_ORD");
        nameToColumnMap.put("description","DESCRIPTION");
        nameToColumnMap.put("createdDate","CREATED_DATE");
        nameToColumnMap.put("archivedDate","ARCHIVED_DATE");
        nameToColumnMap.put("workerCount","WORKER_COUNT");
        nameToColumnMap.put("jobCount","JOB_COUNT");
        nameToColumnMap.put("templateCount","TEMPLATE_COUNT");
    }
    
    /**
     * Get SkillTypes
     * @param criteria
     * @param request
     * @return
     * @throws java.sql.SQLException
     */
    public PaginationResponse getData(SearchCriteria criteria, PaginationRequest request) throws SQLException {
        SkillTypesCriteria cr = (SkillTypesCriteria)criteria;
        String sql = getSql(cr);
        List parameters = new ArrayList();
        if(cr!=null){
        	if(cr.getId()!=null){
        		parameters.add(cr.getId());
        	}
        }
        return process(parameters,sql,request);
    }

    protected String getSql(SearchCriteria criteria) {
    	SkillTypesCriteria cr = (SkillTypesCriteria)criteria;
    	String type = (cr!=null)?cr.getType():null;
        StringBuilder sql = new StringBuilder();
        if(cr == null || SkillTypesCriteria.Types.None.toString().equals(type)){
	        sql.append("SELECT ID, CODE, DISP_ORD, DESCRIPTION, CREATED_DATE, ARCHIVED_DATE");
	        sql.append(", (SELECT COUNT(S.ID) FROM SKILL S, person p WHERE S.SKILL_TYPE_ID=STR.ID and s.person_id= p.id and p.active=1) WORKER_COUNT");
	        sql.append(", (SELECT COUNT(J.ID) FROM JOB J, JOB_STATUS_REF JSR WHERE J.skill_type_id= str.id and J.status_id=JSR.ID AND JSR.code not in "+JobStatusRef.finalStatusesSQLClauseMinusNIA+") JOB_COUNT");
	        sql.append(", (select count(unique(od.id)) from object_def od, task_def td where od.id = td.object_def_id and td.skill_type_id= str.id and td.archived_date is null) TEMPLATE_COUNT");
	        sql.append(" FROM SKILL_TYPE_REF STR");
	        sql.append(" WHERE ARCHIVED_DATE IS NULL");
        }else if(SkillTypesCriteria.Types.Workers.toString().equals(type)){
        	sql.append("SELECT P.USER_NAME CODE, DECODE(P.ACTIVE,1,'Yes','No') DESCRIPTION FROM SKILL S, person p WHERE S.SKILL_TYPE_ID=? and s.person_id= p.id");
	    }else if(SkillTypesCriteria.Types.Jobs.toString().equals(type)){
	    	sql.append("SELECT J.ID || ' - ' || J.DESCRIPTION CODE, JSR.CODE DESCRIPTION FROM JOB J, JOB_STATUS_REF JSR WHERE J.SKILL_TYPE_ID=? " +
	    			"and J.STATUS_ID=JSR.ID AND JSR.CODE NOT IN "+JobStatusRef.finalStatusesSQLClauseMinusNIA);
	    }else if(SkillTypesCriteria.Types.Templates.toString().equals(type)){
	    	sql.append("SELECT '['||oc.code||'] '||oc.description CODE, count(td.id) DESCRIPTION from object_def od, OBJECT_CLASSIFICATION oc, task_def td " +
	    			"where od.class_id= oc.id and od.id = td.object_def_id and td.skill_type_id=? and td.archived_date is null group by oc.code, oc.description");
	    }
        if(cr!=null){
        	cr.setType(null);
        }
        return sql.toString();
    }

    protected  Properties getPropertyToColumnMap(){
        return nameToColumnMap;
    }
}