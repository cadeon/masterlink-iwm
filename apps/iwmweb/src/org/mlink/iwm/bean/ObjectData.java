package org.mlink.iwm.bean;

/**
 * User: andrei
 * Date: Oct 30, 2006
 */
public class ObjectData extends DataCommon{
    private String objectId;
    private String dataDefId;
    private String custom;

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getDataDefId() {
        return dataDefId;
    }

    public void setDataDefId(String dataDefId) {
        this.dataDefId = dataDefId;
    }

    public String getCustom() {
        return custom;
    }

    public void setCustom(String custom) {
        this.custom = custom;
    }
}
