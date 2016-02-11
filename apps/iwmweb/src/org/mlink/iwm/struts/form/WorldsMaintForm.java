package org.mlink.iwm.struts.form;

import org.mlink.iwm.util.AutoGrowingList;
import org.apache.struts.action.ActionMapping;

import javax.servlet.ServletRequest;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Andrei
 * Date: Mar 5, 2006
 */
public class WorldsMaintForm extends BaseForm {
    protected java.util.List worlds = new AutoGrowingList(WorldLI.class);
    protected java.lang.String selectedItemId;


    public List getWorlds() {
        return worlds;
    }

    public void setWorlds(List worlds) {
        this.worlds = worlds;
    }

    public void reset(ActionMapping actionMapping, ServletRequest servletRequest) {
        super.reset(actionMapping, servletRequest);
        worlds = new AutoGrowingList(WorldLI.class);
    }

    public String getSelectedItemId() {
        return selectedItemId;
    }

    public void setSelectedItemId(String selectedItemId) {
        this.selectedItemId = selectedItemId;
    }
}
