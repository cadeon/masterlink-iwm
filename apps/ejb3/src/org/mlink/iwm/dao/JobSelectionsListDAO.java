package org.mlink.iwm.dao;


import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.mlink.iwm.base.BaseListDAO;
import org.mlink.iwm.entity3.Party;
import org.mlink.iwm.entity3.Person;
import org.mlink.iwm.exception.IncompleteStateException;


/**
 * Created by Andrei Povodyrev
 * <p/>
 * Date: Jul 17, 2004
 */

public class JobSelectionsListDAO extends BaseListDAO {
    final private String tables = " PERSON L, PARTY P, WORK_SCHEDULE WS, " +
            " JOB_SCHEDULE JS ";

    // select statement uses fields
    final private String fields = "L.ID, P.NAME ";
    public String getSql()
    {
        StringBuffer sql = new StringBuffer("SELECT ");
        sql.append(fields);
        sql.append(" FROM " + tables);
        sql.append(" WHERE L.PARTY_ID = P.ID " +
                " AND L.ID = WS.PERSON_ID " +
                " AND WS.ID = JS.WORK_SCHEDULE_ID AND JS.DELETED_TIME IS NULL" +
                " AND JS.JOB_ID = ? " +
                " AND WS.DAY = ? ");
        return sql.toString();

    }


    /**
     * criteria[0]=Long jobId
     *
     * @param criteria
     * @throws IncompleteStateException
     */

    public void setParameters(List criteria) throws IncompleteStateException {
        try {
            Long jobId =(Long) criteria.get(0);
            java.sql.Date scheduledDate = (java.sql.Date) criteria.get(1);
            this.parameters = new ArrayList();
            parameters.add(jobId);
            parameters.add(scheduledDate);
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String error = this.getClass().getName() + " requires following criteria: " +
                    " criteria[0]=Long jobId, " +
                    " criteria[1]=java.sql.Date scheduledDate " + sw.toString();
            throw new IncompleteStateException(error);
        }
    }


    public List prepareResult(ResultSet rs)
            throws SQLException {
        List list = new ArrayList();
        while (rs.next()) {
            int i = 1;
            Person person = new Person();
            Party party = new Party();
            long id = rs.getLong(i++);
            if (!rs.wasNull())
                person.setId(new Long(id));

            party.setName(rs.getString(i++));
            person.setParty(party);
            list.add(person);
        }
        return list;
    }
}



