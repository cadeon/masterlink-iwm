package org.mlink.iwm.struts.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.mlink.agent.dao.DAOException;
import org.mlink.agent.dao.WorldDAO;
import org.mlink.agent.model.World;
import org.mlink.iwm.access3.ServiceLocator;
import org.mlink.iwm.base.WebException;
import org.mlink.iwm.exception.BusinessException;
import org.mlink.iwm.session.AgentSF;
import org.mlink.iwm.struts.form.WorldSummaryForm;
import org.mlink.iwm.util.UserTrackHelper;

public class WorldSummary extends BaseAction {
	private static final Logger logger = Logger.getLogger(WorldSummary.class);

	public ActionForward executeLogic(ActionMapping mapping, ActionForm aform,
			HttpServletRequest request, HttpServletResponse response)
			throws WebException {
		// no form for this servlet -- AJAX receiver
		String agent   = request.getParameter("agent");
		String forward = request.getParameter("forward");
		if (forward == null)
			forward = "read";
		WorldSummaryForm form;
		if (aform != null)
			form = (WorldSummaryForm) aform;
		else
			form = new WorldSummaryForm();

		ActionForward af = findForward(mapping, request);
		try {

			logger.debug("execute " + forward);
			if ("read".equals(forward)) {
				if ("planner".equals(agent)) 
					runPlanner(form);
				else
					loadWorlds(mapping, form, request, response);
			} else if ("modify".equals(forward)) {
			} else if ("reports".equals(forward)) {
			} else if ("run".equals(forward)) {
			}
			/*else { // try to create Command class
				String s =  executeCommand(request,response);
				response.addHeader("simCommandResponse", s);
			}*/
		} catch (BusinessException be) {
			form.setMessage(be.getMessage());
		}catch(Exception ex){
            throw new WebException(ex);
        }

		return af;
	}

	private void loadWorlds(ActionMapping mapping, WorldSummaryForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws BusinessException, Exception {
		try {
			WorldDAO.getInstance().activateWorld(
					UserTrackHelper.getProductionSchema());
			List<World> l = WorldDAO.getInstance().getAllWorlds();
			form.getWorlds().addAll(l);
			WorldDAO.getInstance().activateWorld(
					UserTrackHelper.getSelectedSchema());
		} catch (DAOException de) {
			logger.error("Failed to load worlds from production.", de);
			try {
				WorldDAO.getInstance().activateWorld(
						UserTrackHelper.getSelectedSchema());
			} catch (DAOException de2) {
				logger.error("Failure setting world to '"
						+ UserTrackHelper.getSelectedSchema() + "' schema.",
						de2);
			}
			throw new BusinessException(de.getMessage());
		}
	}
	
	private void runPlanner(WorldSummaryForm form) throws BusinessException, Exception {
		try {
			AgentSF asf = ServiceLocator.getAgentSFLocal( );
			asf.runPlanner();
		} catch (Exception e) {
			logger.error("Error during Planner run.", e);
			throw new BusinessException(e.getMessage());
		}
		
	}

}
