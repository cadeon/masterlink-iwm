package org.mlink.iwm.iwml;

import org.apache.commons.digester.Digester;

import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: andreipovodyrev
 * Date: Jun 4, 2007
 */
public class   IWMLDigester extends Parser{


    protected Digester prepare(){
        Digester digester = new Digester();
        digester.setValidating(true);
        digester.setSchema("iwml.xsd");
        digester.addObjectCreate("iwml", ArrayList.class);
        digester.addObjectCreate("iwml/worker", Worker.class);
        digester.addBeanPropertySetter("iwml/worker/billingRate", "billingRate");
        digester.addBeanPropertySetter("iwml/worker/fname", "fname");
        digester.addBeanPropertySetter("iwml/worker/lname", "lname");
        digester.addBeanPropertySetter("iwml/worker/type", "type");
        digester.addBeanPropertySetter("iwml/worker/organization", "organization");
        digester.addBeanPropertySetter("iwml/worker/email", "email");

        digester.addObjectCreate("iwml/worker/skills", ArrayList.class);
        digester.addObjectCreate("iwml/worker/skills/skill", Skill.class);
        digester.addSetProperties("iwml/worker/skills/skill", "type", "type");
        digester.addSetProperties("iwml/worker/skills/skill", "level", "level");
        digester.addSetNext("iwml/worker/skills/skill", "add");
        digester.addSetNext("iwml/worker/skills", "setSkills");
        digester.addSetNext("iwml/worker", "add");
        return digester;
    }


}
