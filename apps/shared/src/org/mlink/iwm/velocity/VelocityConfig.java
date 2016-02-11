package org.mlink.iwm.velocity;

import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.Template;
import org.apache.log4j.Logger;

import java.util.Map;
import java.util.HashMap;
import java.io.FileNotFoundException;

/**
 * Created by Andrei Povodyrev
 * Date: Jun 17, 2006
 */
public class VelocityConfig {
    private static VelocityEngine engine = null;
    private static final Logger logger = Logger.getLogger(VelocityConfig.class);
    private static Map cachedTemplates = new HashMap();

    private static VelocityEngine getTemplateEngine() throws Exception{
        if(engine==null){
            engine = new VelocityEngine();
            //defaul loader is FileLoader which is useless for us since we deploy app as ear and do not plan for central location of templates
            // configure notification's Classpath loader
            engine.setProperty("resource.loader","class");
            engine.setProperty("class.resource.loader.class","org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");

            //override notification's default logging (notification.log file) and assign to the current logger.
            // see notification docs for references
            engine.setProperty(RuntimeConstants.RUNTIME_LOG_LOGSYSTEM_CLASS,"org.apache.velocity.runtime.log.SimpleLog4JLogSystem");
            engine.init();
            engine.setProperty("runtime.log.logsystem.log4j.category", logger.getName());
        }
        return engine;
    }

    /**
     *
     * @param templateName   ex: org/mlink/iwm/notification/filename.vm
     * @return Template
     */
    public static synchronized Template getTemplate(String templateName) throws FileNotFoundException {
        if(cachedTemplates.get(templateName)==null)
            try {
                Template tpl = getTemplateEngine().getTemplate(templateName);
                cachedTemplates.put(templateName, tpl);
                return tpl;
            } catch (Exception e) {
                throw new FileNotFoundException(templateName);
            }else{
            return (Template)cachedTemplates.get(templateName);
        }
    }


}
