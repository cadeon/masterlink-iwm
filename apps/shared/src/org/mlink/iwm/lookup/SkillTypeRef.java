package org.mlink.iwm.lookup;

import org.apache.log4j.Logger;

import java.util.Map;
import java.util.Collection;

public class SkillTypeRef extends CodeLookupValues{
    private static final Logger logger = Logger.getLogger(SkillTypeRef.class);

    public static final String MUST_ASSIGN = "Assign_Skill";

    public String getSql() {
        return "SELECT * from  SKILL_TYPE_REF WHERE ARCHIVED_DATE IS NULL ORDER BY DISP_ORD, CODE";
    }

    public void addOption(Map map){
        Object value = map.get("ID");
        Object label = map.get("DESCRIPTION");
        Object code = map.get("CODE");
        if(value == null || label == null )
            logger.error(getSql() + " returned invalid data " + map);
        else
            addOption(new OptionItem(value.toString(),label.toString(), code.toString()));
    }

    public static String getCode(Integer id){
    	SkillTypeRef me = (SkillTypeRef) LookupMgr.getInstance(SkillTypeRef.class);
        return  me.getCode(id==null?null:String.valueOf(id));
    }


    public static String getLabel(Integer id){
        SkillTypeRef me = (SkillTypeRef) LookupMgr.getInstance(SkillTypeRef.class);
        return  me.getLabel(id==null?null:String.valueOf(id));
    }

    public Collection getDefault() {
        return null;
    }

    public static Collection getSkills(){
        SkillTypeRef me = (SkillTypeRef) LookupMgr.getInstance(SkillTypeRef.class);
        return  me.getOptions();
    }

    public static int getIdByCode(String code){
        SkillTypeRef me = (SkillTypeRef) LookupMgr.getInstance(SkillTypeRef.class);
        return  me.getValueByCode(code).intValue();
    }

   public static int getIdByLabel(String label){
        SkillTypeRef me = (SkillTypeRef) LookupMgr.getInstance(SkillTypeRef.class);
        return  me.getValueByLabel(label).intValue();
    }

}
