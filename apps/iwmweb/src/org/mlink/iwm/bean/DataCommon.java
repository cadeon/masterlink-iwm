package org.mlink.iwm.bean;

import org.mlink.iwm.util.StringUtils;
import org.mlink.iwm.util.CopyUtils;

import java.util.Map;

/**
 * User: andrei
 * Date: Oct 21, 2006
 */
public class DataCommon {
    private java.lang.String dataTypeId;
    private java.lang.String dataLabel;
    private java.lang.String dataValue;
    private java.lang.String uomId;
    private java.lang.String custom;
    private java.lang.String isDisplay;
    private String id;
    private String isEditInField;

    public String getIsEditInField() {
        return isEditInField;
    }

    public void setIsEditInField(String editInField) {
        isEditInField = editInField;
    }

    public String getDataTypeId() {
        return dataTypeId;
    }

    public void setDataTypeId(String dataTypeId) {
        this.dataTypeId = dataTypeId;
    }

    public String getDataLabel() {
        return dataLabel;
    }

    public void setDataLabel(String dataLabel) {
        this.dataLabel = dataLabel;
    }

    public String getDataValue() {
        return dataValue;
    }

    public void setDataValue(String dataValue) {
        this.dataValue = dataValue;
    }

    public String getUomId() {
        return uomId;
    }

    public void setUomId(String uomId) {
        this.uomId = uomId;
    }
    public String getCustom() {
        return custom;
    }

    public void setCustom(String custom) {
        this.custom = custom;
    }

    public String getIsDisplay() {
        return isDisplay;
    }

    public void setIsDisplay(String display) {
        isDisplay = display;
    }
    public String getDataType(){
        return org.mlink.iwm.lookup.DataTypeRef.getLabel(StringUtils.getInteger(dataTypeId));
    }
    public String getUom(){
        return org.mlink.iwm.lookup.UOMRef.getLabel(StringUtils.getInteger(uomId));
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
