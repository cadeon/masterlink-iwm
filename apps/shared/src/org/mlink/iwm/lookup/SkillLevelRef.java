package org.mlink.iwm.lookup;

import org.apache.log4j.Logger;

import java.util.Map;
import java.util.Collection;

public class SkillLevelRef extends CodeLookupValues{
    private static final Logger logger = Logger.getLogger(SkillLevelRef.class);

    public static final String ONE = "1";
    public static final int ZERO = 0;

    public String getSql() {
        return "SELECT * from  SKILL_LEVEL_REF ORDER BY DISP_ORD";
    }

    public void addOption(Map map){
        Object value = map.get("ID");
        Object code = map.get("VALUE");
        Object label = map.get("DESCRIPTION");
        if(value == null || label == null || code ==null)
            logger.error(getSql() + " returned invalid data " + map);
        else{
            OptionItem item = new OptionItem(value.toString(),label.toString(), code.toString());
            addOption(item);
        }
    }

    public static String getLabel(Integer id){
        SkillLevelRef me = (SkillLevelRef) LookupMgr.getInstance(SkillLevelRef.class);
        return  me.getLabel(id==null?null:String.valueOf(id));
    }


    /**
     * In SkillLevelRef code property represents the skill level value!
     * @param id
     * @return skill value
     */

    public static String getCode(Integer id){
        SkillLevelRef me = (SkillLevelRef) LookupMgr.getInstance(SkillLevelRef.class);
        return  me.getCode(id==null?null:String.valueOf(id));
    }

    public static Integer getSkillLevelValue(Integer id){
        String skillValue = getCode(id);
        return  new Integer(skillValue);
    }

    public static int getIdByCode(String code){
        SkillLevelRef me = (SkillLevelRef) LookupMgr.getInstance(SkillLevelRef.class);
        return  me.getValueByCode(code).intValue();
    }

   public static int getIdByLabel(String label){
       SkillLevelRef me = (SkillLevelRef) LookupMgr.getInstance(SkillLevelRef.class);
        return  me.getValueByLabel(label).intValue();
    }

    
    public Collection getDefault() {
        return null;
    }

}
