-- SQL delta Short between IWM v3.0 and 3.5---
-- A short version of v35deltaFull.sql without statements executed by application on startup--


CREATE TABLE SCHEMA_INFO
( VERSION NUMBER (10,0) )
;


CREATE TABLE SYSTEM_LOG
( ID NUMBER,
SHORT_DESC VARCHAR2 (100),
LONG_DESC VARCHAR2 (2000),
LOG_TYPE VARCHAR2 (20),
CREATED_DATE DATE,
CONSTRAINT CHECK_SYSTEM_LOG_TYPE CHECK (LOG_TYPE IN ('EMAIL',
'ALERT', 'INFO')) ENABLE,
CONSTRAINT PK_SYSTEM_LOG PRIMARY KEY (ID) ENABLE )
;

alter table job_schedule_hist rename column eff_date to action_date;

ALTER TABLE SEQUENCE  MODIFY DESCRIPTION VARCHAR2(250);

ALTER TABLE JOB_SCHEDULE  drop column WLS_TEMP;

CREATE SEQUENCE SYSTEM_LOG_SEQ MINVALUE 1 MAXVALUE
9999999999999999999999 INCREMENT BY 1 START WITH 1000 CACHE 20
NOORDER NOCYCLE ;


drop sequence WORLD_SEQ;

CREATE SEQUENCE WORLD_SEQ MINVALUE 1000 MAXVALUE
9999999999999999999999 INCREMENT BY 1 START WITH 1000 CACHE 20
NOORDER NOCYCLE ;




CREATE OR REPLACE TRIGGER UPD_JOB_SCHEDULE
AFTER UPDATE
on JOB_SCHEDULE
REFERENCING NEW AS NEW OLD AS OLD
FOR EACH ROW
BEGIN
insert into job_schedule_hist
(WORK_SCHEDULE_ID, ID,JOB_ID,  CREATED_TIME, action_date, ACTION,  USR)
values
(:NEW.WORK_SCHEDULE_ID, :NEW.ID, :NEW.JOB_ID, :NEW.CREATED_TIME,  SYSDATE,'U',  :NEW.USR)  ;
END;
/
ALTER TRIGGER UPD_JOB_SCHEDULE ENABLE
;



create or replace TRIGGER DLT_JOB_SCHEDULE
   AFTER DELETE
      on JOB_SCHEDULE
REFERENCING NEW AS NEW OLD AS OLD
   FOR EACH ROW
BEGIN
insert into job_schedule_hist
(WORK_SCHEDULE_ID,ID,JOB_ID, CREATED_TIME, ACTION_DATE, ACTION,  USR)
values
(:OLD.WORK_SCHEDULE_ID, :OLD.ID, :OLD.JOB_ID, :OLD.CREATED_TIME,SYSDATE, 'D',  :OLD.USR)
;
END;
/




create or replace TRIGGER INS_JOB_SCHEDULE
  AFTER INSERT
     on JOB_SCHEDULE
 REFERENCING NEW AS NEW OLD AS OLD
  FOR EACH ROW
BEGIN
insert into job_schedule_hist
 (WORK_SCHEDULE_ID, ID,JOB_ID, CREATED_TIME, ACTION_DATE, ACTION,  USR)
values
(:NEW.WORK_SCHEDULE_ID, :NEW.ID, :NEW.JOB_ID, :NEW.CREATED_TIME, SYSDATE, 'I',  :NEW.USR)
;
 END;
/

ALTER TRIGGER INS_JOB_SCHEDULE ENABLE
;


/**   CREATE OR REPLACE FORCE VIEW AGT_PRECEDES_SUM
(ID,PROJECT_ID, INCOMPLETE, COMPLETE)
AS
   select id,
            project_id,
            (select count(*)
            from job j2
            where j2.project_id = j1.project_id
            and j2.sequence_level < j1.sequence_level
            and j2.status_id not  in (
            select id from job_status_ref where code in ('CIA','DUN','EJO')
            ))                                 as incomplete,
            (select count(*)
            from job j2
            where j2.project_id = j1.project_id
            and j2.sequence_level < j1.sequence_level
            and j2.status_id  in (
            select id from job_status_ref where code in ('CIA','DUN','EJO')
            ))                                  as complete
   from job j1
order by project_id, sequence_level, id
see UpdateAGT_PRECEDES_SUMView class **/




   CREATE OR REPLACE FORCE VIEW TASKVIEW
