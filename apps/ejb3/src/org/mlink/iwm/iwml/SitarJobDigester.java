package org.mlink.iwm.iwml;

import org.apache.commons.digester.Digester;

import java.util.ArrayList;

/**
 * User: andreipovodyrev
 * Date: Nov 22, 2007
 */
public class SitarJobDigester extends Parser{

    protected Digester prepare(){
        Digester digester = new Digester();
        digester.setValidating(true);
        digester.setSchema("sitar-job.xsd");
        digester.addObjectCreate("sitar-jobs", ArrayList.class);
        digester.addObjectCreate("sitar-jobs/job", SitarJob.class);
        digester.addBeanPropertySetter("sitar-jobs/job/description", "description");
        digester.addBeanPropertySetter("sitar-jobs/job/jcn", "jcn");
        digester.addBeanPropertySetter("sitar-jobs/job/comment", "comment");
        digester.addBeanPropertySetter("sitar-jobs/job/locator", "locator");
        digester.addBeanPropertySetter("sitar-jobs/job/organization", "organization");
        digester.addBeanPropertySetter("sitar-jobs/job/number-of-workers", "numberOfWorkers");
        digester.addBeanPropertySetter("sitar-jobs/job/work-unit-code", "workUnitCode");
        digester.addBeanPropertySetter("sitar-jobs/job/estimated-time", "estimatedTime");
        digester.addBeanPropertySetter("sitar-jobs/job/priority", "priority");
        digester.addBeanPropertySetter("sitar-jobs/job/type", "type");
        //digester.addSetProperties("sitar-jobs/job/skill","type", "skillType");
        //digester.addSetProperties("sitar-jobs/job/skill","level", "skillLevel");
        digester.addSetNext("sitar-jobs/job", "add");
        return digester;
    }
}
