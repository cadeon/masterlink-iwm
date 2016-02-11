package org.mlink.iwm.command;

import org.mlink.agent.model.World;
import org.mlink.agent.dao.WorldDAO;
import org.mlink.agent.xao.SimResponse;
import org.mlink.agent.xao.WorldXAO;
import org.mlink.iwm.util.UserTrackHelper;

import org.apache.log4j.Logger;

public class RunCommand extends SimCommand {
	private static final Logger logger = Logger.getLogger(RunCommand.class);

	String simid;
	String schema;
	
	private RunCommand() {
		super();
	}
	public RunCommand(String simid,String schema) {
		this.simid=simid;
		this.schema=schema;
	}
	
	@Override
	public SimResponse execute()  {
		SimResponse sr = WorldXAO.getInstance().createSimResponse();
		sr.setAction(WorldXAO.RUN);
		sr.setSimid(simid);
		try {
			UserTrackHelper.setSelectedSchema(schema);
			activateProductionSchema();
			World x = WorldDAO.getInstance().findBySchema(schema);
			//FIXME: Need to lock the world here
			// Create world connection, generate token, save world, and 
			// pass to sim controller
			
			activateSelectedSchema();
		} catch (Exception e)  { // catches DAOException
			logger.error(e.getMessage(),e);
			sr.setStatus(WorldXAO.FAIL);
			sr.setDisplay("Run attempt  was not successful. Please try again later, or contact your IWM administrator.");
		}

		return sr;
	}
}
