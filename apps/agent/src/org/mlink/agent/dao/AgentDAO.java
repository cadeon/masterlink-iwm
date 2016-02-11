package org.mlink.agent.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.hibernate.*;
import org.hibernate.cfg.Configuration;

public class AgentDAO {
	private static Configuration cfg = null;
	private static SessionFactory factory = null;
	private static AgentDAO instance = null;
	private Collection jobs;
	private Collection tasks;
	private Collection schedules;
	private ArrayList jobsRFS;

	private AgentDAO() {
	}

	public static AgentDAO getInstance() throws DAOException {
		if (instance == null) {
			try {
				cfg = new Configuration().configure();
				factory = cfg.buildSessionFactory();
			} catch (HibernateException e) {
				throw new DAOException(e.toString());
			}

			instance = new AgentDAO();
		}

		return instance;
	}

	public Collection getJobs() throws DAOException {
		return getTable("jobs");
	}


	public Collection getSchedules() throws DAOException {
    return getTable("WorkSchedule");
	}

	public Collection getTasks() throws DAOException {
    return getTable("task");
	}

 
	public Collection hql(String query) throws DAOException {
		Session session = null;
		Collection rows = null;
		Transaction tx = null;

		try {
			session = factory.openSession();
			tx = session.beginTransaction();
			rows = (Collection) session.createQuery(query).list(); 
			tx.commit();
		} catch (Exception e) {
			if (tx != null) {
				try {
					tx.rollback();
				} catch (HibernateException e1) {
					throw new DAOException(e1.toString());
				}
			}
      e.printStackTrace();  
			throw new DAOException(e.toString());
      
		} finally {
			if (session != null) {
				try {
					session.close();
				} catch (HibernateException e) {
            e.printStackTrace();   
				}
			}
		}
		return rows;
  }
	
	public Collection getTable(String tableName, String colWithOrder) throws DAOException {
		Set<String> colsWithOrder = new HashSet<String>();
		colsWithOrder.add(colWithOrder);
		return getTable(tableName, null, colsWithOrder);
	}

	public Collection getTable(String tableName, String whereClause, String colWithOrder) throws DAOException {
		Set<String> whereClauses = null, colsWithOrder = null;
		if(whereClause!=null && whereClause.length()!=0){
			whereClauses = new HashSet<String>();
			whereClauses.add(whereClause);
		}
		
		if(colWithOrder!=null && colWithOrder.length()!=0){
			colsWithOrder = new HashSet<String>();
			colsWithOrder.add(colWithOrder);
		}
		return getTable(tableName, whereClauses, colsWithOrder);
	}
	
	public Collection getTable(String tableName, Set<String> whereClauses, Set<String> colsWithOrder) throws DAOException {
	   capitalize(tableName);
	   StringBuffer query = new StringBuffer("from ");
	   query.append(tableName);
	   if(whereClauses!=null && !whereClauses.isEmpty()){
		   query.append(" where ");
		   for(String whereClause: whereClauses){
			   query.append(whereClause+", ");
		   }
		   query.replace(query.length()-2, query.length(), "  ");
	   }
	   
	   if(colsWithOrder!=null && !colsWithOrder.isEmpty()){
		   query.append(" order by ");
		   for(String colWithOrder: colsWithOrder){
			   query.append(colWithOrder+", ");
		   }
		   query.replace(query.length()-2, query.length(), "  ");
	   }
	   
	   return hql(query.toString());
	}

	public Collection getTable(String tableName) throws DAOException {
    return hql("from "+capitalize(tableName));
	}

  // Do we have a place for utilities such as ...
   public String capitalize(String s) {
        StringBuffer sb = new StringBuffer(s.length());
        sb.append(Character.toUpperCase(s.charAt(0)));
        sb.append(s.substring(1));
        return sb.toString();        
    }


}
