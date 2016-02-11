package org.mlink.iwm.struts.action;

import org.mlink.iwm.base.WebException;
import org.mlink.iwm.util.DBAccess;
import org.mlink.iwm.util.Constants;
import org.mlink.iwm.util.UserTrackHelper;
import org.mlink.iwm.struts.form.WorldsMaintForm;
import org.mlink.iwm.struts.form.WorldLI;
import org.mlink.iwm.struts.action.BaseAction;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForm;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Andrei
 * Date: Mar 5, 2006
 * Time: 2:17:21 PM
 * Supports functionality of changing schema to which application is attached to.
 * List of schemas is hardcoded foe now as still in testing. It will be read from DB from Worlds table
 */
public class WorldsMaint extends BaseAction {
    private static final Logger logger = Logger.getLogger(WorldsMaint.class);

    public ActionForward executeLogic(ActionMapping mapping,
                                      ActionForm aform,
                                      HttpServletRequest request,
                                      HttpServletResponse response)  throws WebException {
        logger.debug("execute");
        WorldsMaintForm form  = (WorldsMaintForm)aform;
        ActionForward forward = findForward(mapping, request);


        if("select".equals(form.getForward())){
            processSelect(mapping, form,request, response);
        }if("delete".equals(form.getForward())){
        processDelete(mapping, form,request, response);
    }else{ //("read".equals(form.getForward())){
        processView(mapping, form,request, response);
    }

        return forward;
    }

    private void processDelete(ActionMapping mapping, WorldsMaintForm form, HttpServletRequest request, HttpServletResponse response) throws WebException{
        //do nothing at this time
    }
    private void processSelect(ActionMapping mapping, WorldsMaintForm form, HttpServletRequest request, HttpServletResponse response) throws WebException{
        String schema = form.getSelectedItemId();
        request.getSession().setAttribute(Constants.ACTIVE_DB_SCHEMA,schema);
        logger.debug("Setting schema name to " + schema + " for current user");
        /*UserTrackHelper.setSelectedSchema(schema);
        try {
            List result = DBAccess.execute("select count(*) from person");
            for (int i = 0; i < result.size(); i++) {
               logger.debug("RESULT="+result.get(i));

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }*/
    }
    private void processView(ActionMapping mapping, WorldsMaintForm form, HttpServletRequest request, HttpServletResponse response) throws WebException {
        form.getWorlds().clear();
        WorldLI li;
        /*li = new WorldLI();
        li.setSchemaName("TEST_U"); li.setSchemaDesc("TEST_U schema"); li.setCreatedDate("01/01/2006");
        worlds.add(li);
        li = new WorldLI();
        li.setSchemaName("TEST_U_SIM"); li.setSchemaDesc("TEST_U_SIM schema"); li.setCreatedDate("03/01/2006");
        worlds.add(li);*/
        String owner = DBAccess.getSchemaOwner();
        String activeSchema= UserTrackHelper.getSelectedSchema();
        List schemas = DBAccess.getSchemas();
        for (int i = 0; i < schemas.size(); i++) {
            String s = (String)schemas.get(i);

            li = new WorldLI();li.setSchemaName(s); li.setSchemaDesc(s);
            if(s.equals(owner))
                li.setIsSchemaOwner(true);
            else
                li.setIsSchemaOwner(false);

            if(s.equals(activeSchema))
                li.setIsActive(true);
            else
                li.setIsActive(false);
            form.getWorlds().add(li);
        }
    }
}
