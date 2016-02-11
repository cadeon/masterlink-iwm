package org.mlink.iwm.command;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.mlink.agent.dao.DAOException;
import org.mlink.agent.dao.WorldDAO;
import org.mlink.agent.xao.SimResponse;
import org.mlink.iwm.util.UserTrackHelper;

import org.apache.log4j.*;

public abstract class SimCommand {
	private static final Logger logger = Logger.getLogger(SimCommand.class);

	public abstract SimResponse execute();
	
	public void activateProductionSchema() throws DAOException {
		WorldDAO.getInstance().activateWorld(UserTrackHelper.getProductionSchema());
	}
	public void activateSelectedSchema() throws DAOException {
		if (UserTrackHelper.getSelectedSchema()==null) { 
			// If no selected, set to production until other selection made
			UserTrackHelper.setSelectedSchema(UserTrackHelper.getProductionSchema());
		}
		WorldDAO.getInstance().activateWorld(UserTrackHelper.getSelectedSchema());
	}
	
	protected boolean processStatusLifecycle(Process process) throws IOException, InterruptedException  {
		InputStream is = process.getInputStream();
		InputStreamReader isr = new InputStreamReader(is);
		BufferedReader br = new BufferedReader(isr);
		String line;
		while ((line = br.readLine()) != null) {
			log(line);
		}
		return  (0==process.waitFor());
	}
	protected void log(String s) {
		logger.debug(s);
	}
}
