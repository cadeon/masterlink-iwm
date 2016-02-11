package org.mlink.agent.dao;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.mlink.iwm.util.Config;


public abstract class BaseDAO {
	protected static Logger logger = Logger.getLogger(BaseDAO.class);
	protected static SessionFactory factory     = null;
	protected static Configuration  cfg         = null;
	protected static Context ctx                = null;
	
	protected static int noOfSessions = 0;
	
	public BaseDAO() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	static {
		try {
			ctx = new InitialContext();
		} catch (NamingException ne) {
			ne.printStackTrace();
		}
	}

	protected Session getSession() throws DAOException {
		if (ctx==null) {
			return null;
		}
		noOfSessions++;
		if(noOfSessions>1){
			throw new DAOException("Opening session when another is still open");
		}
		Session session = null;
		try {
			String sfHandleStr = Config.getProperty("application.SessionFactory");
			SessionFactory factory =  (SessionFactory)ctx.lookup(sfHandleStr);
			session = factory.openSession();
			session.beginTransaction();
		} catch (HibernateException he) {
			he.printStackTrace();
			throw new DAOException(he);
		} catch (NamingException ne) {
			ne.printStackTrace();
			throw new DAOException(ne);
		}
		return session;		
	}
	protected void rollback(Transaction tx) throws DAOException {
		if (tx==null){
			return;
		}
		try {
			tx.rollback();
		} catch (HibernateException he) {
			he.printStackTrace();
			throw new DAOException(he);
		}
	}
	protected void close(Session session) throws DAOException {
		if (session==null) {
			return;
		}
		noOfSessions--;
		try {
			session.getTransaction().commit();
			if (session.isOpen()) {
				session.close();
			}
		} catch (HibernateException he) {
			he.printStackTrace();
			throw new DAOException(he);
		}
	}
	
	protected void log(Object o) {logger.debug(o);}
	protected void error(Object o, Throwable t) {logger.error(o, t);}
}
