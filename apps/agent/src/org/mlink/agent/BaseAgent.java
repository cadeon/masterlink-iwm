package org.mlink.agent;

import java.util.Collection;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.mlink.agent.dao.DAOException;
import org.mlink.iwm.exception.BusinessException;

public abstract class BaseAgent {
	private static final Logger logger = Logger.getLogger(BaseAgent.class);
	
	public static final String JSM           = "JobStateManager";
	public static final String PLANNER       = "Planner";
	public static final String SCHEDULER     = "Scheduler";
	public static final String SHIFT_MANAGER = "ShiftManager";
	
	private String name;

	protected BaseAgent(String name) {
		this.name = name;
	}
	
	public abstract Collection run(Collection c) throws BusinessException, DAOException;
	
	// Logging helper methods
	protected void log (Object o) {
		logger.debug(name +": "+o);
	}
	protected void error (Object o, Throwable t) {
		logger.error(o, t);
	}
	
	protected void debug(Object o) {
		if (logger.getLevel().isGreaterOrEqual(Level.DEBUG)) logger.debug(name +": "+o);
	}
	protected void info(Object o) {
		if (logger.getLevel().isGreaterOrEqual( Level.INFO)) logger.info(name +": "+o);
	}
	protected void warn(Object o) {
		if (logger.getLevel().isGreaterOrEqual( Level.WARN)) logger.warn(name +": "+o);
	}
	protected void error(Object o) {
		if (logger.getLevel().isGreaterOrEqual(Level.ERROR)) logger.error(name +": "+o);
	}
	protected void fatal(Object o) {
		if (logger.getLevel().isGreaterOrEqual(Level.FATAL)) logger.fatal(name +": "+o);
	}
}