(OBJECT_ID,LOCATOR_ID, PRIORITY, THRESHOLD, TASKTYPE, DESCRIPTION,
LASTSERVICED, LASTPLANNED, TARGET, ESTIMATEDTIME, RUNHOURS,
SKILLTYPE, SKILLLEVEL, PLAN, SCHEDULEGROUP, GROUPDESCRIPTION, ORGANIZATION_ID,
METERRULE, ACTIVE, NUMBERWORKERS, SEQUENCEGROUP, SEQUENCELEVEL, MONTHS, DAYS)
AS
   select
               ts.id as object_id,
               tr.locator_id as locator_id   ,
               pr.id as priority,
               decode(nvl(ts.run_hours_threshold_increment,0),
               0,0,ts.run_hours_threshold) as threshold,
               ts.task_type_id as tasktype,
               ts.description,
               ts.last_serviced_date as lastserviced,
               ts.last_planned_date as lastplanned,
               ts.object_id as target,
               ts.estimated_time as estimatedtime,
               decode(nvl(ts.run_hours_threshold_increment,0),
               0,0, tr.run_hours) as runhours,
               ts.skill_type_id as skilltype,
               ts.skill_level_id  as skilllevel,
               remove_commas(ts.plan) as plan,
               ts.group_id as schedulegroup,
               sg.description as groupdescription,
               og.id as organization_id,
               ts.meter_rule as meterrule,
               ts.active,
               ts.number_workers as numberworkers,
               seq.sequence_id as sequencegroup,
               seq.sequence_level as sequencelevel,
               decode(tf.code,null,ts.freq_months,tf.months) as months,
               decode(tf.code,null,ts.freq_days,tf.days) as days
from
               task ts,
               object tr,
               organization og,
               task_group sg,
               priority_ref pr,
               task_frequency_ref tf,
               task_sequence seq,
               schedule_responsibility_ref srr
where
               nvl(ts.last_planned_date,today-1) < today
        and    nvl(ts.active,1) = 1
        and    nvl(tr.active,1) = 1
        and    srr.code = 'System'
        and    nvl(tr.start_date,today) <= today
        and    ts.start_date <= today
        and    ts.object_id = tr.id
        and    ts.group_id = sg.id(+)
        and    ts.priority_id = pr.id
        and    ts.frequency_id = tf.id(+)
        and    ts.id = seq.task_id(+)
        and    ts.organization_id = og.id(+)
        and    ts.schedule_responsibility_id = srr.id
order by    schedulegroup;





UPDATE SCHEDULE_RESPONSIBILITY_REF SET disp_ord=1 WHERE code = 'System';
UPDATE SCHEDULE_RESPONSIBILITY_REF SET disp_ord=2 WHERE code = 'Manual';

COMMIT;




alter table sequence add constraint FK_SEQUENCE_ORGANIZATIONID
foreign key  (ORGANIZATION_ID )
references ORGANIZATION (ID) ENABLE     ;

alter table SEQUENCE add constraint FK_SEQUENCE_PROJECTTYPEID
foreign key (PROJECT_TYPE_ID )
references PROJECT_TYPE_REF (ID) ENABLE     ;



alter table JOB_TASK add constraint FK_JOBTASK_PRIORITYID
foreign  key (PRIORITY_ID )
references PRIORITY_REF (ID) ENABLE     ;



alter table JOB_TASK add constraint FK_JOBTASK_SKILLEVELID
foreign  key (SKILL_LEVEL_ID )
references SKILL_LEVEL_REF (ID) ENABLE     ;

alter table JOB_TASK add constraint FK_JOBTASK_SKILLTYPEID
foreign  key (SKILL_TYPE_ID )
references SKILL_TYPE_REF (ID) ENABLE     ;

alter table JOB_TASK add constraint FK_JOBTASK_TASKTYPEID
foreign  key (TASK_TYPE_ID )
references TASK_TYPE_REF (ID) ENABLE     ;

create index ACTION_ACTDEFID_INDX  on ACTION  (ACTION_DEF_ID);
create index ACTIONDEF_TASKDEFID_INDX  on ACTION_DEF  (TASK_DEF_ID);
create index JOB_JOBTYPID_INDX  on JOB  (JOB_TYPE_ID);

create index JOB_PRIORITYID_INDX  on JOB  (PRIORITY_ID);
create index JOB_SCHEDRESPID_INDX  on JOB  (SCHEDULE_RESPONSIBILITY_ID);
create index JOB_SKILLEVID_INDX  on JOB  (SKILL_LEVEL_ID);
create index JOB_SKILLTYPID_INDX  on JOB  (SKILL_TYPE_ID);
create index JOB_STATUSID_INDX  on JOB  (STATUS_ID);



