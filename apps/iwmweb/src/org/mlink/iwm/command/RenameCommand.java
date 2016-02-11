package org.mlink.iwm.command;

import org.apache.log4j.Logger;
import org.mlink.agent.dao.WorldDAO;
import org.mlink.agent.model.World;
import org.mlink.agent.xao.SimResponse;
import org.mlink.agent.xao.WorldXAO;

public class RenameCommand extends SimCommand {
	private static final Logger logger = Logger.getLogger(RenameCommand.class);

	private String simid;   // UI identifier
	private String target;  // schema name
	private String newname; 
	
	private RenameCommand() {super();}
	public RenameCommand(String simid,String target,String name) {
		this.simid=simid;
		this.target=target;
		this.newname = name;
	}
	
	@Override
	public SimResponse execute()  {
		SimResponse sr = WorldXAO.getInstance().createSimResponse();
		sr.setAction(WorldXAO.RENAME);
		sr.setSimid(simid);
		
		try {
			// Update world repository
			activateProductionSchema();
			World x = WorldDAO.getInstance().findByName(target);
			x.setName(newname);
			WorldDAO.getInstance().saveWorld(x);
			activateSelectedSchema();
			
			sr.setStatus(WorldXAO.SUCCESS);
			
		} catch (Exception e) { // catches DAOException, FailedCommandException, IOException
			logger.error(e.getMessage(),e);
			sr.setStatus(WorldXAO.FAIL);
			sr.setDisplay("Rename attempt was not successful. Please try again later, or contact your IWM administrator.");
		}

		return sr;
	}
	
	protected  void log(String s) {
		logger.debug(s);
	}

}
