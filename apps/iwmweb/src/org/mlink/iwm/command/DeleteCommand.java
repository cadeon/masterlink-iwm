package org.mlink.iwm.command;


import org.mlink.agent.model.World;
import org.mlink.agent.dao.WorldDAO;
import org.mlink.iwm.util.Config;
import org.mlink.agent.xao.SimResponse;
import org.mlink.agent.xao.WorldXAO;

import org.apache.log4j.Logger;

public class DeleteCommand extends SimCommand {
	private static final Logger logger = Logger.getLogger(DeleteCommand.class);

	private String simid;  // UI identifier
	private String target; // schema name
	
	private DeleteCommand() {super();}
	public DeleteCommand(String simid,String target) {
		this.simid=simid;
		this.target=target;
	}
	
	@Override
	public SimResponse execute()  {
		SimResponse sr = WorldXAO.getInstance().createSimResponse();
		sr.setAction(WorldXAO.DELETE);
		sr.setSimid(simid);
		
		String deleteCommand = Config.getProperty("delete.command");
		try {
			Process process = new ProcessBuilder(new String[] {deleteCommand,target,target}).start(); 
	        if (!processStatusLifecycle(process))
				throw new FailedCommandException("Schema delete failed.","DELETE");

			// Update world repository
			activateProductionSchema();
			World x = WorldDAO.getInstance().findByName(target);
			WorldDAO.getInstance().deleteWorld(x);
			activateSelectedSchema();
			
			sr.setStatus(WorldXAO.SUCCESS);
			
		} catch (Exception e) { // catches DAOException, FailedCommandException, IOException
			logger.error(e.getMessage(),e);
			sr.setStatus(WorldXAO.FAIL);
			sr.setDisplay("Delete attempt was not successful. Please try again later, or contact your IWM administrator.");
		}

		return sr;
	}
	
	protected  void log(String s) {
		logger.debug(s);
	}
}
