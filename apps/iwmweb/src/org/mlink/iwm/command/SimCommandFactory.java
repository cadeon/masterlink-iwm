package org.mlink.iwm.command;

import javax.servlet.http.HttpServletRequest;

public class SimCommandFactory {

	public static SimCommand getCommand(HttpServletRequest request)
		throws InvalidParameterException 
	{
		SimCommand sc = null;
		String cmd = request.getParameter("cmd");
		if ("clone".equals(cmd)) {
			String origin = request.getParameter("origin");
			String target = request.getParameter("target");
			if (null==origin || origin.length()<1) 
				throw new InvalidParameterException("originating world","null");
			if (null==target || target.length()<1)
				throw new InvalidParameterException("target world","null");
			return new CloneCommand(origin,target);
		}
		if ("delete".equals(cmd)) {
			String simid = request.getParameter("simid");
			String target = request.getParameter("schema");
			if (null==simid||simid.length()<1)
				throw new InvalidParameterException("simid","null");
			if (null==target||target.length()<1)
				throw new InvalidParameterException("schema","null");
			return new DeleteCommand(simid,target);
		}
		if ("export".equals(cmd)) {
		}
		if ("import".equals(cmd)) {
		}
		if ("modify".equals(cmd)) {
		}
		if ("read".equals(cmd)) {
		}
		if ("rename".equals(cmd)) {
			//TODO: Rename must also update all children who have the target world as parent
		}
		if ("reports".equals(cmd)) {
		}
		if ("run".equals(cmd)) {
			String simid = request.getParameter("simid");
			String target = request.getParameter("schema");
			if (null==simid||simid.length()<1)
				throw new InvalidParameterException("simid","null");
			if (null==target||target.length()<1)
				throw new InvalidParameterException("schema","null");
			return new RunCommand(simid,target);
		}
		return sc;
	}
}