create index LOCATOR_SCHEMAID_INDX  on LOCATOR  (SCHEMA_ID);
create index LOCATORDATA_DTATPEID_INDX  on LOCATOR_DATA  (DATA_TYPE_ID);
create index LOCATORDATA_UOMID_INDX  on LOCATOR_DATA  (UOM_ID);
create index OBJECT_OBJECTDEFID_INDX  on OBJECT  (OBJECT_DEF_ID);
create index OBJECT_OBJECTTPEID_INDX  on OBJECT  (OBJECT_TYPE_ID);
create index OBJECTCLASS_SCHEMAID_INDX  on OBJECT_CLASSIFICATION  (SCHEMA_ID);
create index OBJECTDTA_DTATPEID_INDX  on OBJECT_DATA  (DATA_TYPE_ID);
create index OBJECTDTA_OBJDTADEFID_INDX  on OBJECT_DATA  (OBJECT_DATA_DEF_ID);
create index OBJECT_DATA_UOM_ID_INDX  on OBJECT_DATA  (UOM_ID);
create index OBJECTDTADEF_DTATPEID_INDX  on OBJECT_DATA_DEF  (DATA_TYPE_ID);
create index OBJECTDTADEF_OBJDEFID_INDX  on OBJECT_DATA_DEF  (OBJECT_DEF_ID);
create index OBJECTDTADEF_UOMID_INDX  on OBJECT_DATA_DEF  (UOM_ID);
create index OBJECTDEF_CLASSID_INDX  on OBJECT_DEF  (CLASS_ID);
create index ORGANIZATION_ORGTPEID_INDX  on ORGANIZATION  (ORGANIZATION_TYPE_ID);

create index PERSON_SECURITYLEVID_INDX  on PERSON  (SECURITY_LEVEL_ID);
create index PERSON_WRKRTPEID_INDX  on PERSON  (WORKER_TYPE_ID);
create index PROJECT_PROJTPEID_INDX  on PROJECT  (PROJECT_TYPE_ID);
create index SKILL_SKILLEVID_INDX  on SKILL  (SKILL_LEVEL_ID);
create index SKILL_SKILLTPEID_INDX  on SKILL  (SKILL_TYPE_ID);
create index TASK_FREQID_INDX  on TASK  (FREQUENCY_ID);
create index TASK_GROUPID_INDX  on TASK  (GROUP_ID);

create index TASK_SCHEDRESPID_INDX  on TASK  (SCHEDULE_RESPONSIBILITY_ID);
create index TASK_SKILLEVID_INDX  on TASK  (SKILL_LEVEL_ID);
create index TASK_SKILLTPEID_INDX  on TASK  (SKILL_TYPE_ID);
create index TASK_TSKDEFID_INDX  on TASK  (TASK_DEF_ID);

create index TASK_TASKTPEID_INDX  on TASK  (TASK_TYPE_ID);
create index TASK_WRKRTPEID_INDX  on TASK  (WORKER_TYPE_ID);
create index TASKDEF_FREQID_INDX  on TASK_DEF  (FREQUENCY_ID);
create index TASKDEF_OBJDEF_ID_INDX  on TASK_DEF  (OBJECT_DEF_ID);
create index TASKDEF_PRIORITYID_INDX  on TASK_DEF  (PRIORITY_ID);
create index TASKDEF_SECURITYLEVID_INDX  on TASK_DEF  (SECURITY_LEVEL_ID);
create index TASKDEF_SKILLEVID_INDX  on TASK_DEF  (SKILL_LEVEL_ID);
create index TASKDEF_SKILLTPEID_INDX  on TASK_DEF  (SKILL_TYPE_ID);
create index TASKDEF_TASKTPEID_INDX  on TASK_DEF  (TASK_TYPE_ID);
create index TASKDEF_WRKRTPEID_INDX  on TASK_DEF  (WORKER_TYPE_ID);
create index TASKGROUP_SKILLTPEID_INDX  on TASK_GROUP  (SKILL_TYPE_ID);
create index TASKGROUP_TASKGRPDEFID_INDX  on TASK_GROUP  (TASK_GROUP_DEF_ID);
create index TASKGROUPDEF_OBJDEFID_INDX  on TASK_GROUP_DEF  (OBJECT_DEF_ID);
create index TASKGROUPDEF_SKILLTPEID_INDX  on TASK_GROUP_DEF  (SKILL_TYPE_ID);
create index TASKSEQUENCE_SEQID_INDX  on TASK_SEQUENCE  (SEQUENCE_ID);

UPDATE JOB_TASK JT
SET
TOTAL_TIME = (SELECT  SUM(JTT.TIME)
FROM
JOB_TASK_TIME JTT
WHERE
JTT.JOB_TASK_ID=JT.ID
GROUP BY JTT.JOB_TASK_ID);



