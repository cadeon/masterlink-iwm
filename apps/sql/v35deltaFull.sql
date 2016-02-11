-- SQL delta between IWM v3.0 and 3.5---
-- Prepared by Irina by running diff on schemas as well as developers feedback--
-- IMPORTANT! Should be applied to db schema BEFORE the application, pointing to it, starts --

CREATE TABLE PROJECT_STATUS_REF
( ID NUMBER NOT NULL ENABLE,
DESCRIPTION VARCHAR2(50) DEFAULT '-' NOT NULL ENABLE,
DISP_ORD NUMBER (2,0),
CODE VARCHAR2(20) NOT NULL ENABLE,
CONSTRAINT PK_PROJECTSTATUSREF_ID PRIMARY KEY (ID) ENABLE )
;


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


ALTER TABLE JOB_ACTION  ADD MODIFIER VARCHAR2(150);
ALTER TABLE JOB_ACTION  ADD VERB VARCHAR2(150);
ALTER TABLE JOB_ACTION  ADD NAME VARCHAR2(150);
ALTER TABLE JOB_ACTION  ADD SEQUENCE NUMBER;


ALTER TABLE JOB_SCHEDULE  ADD DELETED_TIME DATE;

alter table job_schedule_hist rename column eff_date to action_date;
--alter table JOB_SCHEDULE_HIST add (ACTION_DATE DATE);

ALTER TABLE JOB_TASK  ADD TOTAL_TIME NUMBER;
ALTER TABLE JOB_TASK ADD SKILL_TYPE_ID NUMBER;
ALTER TABLE JOB_TASK ADD PRIORITY_ID NUMBER;
ALTER TABLE JOB_TASK  ADD TASK_TYPE_ID NUMBER;
ALTER TABLE JOB_TASK  ADD NUMBER_WORKERS NUMBER;
ALTER TABLE JOB_TASK  ADD ESTIMATED_TIME NUMBER;
ALTER TABLE JOB_TASK  ADD SKILL_LEVEL_ID NUMBER;
ALTER TABLE JOB_TASK ADD  DESCRIPTION VARCHAR2(150);



alter table LOCATOR add (TOP_PARENT_ID NUMBER);

alter table OBJECT add (ORGANIZATION_ID NUMBER);
ALTER TABLE OBJECT_DATA ADD IS_EDIT_IN_FIELD NUMBER(1);
ALTER TABLE OBJECT_DATA_DEF ADD IS_EDIT_IN_FIELD NUMBER(1);

alter table ORGANIZATION add (HIERARCHY VARCHAR2(150));
alter table ORGANIZATION add (PARENT_ID NUMBER);
alter table ORGANIZATION add (SCHEMA_ID NUMBER);

alter table PERSON add (TITLE VARCHAR2(150));

alter table PROJECT add (SEQUENCE_ID NUMBER);
alter table PROJECT add (STARTED_DATE DATE);
alter table PROJECT add (STATUS_ID NUMBER);
ALTER TABLE PROJECT  ADD DESCRIPTION VARCHAR2(200);




alter table SEQUENCE add (ARCHIVED_DATE DATE);
alter table SEQUENCE add (FREQUENCY_ID NUMBER);
alter table SEQUENCE add (FREQUENCY_VALUE NUMBER);
alter table SEQUENCE add (LAST_PLANNED_DATE DATE);
alter table SEQUENCE add (LOCATOR_ID NUMBER);
alter table SEQUENCE add (ORGANIZATION_ID NUMBER);
alter table SEQUENCE add (PROJECT_TYPE_ID NUMBER);
alter table SEQUENCE add (START_DATE DATE);

ALTER TABLE SEQUENCE  ADD ACTIVE NUMBER(1);
ALTER TABLE SEQUENCE  ADD AUTOPLANNING NUMBER(1);
ALTER TABLE SEQUENCE  ADD NAME VARCHAR2(50);
ALTER TABLE SEQUENCE  MODIFY DESCRIPTION VARCHAR2(250);

alter table WORLD add (IS_PRODUCTION NUMBER);

ALTER TABLE JOB_SCHEDULE  drop column WLS_TEMP;



CREATE SEQUENCE SYSTEM_LOG_SEQ MINVALUE 1 MAXVALUE
9999999999999999999999 INCREMENT BY 1 START WITH 1000 CACHE 20
NOORDER NOCYCLE ;




drop sequence WORLD_SEQ;

CREATE SEQUENCE WORLD_SEQ MINVALUE 1000 MAXVALUE
9999999999999999999999 INCREMENT BY 1 START WITH 1000 CACHE 20
NOORDER NOCYCLE ;



