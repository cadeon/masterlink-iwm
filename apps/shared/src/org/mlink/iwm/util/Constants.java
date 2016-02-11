package org.mlink.iwm.util;

/**
 * Created by IntelliJ IDEA.
 * User: andrei
 * Date: Jul 30, 2004
 * Time: 1:20:33 PM
 * To change this template use Options | File Templates.
 */
public interface Constants {
    public static final Integer INITED = 3;
    public static final Integer NOT_INITED = 0;

    public static final Integer STATUS_ACTIVE = 1;
    public static final Integer STATUS_NOT_ACTIVE = 0;

    public static final Integer CUSTOMIZED_YES = 1;
    public static final Integer CUSTOMIZED_NO = 0;

    public static final Integer SEQUENCE_TYPE_TASK = 1;
    public static final Integer SEQUENCE_TYPE_JOB = 2;

    public static final Integer YES = 1;
    public static final Integer NO = 0;
    public static final Integer ACTIVE_JOBS_CATEGORY = 10;
    public static final Integer TERMINAL_JOBS_CATEGORY = 11;
    public static final Integer PENDING_JOBS_CATEGORY = 12;
    

    public static final Long emptyOptionId = -1L;
    public static final String emptyOptionValue = "-------      Select      -------";
    public static final String GLOBAL_SCOPE_LOCATOR_ID = "CurrentLocatorId";
    public static final String PAGE_SCOPE_LOCATOR_ID = "PageScopeLocatorId";
    public static final String GLOBAL_SCOPE_CLASS_ID = "CurrentClassId";
    public static final String ACTIVE_DB_SCHEMA = "ACTIVE_DB_SCHEMA";

    public static final String USER_EXCEPTION   = "user_exception";
    public static final String USER_EXCEPTION_REF   = "user_exception_ref";   
}