package org.mlink.iwm.lookup;

import org.apache.log4j.Logger;

import java.util.*;


public class DummyDropdown extends CodeLookupValues{
    private static final Logger logger = Logger.getLogger(DummyDropdown.class);



    public String getSql() {
        return "SELECT * from ...";
    }

    public DummyDropdown() {
        setLoaded();        
    }

    public DummyDropdown(Collection options) {
        for (Object option : options) {
            OptionItem o = (OptionItem) option;
            addOption(o);
        }
        setLoaded();
    }

    public void addOption(Map map){
        Object value = map.get("id");
        Object label = map.get("value");
        if(value == null || label == null )
            logger.error(getSql() + " returned invalid data " + map);
        else
            addOption(new OptionItem(value.toString(),label.toString()));
    }

    public static String getLabel(Integer id){
        DummyDropdown me = (DummyDropdown) LookupMgr.getInstance(DummyDropdown.class);
        return  me.getLabel(id==null?null:String.valueOf(id));
    }

    public Collection getDefault() {
        //Collection col = new ArrayList();
        /*Map map = new HashMap();
        map.put("id", "1");
        map.put("value", "dynamic data");
        col.add(map);
        map = new HashMap();
        map.put("id", "2");
        map.put("value", "dynamic data");
        col.add(map);
        map = new HashMap();
        map.put("id", "3");
        map.put("value", "dynamic data");*/
        return getOptions();
    }

}