create or replace FUNCTION GET_CLASS_HIERARCHY
(classId IN NUMBER)
RETURN VARCHAR2
IS
	  hierarchy VARCHAR2(200);
          separator VARCHAR(1);
	  CURSOR c1 IS SELECT OC.CODE FROM OBJECT_CLASSIFICATION OC START WITH OC.ID=classId CONNECT BY PRIOR OC.PARENT_ID=OC.ID;
BEGIN
          separator:='';
	  FOR myrecord IN c1
	  LOOP     hierarchy := myrecord.CODE || separator || hierarchy;
          separator:='.';
	  END LOOP;
	  RETURN hierarchy;
END GET_CLASS_HIERARCHY;
/





CREATE OR REPLACE FUNCTION GET_ORGANIZATION_HIERARCHY
(orgId IN NUMBER)  RETURN VARCHAR2
IS
hierarchy VARCHAR2 (200);
separator VARCHAR (1);
CURSOR c1 IS SELECT P.NAME FROM
ORGANIZATION O,PARTY P WHERE
P.ID=O.PARTY_ID START WITH
O.ID=orgId CONNECT BY PRIOR
O.PARENT_ID=O.ID;
BEGIN
FOR myrecord IN c1
LOOP hierarchy := myrecord.NAME || separator || hierarchy;
separator:='|';
END LOOP;
RETURN hierarchy;
END;
/



CREATE OR REPLACE FUNCTION GET_LOCATOR_LOCATION
( locatorId IN NUMBER) RETURN NUMBER
IS
top_parent_id NUMBER;
CURSOR c1 IS select id
from locator start with id = locatorId
connect by prior parent_id = ID;
--
BEGIN
FOR myrecord IN
c1
LOOP top_parent_id := myrecord.ID;
END LOOP;
RETURN top_parent_id;
END
GET_LOCATOR_LOCATION;
/




create or replace procedure INIT_LOCATOR_LOCATION_PROC
as
CURSOR c1 IS
select id, get_locator_location(ID) location from locator;
BEGIN
FOR myrecord IN c1
LOOP
update locator
set top_parent_id = myrecord.location where id=myrecord.id;
END LOOP;
END INIT_LOCATOR_LOCATION_PROC;
/

alter trigger LOCATOR_FIXUP disable;
alter trigger S_LOCATOR_FIXUP disable;
alter trigger UPD_LOC_WKSCHED_ARCHDATE disable;

call INIT_LOCATOR_LOCATION_PROC();



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





   CREATE OR REPLACE FORCE VIEW AGT_PRECEDES_SUM
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
order by project_id, sequence_level, id;
/



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



INSERT INTO SCHEMA_REF
( ID, DESCRIPTION, SCHEMA_TYPE, CODE, PARENT, DISP_ORD )
 VALUES
(20,'Department','O', 'Department',NULL, 20);


INSERT INTO SCHEMA_REF
( ID, DESCRIPTION, SCHEMA_TYPE, CODE, PARENT,  DISP_ORD )
VALUES
(21,'Division','O', 'Division',20, 22);

INSERT INTO SCHEMA_REF
( ID, DESCRIPTION, SCHEMA_TYPE, CODE, PARENT,  DISP_ORD )
 VALUES
(22,'Group','O', 'Group',21, 24);

INSERT INTO SCHEMA_REF
( ID, DESCRIPTION, SCHEMA_TYPE, CODE, PARENT,DISP_ORD )
VALUES
 (23,'Team','O', 'Team',22, 26);

commit;


UPDATE ORGANIZATION SET SCHEMA_ID=20;
commit;


UPDATE JOB_TASK JT
SET
TOTAL_TIME = (SELECT  SUM(JTT.TIME)
FROM
JOB_TASK_TIME JTT
WHERE
JTT.JOB_TASK_ID=JT.ID
GROUP BY JTT.JOB_TASK_ID);


commit;


INSERT INTO PROJECT_STATUS_REF
( ID, DESCRIPTION, CODE, DISP_ORD )
VALUES
(1,'Preparing','Preparing',10);


INSERT INTO PROJECT_STATUS_REF
( ID, DESCRIPTION, CODE, DISP_ORD )
VALUES
(2,'Started','Started',20);


INSERT INTO PROJECT_STATUS_REF
( ID, DESCRIPTION, CODE, DISP_ORD )
VALUES
(3,'Cancelled','Cancelled',30);


INSERT INTO PROJECT_STATUS_REF
( ID, DESCRIPTION, CODE, DISP_ORD )
VALUES
(4,'Completed','Completed',40);

commit;


UPDATE SEQUENCE  SET ACTIVE=1;

UPDATE SEQUENCE  SET AUTOPLANNING=1;

COMMIT;



