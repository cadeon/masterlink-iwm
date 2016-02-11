package org.mlink.iwm.iwml;

import org.mlink.iwm.access3.ServiceLocator;
import org.mlink.iwm.entity3.Job;
import org.mlink.iwm.entity3.Locator;
import org.mlink.iwm.entity3.ObjectEntity;
import org.mlink.iwm.entity3.Organization;
import org.mlink.iwm.lookup.JobStatusRef;
import org.mlink.iwm.lookup.PriorityRef;
import org.mlink.iwm.lookup.SkillLevelRef;
import org.mlink.iwm.lookup.SkillTypeRef;
import org.mlink.iwm.lookup.TaskTypeRef;
import org.mlink.iwm.session.ControlSF;
import org.mlink.iwm.session.ImplementationSF;
import org.mlink.iwm.session.PolicySF;
import org.mlink.iwm.util.ConvertUtils;

/**
 * User: andreipovodyrev
 * Date: Nov 22, 2007
 */
public class SitarJobUpdator extends Updator{
    private SitarJob jobS;

    public SitarJobUpdator(SitarJob job) {
        this.jobS = job;
    }

    public void store() throws Exception{
        log("processing job " + jobS.getJcn());
        PolicySF policy = ServiceLocator.getPolicySFLocal();
        Organization organization = null;//policy.getOrganization(job.getOrganization());
        if(organization==null){
            throw new Exception ("Not valid organization " + jobS.getOrganization());
        }

        Locator locator = policy.get(Locator.class, Long.parseLong(jobS.getLocator()));
        if(locator==null){
            throw new Exception ("Not valid locator " + jobS.getLocator());
        }

        ImplementationSF impl = ServiceLocator.getImplementationSFLocal();
        ObjectEntity object = impl.getObjectByReference("VP30.Generic");

        if(object==null){
            throw new Exception ("VP30.Generic does not exist");
        }

        //Integer skillTypeId;
        String defSkillType = jobS.getSkillType();
        Integer skillTypeId = SkillTypeRef.getIdByCode(defSkillType);
        if(skillTypeId<0){
            throw new Exception("Not valid skillType " + jobS.getSkillType());
        }


        int skillLevelId = SkillLevelRef.getIdByLabel(jobS.getSkillLevel());
        if(skillLevelId<0){
            throw new Exception ("Not valid skillLevel " + jobS.getSkillLevel());
        }

        int priorityId = PriorityRef.getIdByLabel(jobS.getPriority());
        if(priorityId<0){
            throw new Exception ("Not valid priority " + jobS.getPriority());
        }

        int jobTypeId  = TaskTypeRef.getIdByLabel((jobS.getType()));
        if(priorityId<0){
            throw new Exception ("Not valid job type " + jobS.getType());
        }

        Job job = new Job();
        //job.setDescription(job.getDescription());
        job.setDescription(jobS.getWorkUnitCode()!=null?jobS.getWorkUnitCode().toString():null);
        job.setEstTime(jobS.getEstimatedTime());
        job.setNumberOfWorkers(jobS.getNumberOfWorkers());
        job.setLocatorId(locator.getId());
        job.setOrganizationId(organization.getId());
        job.setObjectId(object.getId());
        job.setSkillTypeId(skillTypeId);
        job.setSkillLevelId(skillLevelId);
        job.setPriorityId(priorityId);
        job.setJobTypeId(jobTypeId);
        job.setRefId(jobS.getJcn());
        job.setNote(jobS.getComment());


        job.setStatusId(JobStatusRef.getIdByCode(JobStatusRef.Status.INS));

        //job.setJobTypeId(TaskTypeRef.getIdByCode(TaskTypeRef.ROUTINE_MAINT));

        ControlSF control = ServiceLocator.getControlSFLocal();
        Long newId= control.create(job);

        log("created job " + newId);
        log(ConvertUtils.toString("\t", job));

    }

}
