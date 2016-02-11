package org.mlink.iwm.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.mlink.iwm.base.BaseListDAO;
import org.mlink.iwm.entity3.TenantRequest;
import org.mlink.iwm.exception.IncompleteStateException;

/**
 * Created by Andrei Povodyrev
 * Date: Jul 17, 2004
 */
public class ExternalWorkRequestListDAO extends  BaseListDAO {
     private String sql = "SELECT TR.ID, TR.LOCATOR_ID, TR.PROBLEM_ID, TR.JOB_ID, " +
             "TR.TENANT_NAME,TR.EMAIL,TR.NOTE,TR.PHONE,TR.REQUEST_TYPE,TR.CREATED_DATE,TR.LOCATION_COMMENT " +
             "FROM TENANT_REQUEST_UNIQUE TR, ACTIVE_JOBS_VIEW AJ " +
             "WHERE TR.JOB_ID=AJ.ID AND TR.REQUEST_TYPE='"+ TenantRequest.EXTERNAL_REQUEST+"' " +
             "ORDER BY TR.ID DESC";

    public String getSql() {
        return sql;
    }

    public void setParameters(List criteria) throws IncompleteStateException {}

    public List prepareResult(ResultSet rs)
            throws SQLException {
        List list = new ArrayList();
        while(rs.next()) {
            int i = 1;
            TenantRequest vo = new TenantRequest();
            long id = rs.getLong(i++);
            if(!rs.wasNull()) vo.setId(id);
            id = rs.getLong(i++);
            if(!rs.wasNull()) vo.setLocatorId(id);
            id = rs.getLong(i++);
            if(!rs.wasNull()) vo.setProblemId(id);
            id = rs.getLong(i++);
            if(!rs.wasNull()) vo.setJobId(id);

            vo.setTenantName(rs.getString(i++));
            vo.setTenantEmail(rs.getString(i++));
            vo.setNote(rs.getString(i++));
            vo.setTenantPhone(rs.getString(i++));
            vo.setRequestType(rs.getString(i++));
            vo.setCreatedDate(rs.getTimestamp(i++));
            vo.setLocationComment(rs.getString(i++));
            list.add(vo);
        }
        return list;
    }
}


