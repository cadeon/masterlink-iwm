package org.mlink.iwm.command;

import java.util.List;

import org.mlink.agent.model.World;
import org.mlink.agent.dao.WorldDAO;
import org.mlink.iwm.util.Config;
import org.mlink.agent.xao.SimResponse;
import org.mlink.agent.xao.WorldXAO;

import org.apache.log4j.Logger;

public class CloneCommand extends SimCommand {
	private static final Logger logger = Logger.getLogger(CloneCommand.class);

	private String origin;
	private String target;

	private CloneCommand() {
		super();
	}

	public CloneCommand(String origin, String target) {
		this.origin = origin;
		this.target = target;
	}

	@Override
	public SimResponse execute()  {
		SimResponse sr = WorldXAO.getInstance().createSimResponse();	
		sr.setAction(WorldXAO.CLONE);
		
		String copyCommand = Config.getProperty("copy.command");
		try {
			/*ProcessBuilder pb = new ProcessBuilder(new String[] { copyCommand, origin, origin, target, target });
			System.out.println(pb.directory());
			Process process = pb.start();
			if (!processStatusLifecycle(process))
		    throw new FailedCommandException("Schema copy failed.","CLONE");*/
			
			World x = new World(target, target);
			activateProductionSchema();
			WorldDAO.getInstance().saveWorld(x);
			activateSelectedSchema();
			// FIXME: Determine what to do if save fails: remove schema, or...?
			
			sr.setStatus(WorldXAO.SUCCESS);

		} catch (Exception e) { // catches DAOException, FailedCommandException, IOException
			logger.error(e.getMessage(),e);
			sr.setStatus(WorldXAO.FAIL);
			sr.setDisplay("Clone attempt  was not successful. Please try again later, or contact your IWM administrator.");
		}

		return sr;
	}

	protected  void log(String s) {
		logger.debug(s);
	}
}
