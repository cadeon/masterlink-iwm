package org.mlink.iwm.lookup;

import org.apache.log4j.Logger;

import java.util.Map;
import java.util.Collection;
import java.util.ArrayList;
import java.util.HashMap;

import org.mlink.iwm.lookup.CodeLookupValues;

public class PersonRef extends CodeLookupValues{
    private static final Logger logger = Logger.getLogger(PersonRef.class);


    public String getSql() {
        return "SELECT PERSON.ID,PARTY.NAME, PERSON.PARTY_ID, PERSON.USERNAME FROM PERSON, PARTY WHERE PERSON.PARTY_ID=PARTY.ID";
    }

    public void addOption(Map map){
        Object value = map.get("ID");
        Object label = map.get("NAME");
        Object partyId = map.get("PARTY_ID");
        Object username = map.get("USERNAME");
        if(value == null || label == null )
            logger.error(getSql() + " returned invalid data " + map);
        else{
            OptionItem option = new OptionItem(value.toString(),label.toString());
            option.addProperty("partyId",partyId==null?"":partyId.toString());
            option.addProperty("username",username==null?"":username.toString());
            addOption(option);
        }
    }

    public static String getLabel(Integer id){
        PersonRef me = (PersonRef) LookupMgr.getInstance(PersonRef.class);
        return  me.getLabel(id==null?null:String.valueOf(id));
    }
    public static String getLabel(Long id){
        PersonRef me = (PersonRef) LookupMgr.getInstance(PersonRef.class);
        return  me.getLabel(id==null?null:String.valueOf(id));
    }

    public static String getPartyId(Integer id){
        PersonRef me = (PersonRef) LookupMgr.getInstance(PersonRef.class);
        OptionItem option = me.getOptionItem(String.valueOf(id));
        return (String)option.getProperty("partyId");
    }
    public static String getUsername(Integer id){
        PersonRef me = (PersonRef) LookupMgr.getInstance(PersonRef.class);
        OptionItem option = me.getOptionItem(String.valueOf(id));
        return (String)option.getProperty("username");
    }




}

