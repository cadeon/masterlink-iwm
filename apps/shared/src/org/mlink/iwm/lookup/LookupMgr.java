package org.mlink.iwm.lookup;

import org.apache.log4j.Logger;
import java.util.*;


public class LookupMgr {
    private static final Logger logger = Logger.getLogger(LookupMgr.class);
    private static List <CodeLookupValues> lookups = new ArrayList<CodeLookupValues>();

    static{   // init objects here
        addCDLV(new TaskTypeRef());
        addCDLV(new PriorityRef());
        addCDLV(new SkillTypeRef());
        addCDLV(new SkillLevelRef());
        addCDLV(new RoleRef());
		addCDLV(new WorkerTypeRef());
        addCDLV(new TaskFrequencyRef());
        addCDLV(new DataTypeRef());
        addCDLV(new UOMRef());
        addCDLV(new SecurityTypeRef());
        addCDLV(new OrganizationTypeRef());

        addCDLV(new DummyDropdown());

        addCDLV(new JobStatusRef());
        addCDLV(new ProjectTypeRef());
        addCDLV(new ActiveStatusRef());
        addCDLV(new OrganizationRef());
        addCDLV(new ShiftRef());
        addCDLV(new ObjectTypeRef());
        addCDLV(new LocatorSchemaRef());
        addCDLV(new ObjectClassSchemaRef());
        addCDLV(new ScheduleResponsibilityRef());
        addCDLV(new PersonRef());
        addCDLV(new WorkScheduleStatusRef());
        addCDLV(new ExternalWorkRequestProblemRef());
        addCDLV(new LocatorRef());
        addCDLV(new TargetClassRef());
        addCDLV(new OrganizationSchemaRef());
        addCDLV(new ProjectStatusRef());
        addCDLV(new ProjectRef());
        addCDLV(new WorkerStatusRef());
        addCDLV(new WorkerCertificationRef());
        addCDLV(new WorkerQualificationRef());
        addCDLV(new WorkerLicenseRef());
        addCDLV(new CQLSRef());
        addCDLV(new ObjectClassificationRef());
        addCDLV(new MWAccessTypeRef());
        addCDLV(new TaskExpiryTypeRef());
        //readLookupTables();
    }
    /**
     *
     * @param lookup
     */
    private static void addCDLV(CodeLookupValues lookup){
        lookups.add(lookup);
    }

    public static List getCDLVs() {
        return lookups;
    }

    public static CodeLookupValues getInstance(Class clazz){
        for (CodeLookupValues clv : lookups) {
            if (clv.getClass().equals(clazz)) {
                //if (!clv.isInited) reloadCDLV(clv);
                return clv;
            }
        }
        logger.debug("not registered " + clazz);
        return null;
    }
    /**
     * Loads all  CodeLookupValues objects registered with LookupMgr
     */
    public static void readLookupTables(){
        List list = getCDLVs();
        for (Object aList : list) {
            CodeLookupValues o = (CodeLookupValues) aList;
            logger.debug("loading lookup object " + o.getClass());
            reloadCDLV(o);
        }
        logger.debug("lookup tables loaded ");
    }

    /**
     * Reloads a single CodeLookupValues instance
     * @param cdlvClass class name of a CodeLookupValues child instance
     */
    public static void reloadCDLV(Class  cdlvClass){
        CodeLookupValues cdlv  = getInstance(cdlvClass);
        reloadCDLV(cdlv);
    }

    /**
     * Reloads a single CodeLookupValues instance
     * @param cdlv   CodeLookupValues
     */
    private static void reloadCDLV(CodeLookupValues cdlv){
        cdlv.reset();
        cdlv.init();
        try{
            Collection results = CDLVLoader.load(cdlv);
            for (Object result : results) {
                Map map = (Map) result;
                cdlv.addOption(map);
            }

            //cdlv.isInited=true;
            logger.debug(cdlv.toString());

        }catch(Throwable e){
            e.printStackTrace();
            logger.error("could not load lookup values for " + cdlv.getClass() + " Exception: " + e.getMessage());
            logger.error("sql select statement failed to execute " + cdlv.getSql());
        }
    }
}
