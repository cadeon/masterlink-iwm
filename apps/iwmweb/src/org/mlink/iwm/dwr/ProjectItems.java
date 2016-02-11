package org.mlink.iwm.dwr;

import org.mlink.iwm.bean.ResponsePage;
import org.mlink.iwm.bean.ProjectItem;
import org.mlink.iwm.bean.ProjectMatrix;

import java.util.HashMap;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: andreipovodyrev
 * Date: Aug 12, 2007
 */
public class ProjectItems implements ReturnCodes{

    public ResponsePage getData(HashMap criteria) throws Exception {
        List <ProjectItem> lst =  getProject().getDisplayItems();
        return new ResponsePage(lst.size(),lst);
    }

    public void addIndent(int refNumber) throws Exception{
        getProject().addIndent(refNumber);
    }

    public void removeIndent(int refNumber) throws Exception{
        getProject().removeIndent(refNumber);
    }

   public void updateSequence(int refNumber, int sequenceNumber) throws Exception {
        getProject().moveItem(sequenceNumber-1,refNumber);
    }
    public void deleteItem(int refNumber) throws Exception {
        getProject().removeItem(refNumber);
    }

    /**
     * Expand/collapse parent item (will set display flag for all children)
     * @param refNumber
     * @throws Exception
     */
    public void expand(int refNumber) throws Exception {
        //ProjectItem parent = getProject().getItem(refNumber);
        //parent.setExpand(!parent.getExpand());
        getProject().expand(refNumber);

    }

    private ProjectMatrix getProject(){
        ProjectMatrix pm = (ProjectMatrix) SessionUtil.getAttribute("projectItems");
        if(pm==null) {
            pm = new ProjectMatrix();
            SessionUtil.setAttribute("projectItems",pm);
        }
        return pm;
    }



    /**
     * Here we save the changes
     */
    public void apply(){
        SessionUtil.removeAttribute("projectItems");
    }

    public void cancel(){
        SessionUtil.removeAttribute("projectItems");
    }

}
