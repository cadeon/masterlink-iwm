package org.mlink.iwm.base;

import org.mlink.iwm.exception.IncompleteStateException;

import java.util.List;
import java.util.Collection;
import java.util.ArrayList;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: andrei
 * Date: Jun 8, 2004
 * Time: 10:28:20 PM
 * To change this template use Options | File Templates.
 */
public abstract class BaseListDAO implements Serializable{
    public abstract String getSql();
    protected List parameters;

    public abstract void setParameters(List criteria) throws IncompleteStateException;

    public abstract List prepareResult(ResultSet rs)
            throws SQLException;

    public List getParameters(){
        return parameters;
    }
}
