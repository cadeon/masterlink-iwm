package org.mlink.iwm.lookup;

import org.mlink.iwm.exception.IWMException;
import org.mlink.iwm.util.UserTrackHelper;
import org.apache.log4j.Logger;

import java.util.*;
import java.io.Serializable;


public abstract class CodeLookupValues implements Serializable {
    private Map <String,WorldOptions> worlds = new HashMap<String,WorldOptions>();
    private static final Logger logger = Logger.getLogger(CodeLookupValues.class);


    public List <OptionItem> getOptions() {
        load();
        return getCurrentWorld().getOptions();
    }

    protected void load(){
        //reload if empty
        WorldOptions wo = getCurrentWorld();
        if(!wo.isLoaded){
            wo.isLoaded=true;
            LookupMgr.reloadCDLV(getClass());
        }
    }

    public void init(){}

    public void addOption(OptionItem item){
        getCurrentWorld().addOption(item);
    }

    protected void setLoaded(){
        getCurrentWorld().isLoaded=true;    
    }

    public String getName(){
        String name = this.getClass().getName();
        int start = name.lastIndexOf(".");
        if(start > 0)
            return name.substring(start + 1);
        else
            return name;
    }

    public void reset(){
        getCurrentWorld().reset();
    }

    public String toString(){
        List list = getOptions();
        StringBuffer sb = new StringBuffer(this.getName() + " contains:\n");
        for (Object aList : list) {
            OptionItem o = (OptionItem) aList;
            sb.append(o.toString()).append("\n");
        }
        return sb.toString();
    }
    public abstract String getSql();
    public abstract void addOption(Map map);

    public String getLabel(String value){
        load();
        if(value==null) return "";

        Long lValue;
        try{
            lValue = Long.valueOf(value);
        }catch(NumberFormatException e){
            return "";
        }
        for (OptionItem optionItem : getOptions()) {
            if (lValue.equals(optionItem.getValue()))
                return optionItem.getLabel();
        }
        return "";
    }


    public OptionItem getOptionItem(String value){
        load();
        Long lValue;
        try{
            lValue = Long.valueOf(value);
        }catch(NumberFormatException e){
            return null;
        }
        for (OptionItem optionItem : getOptions()) {
            if (lValue.equals(optionItem.getValue()))
                return optionItem;
        }
        return null;
    }

    public OptionItem getItemByCode(String code){
        load();
        for (OptionItem optionItem : getOptions()) {
            if (optionItem.getCode().equalsIgnoreCase(code))
                return optionItem;
        }
        return null;
    }

    public Long getValueByLabel(String label){
        if(label==null) label="";
        load();
        for (OptionItem option : getOptions()) {
            if (option.getLabel().equals(label))
                return option.getValue();
        }
        return -1L;
    }

    public Long getValueByCode(String code){
        if(code==null) code="";
        load();
        for (OptionItem option : getOptions()) {
            if (code.equals(option.getCode()))
                return option.getValue();
        }
        logger.warn("entry not found for code = " + code);
        return -1L;
    }

    public String getCode(String value) {
        load();
        if (value==null) return "";

        Long lvalue;
        try {
            lvalue = Long.valueOf(value);
        }
        catch (NumberFormatException nfe) {
            return "n/a";
        }

        for (OptionItem optionItem : getOptions()) {
            if (optionItem.getValue().equals(lvalue))
                return optionItem.getCode();
        }

        logger.warn("entry not found for value = " + value);
        return "n/a";
    }

    public Collection getDefault() {
        return null;
    }

    //public static boolean isUptodate(){
    //    return true;
    //}


    private WorldOptions getCurrentWorld(){
        String schema = UserTrackHelper.getSelectedSchema();
        if(schema==null) throw new IWMException("No current database schema available!");
        WorldOptions wo = worlds.get(schema);
        if(wo==null){//i.e first time calling options for this world
            wo = new WorldOptions(schema) ;
            worlds.put(schema,wo);
        }
        return wo;
    }
}

