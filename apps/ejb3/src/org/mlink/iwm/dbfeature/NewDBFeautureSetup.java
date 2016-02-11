package org.mlink.iwm.dbfeature;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * Created by IntelliJ IDEA.
 * User: andrei
 * Subclasses of NewDBFeautureSetup implement database updates for new application features that require syncing with database
 */
public abstract class NewDBFeautureSetup {
    private static List features = new ArrayList();
    private static final Logger logger = Logger.getLogger(NewDBFeautureSetup.class);


    // register features
    static{
//        addFeature(new AddTaskTypeUrgent());
        /*addFeature(new AddContactToLocator());
        addFeature(new MailPropsSetup());
        addFeature(new AddParentToOrganization());
        addFeature(new AddTimeToJobTask());
        addFeature(new CreateOrgHierachyFunction());
        addFeature(new AddIsEditInField());
        addFeature(new AddProjectStatus());
        addFeature(new AddProjectStencils());
        addFeature(new AddJobScheduleHistory());
        addFeature(new AddActiveToJobSchedule());
        addFeature(new AddColstoJobAction());
        addFeature(new AddColstoJobTask());
        addFeature(new AddUpdatesToRoles());
        addFeature(new AddTitleToPerson());
        addFeature(new AddLocationToLocator());
        addFeature(new InitLocatorLocation());
        addFeature(new CreateClassHierarchyFunc());
        addFeature(new AddOrgToObject());
        addFeature(new AddIsProductionToWorld());
        addFeature(new AddAttachment());
        addFeature(new InitJobSticky());
        addFeature(new InitJSCreatedTime());
        addFeature(new AddParentToProject());
        addFeature(new AddTimeToJob());
        addFeature(new AddWorkerStatus());
        addFeature(new AddRefIdToPerson());
        addFeature(new AddCQLS());
        addFeature(new UpdateAGT_PRECEDES_SUMView());
        addFeature(new AddRefIdToJob());
        addFeature(new AddBSCToPerson());
        addFeature(new AddMobileWorkerAccessTrace());
        addFeature(new AddMobileWorkerAccessView());
        addFeature(new ReplaceAA_WORKSCHEDULE_LOCNAME_JS());
        addFeature(new AddUCSeqs());
        addFeature(new AddUser_NameToPerson());
        addFeature(new AddUserIdToPerson());
        addFeature(new AddObjectDefSeq());
        addFeature(new AddIdToSystemProps());
        addFeature(new AddDatesToSkillType());
        addFeature(new AddSeqToSkillType());
        addFeature(new AddExpiryToTask());
        //addFeature(new AddLocToMobileWorkerAccess());
        addFeature(new AddLocInfoToOrg());
        addFeature(new SchemaTopPropsSetup());
        addFeature(new AddExpiryToTask());
        //addFeature(new ReplaceTaskView());
        addFeature(new AddSeqToShiftRef());
        addFeature(new AddTenantRequestUniqueView());
        addFeature(new AddDatesToShiftRef());
        addFeature(new AddShiftAndBreak());
        addFeature(new AddShiftTrace());
        addFeature(new AddObjectDefInventory());
        addFeature(new AddParentObjectIdToObject());*/
        addFeature(new AddJobStatusHistory());
        addFeature(new AddReportingViews());
    }

    private static void addFeature(NewDBFeautureSetup feature){
        features.add(feature);
    }

    /**
     * Check if future already installed
     * @return true if installed
     */
    public abstract boolean isInstalled();

    /**
     * Add functionality (JDBC /Hibernate /etc) to update db with the new feature
     */
    public abstract void install();

    public void execute(){
        logger.debug(getClass().getCanonicalName() + " will install if has not been ...");        
        if(isInstalled()){
            logger.debug(getClass().getCanonicalName() +
                    " already installed");
        }else{
            install();
        }
    }

    public static void installFeatures(){
        for (int i = 0; i < features.size(); i++) {
            NewDBFeautureSetup newDBFeautureSetup = (NewDBFeautureSetup) features.get(i);
            newDBFeautureSetup.execute();
        }
    }

}
