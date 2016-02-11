package org.mlink.iwm.bean;

import org.mlink.iwm.struts.form.BaseForm;
import org.mlink.iwm.lookup.CQLSRef;
import org.mlink.iwm.util.StringUtils;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by IntelliJ IDEA.
 * User: andreipovodyrev
 * Date: Nov 17, 2007
 */
public class CQLS extends BaseForm implements Comparable{
    private java.lang.String id;
    private java.lang.String personId;
    private java.lang.String expirationDate;
    private java.lang.String  cqlsRefId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getCqlsRefId() {
        return cqlsRefId;
    }

    public void setCqlsRefId(String cqlsRefId) {
        this.cqlsRefId = cqlsRefId;
    }

    public String getCode() {
        return CQLSRef.getLabel(StringUtils.getInteger(getCqlsRefId()));
    }

    public String getType() {
        return CQLSRef.getType(StringUtils.getInteger(getCqlsRefId()));
    }

    public int compareTo(Object o) {
        return getCode().compareTo(((CQLS)o).getCode());
    }

    public void reset(ActionMapping mapping, HttpServletRequest request) {
        super.reset(mapping, request);
        id = null;
        personId = null;
        expirationDate = null;
        cqlsRefId = null;
    }
}
