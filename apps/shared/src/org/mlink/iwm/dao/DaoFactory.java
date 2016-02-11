package org.mlink.iwm.dao;

import org.mlink.iwm.exception.IWMException;

import java.util.Map;
import java.util.HashMap;
import java.util.Collections;
import java.sql.SQLException;

/**
 * User: andrei
 * Date: Nov 14, 2006
 */
public class DaoFactory {
    public enum NAME {ObjectsDAO,   JobTaskActionsDAO,  JobTasksDAO,    LocatorDataDAO,     JobSchedulesDAO,
                      JobsDAO,      ObjectClassDAO,     ObjectDataDAO,  ObjectTaskActionsDAO, ObjectTaskGroupsDAO,
                      LocatorsDAO,  ObjectTasksDAO,     OrganizationsDAO,TemplateDataDAO,     TemplateTaskActionsDAO,
                      TemplatesDAO, WorkersDAO,         TemplateTasksDAO, TemplateTaskGroupsDAO, TimeSpecsDAO,
                      JobHistoryDAO,TenantRequestsDAO,  UserRolesDAO,     JobTaskTimesDAO,  ProjectStencilsDAO,
                      WorkScheduleJobsDAO,  TemplatesForObjectCreateDAO, ObjectsForSelectDAO, ProjectsDAO,
                      ProjectStencilTasksDAO,   ProjectStencilHistoryDAO, GlobalSearchDAO, AttachmentDAO,
                      ScheduledJobsDAO, SystemPropsDAO, SkillTypesDAO, MWLocationsDAO, ShiftRefsDAO, ShiftTimingReportDAO}

    public static enum Database{ORACLE,SQLSERVER}
    public static Database currentDatabaseType=Database.ORACLE;     //default

    private Map <String,ListDAOTemplate>  cache = Collections.synchronizedMap(new HashMap<String,ListDAOTemplate>());


    //this class is a Singleton
    private static DaoFactory ourInstance = new DaoFactory();
    public static DaoFactory getInstance() {return ourInstance;}
    private DaoFactory() {}


    public static PaginationResponse process(NAME daoEnum,SearchCriteria cr,PaginationRequest request) throws IWMException{
        PaginationResponse response;
        DaoFactory me = DaoFactory.getInstance();
        ListDAOTemplate dao = me._get(daoEnum);
        try {
            response = dao.getData(cr, request);
        } catch (SQLException e) {
            throw new IWMException(e);
        }
        return response;
    }

    public static DAOResponse process(NAME daoEnum,SearchCriteria cr) throws IWMException{
        DAOResponse response;
        DaoFactory me = DaoFactory.getInstance();
        ListDAOTemplate dao = me._get(daoEnum);
        try {
            response = dao.getData(cr);
        } catch (SQLException e) {
            throw new IWMException(e);
        }
        return response;
    }


    private ListDAOTemplate _get(NAME daoEnum) {
        ListDAOTemplate rtn;
        String className;
        switch(currentDatabaseType){
            case SQLSERVER:
                className = "org.mlink.iwm.dao.sqlserver."+daoEnum.toString();
                break;
            case ORACLE:
            default:
                className = "org.mlink.iwm.dao."+daoEnum.toString();
        }
        if(cache.get(className)==null){
            try{
                rtn = (ListDAOTemplate)Class.forName(className).newInstance();
                cache.put(className,rtn);
            } catch (Exception e) {
                throw new IWMException(e);
            }
        }else{
            rtn = cache.get(className);
        }
        return rtn;
    }

}