UPDATE SCHEDULE_RESPONSIBILITY_REF SET disp_ord=1 WHERE code = 'System';
UPDATE SCHEDULE_RESPONSIBILITY_REF SET disp_ord=2 WHERE code = 'Manual';

COMMIT;

UPDATE JOB_ACTION JA
SET (NAME,VERB,MODIFIER,SEQUENCE) = (
   SELECT A.NAME,A.VERB,A.MODIFIER,A.SEQUENCE
   FROM ACTION A
   WHERE A.ID = JA.ACTION_ID);

COMMIT;

UPDATE JOB_TASK JA SET
(SKILL_TYPE_ID,PRIORITY_ID,TASK_TYPE_ID,NUMBER_WORKERS,ESTIMATED_TIME,SKILL_LEVEL_ID,DESCRIPTION) =
(SELECT
A.SKILL_TYPE_ID,A.PRIORITY_ID,A.TASK_TYPE_ID,A.NUMBER_WORKERS,A.ESTIMATED_TIME,A.SKILL_LEVEL_ID,A.DESCRIPTION
FROM TASK A  WHERE A.ID = JA.TASK_ID);
COMMIT;




alter table PROJECT add constraint FK_PROJECT_STATUSID
foreign key (STATUS_ID ) references PROJECT_STATUS_REF (ID) on delete cascade;


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

ALTER TABLE PROJECT  ADD PARENT_ID NUMBER;
ALTER TABLE SEQUENCE  ADD PARENT_ID NUMBER;
ALTER TABLE PROJECT  ADD ORGANIZATION_ID NUMBER;

ALTER TABLE JOB ADD TOTAL_TIME NUMBER;
UPDATE JOB J SET TOTAL_TIME = (SELECT  SUM(JT.TOTAL_TIME) FROM JOB_TASK JT WHERE JT.JOB_ID=J.ID GROUP BY JT.JOB_ID);

ALTER TABLE PERSON  ADD STATUS_ID NUMBER(2,0);

CREATE TABLE WORKER_STATUS_REF
( ID NUMBER NOT NULL ENABLE,
DESCRIPTION VARCHAR2(50) DEFAULT '-' NOT NULL ENABLE,
DISP_ORD NUMBER (2,0),
CODE VARCHAR2(20) NOT NULL ENABLE,
CONSTRAINT PK_WORKERSTATUSREF_ID PRIMARY KEY (ID) ENABLE );

alter table PERSON add constraint FK_PERSON_STATUSID foreign  key (STATUS_ID ) references WORKER_STATUS_REF (ID) ENABLE     ;

INSERT INTO WORKER_STATUS_REF( ID, DESCRIPTION, CODE, DISP_ORD )VALUES (1,'Available','Available',10);
INSERT INTO WORKER_STATUS_REF( ID, DESCRIPTION, CODE, DISP_ORD )VALUES (2,'Not Available','Not_Available',20);

//for sitar
ALTER TABLE PERSON  ADD REF_ID VARCHAR2(150 BYTE)

CREATE TABLE CQLS_REF
( ID NUMBER NOT NULL ENABLE,
DESCRIPTION VARCHAR2(50) DEFAULT '-' NOT NULL ENABLE,
DISP_ORD NUMBER (2,0),
CODE VARCHAR2(50) NOT NULL ENABLE,
TYPE VARCHAR2(20) NOT NULL ENABLE,
CONSTRAINT PK_CQLSREF_ID PRIMARY KEY (ID) ENABLE );

INSERT INTO CQLS_REF( ID, DESCRIPTION, CODE, DISP_ORD,TYPE )VALUES (1,'CDI','CDI',10,'QUALIFICATION');
INSERT INTO CQLS_REF( ID, DESCRIPTION, CODE, DISP_ORD,TYPE )VALUES (105,'MMG-1/A Motor Generator','MMG-1/A Motor Generator',15,'LICENSE');

CREATE TABLE CQLS
( ID NUMBER NOT NULL ENABLE,
PERSON_ID NUMBER,
CQLS_REF_ID NUMBER,
EXP_DATE DATE,
CONSTRAINT PK_CQLS_ID PRIMARY KEY (ID) ENABLE );

ALTER TABLE CQLS add CONSTRAINT FK_CQLS_CQLSREFID FOREIGN KEY (CQLS_REF_ID ) references CQLS_REF (ID) ENABLE     ;
ALTER TABLE CQLS add CONSTRAINT FK_CQLS_PERSON_ID FOREIGN KEY (PERSON_ID ) references PERSON (ID) ENABLE     ;

create sequence CQLS_SEQ start with 1000;

ALTER TABLE JOB  ADD REF_ID VARCHAR2(150 BYTE);
ALTER TABLE PERSON  ADD BSCODE VARCHAR2(50 BYTE);





