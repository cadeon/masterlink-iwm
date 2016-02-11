/**
 * User: andrei
 * Date: Feb 14, 2007
 */
package org.mlink.iwm.timer;

import java.util.HashMap;
import java.util.Map;

import org.mlink.iwm.util.Config;

public class IWMTimer {
    private static IWMTimer ourInstance ;
    private Map <Class,IWMTask> tasks = new HashMap<Class,IWMTask>();

    public static synchronized  IWMTimer getInstance() {
        if(ourInstance==null) ourInstance = new IWMTimer();
        return ourInstance;
    }

    private IWMTimer() {}


    public  synchronized  void start() {
        schedule(new CleanProjects());
        if("true".equals(Config.getProperty(Config.JAVA_AGENTS_ENABLED,"false"))){
            long interval = Long.parseLong(Config.getProperty(Config.JAVA_AGENTS_INTERVAL,"0"));
            schedule(new RunAgents(interval*1000L*60));
        }
        if ("true".equals(Config.getProperty(Config.CREATE_WORK_SCHEDULES_ENABLED,"false"))){
        schedule(new CreateWorkSchedules());
        }
    }

    public synchronized void stop() {
        for (Map.Entry<Class, IWMTask> task : tasks.entrySet()) {
            task.getValue().stop();
        }
    }

    public synchronized void schedule(IWMTask task){
        IWMTask t = tasks.get(task.getClass());
        if(t!=null) t.stop();
        tasks.put(task.getClass(),task);
        task.start();
    }

    public IWMTask getTask(Class clazz){
        return tasks.get(clazz);
    }
}
