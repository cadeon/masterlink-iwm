package org.mlink.iwm.dwr;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.entity.StandardEntityCollection;
import org.jfree.chart.servlet.ServletUtilities;
import org.jfree.data.category.IntervalCategoryDataset;
import org.jfree.data.gantt.Task;
import org.jfree.data.gantt.TaskSeries;
import org.jfree.data.gantt.TaskSeriesCollection;
import org.jfree.data.time.SimpleTimePeriod;
import org.mlink.iwm.access3.ServiceLocator;
import org.mlink.iwm.bean.GanttTask;
import org.mlink.iwm.bean.ProjectJob;
import org.mlink.iwm.bean.ResponsePage;
import org.mlink.iwm.dao.DaoFactory;
import org.mlink.iwm.dao.JobsCriteria;
import org.mlink.iwm.dao.PaginationRequest;
import org.mlink.iwm.dao.PaginationResponse;
import org.mlink.iwm.entity3.Project;
import org.mlink.iwm.session.ControlSF;
import org.mlink.iwm.session.ImplementationSF;
import org.mlink.iwm.util.ConvertUtils;
import org.mlink.iwm.util.EqualsUtils;

/**
 * User: andrei
 * Date: Feb 2, 2007
 */
public class ProjectJobs extends Jobs {
    private static final Logger logger = Logger.getLogger(ProjectJobs.class);

    public ResponsePage getData(HashMap criteria, int offset, int pageSize, String orderBy, String orderDirection) throws Exception {
        JobsCriteria cr = new JobsCriteria(criteria);
        SessionUtil.setAttribute("JobsCriteria",cr);    //store in session, reuse in later when coming back
        PaginationRequest request = new PaginationRequest(offset,pageSize,orderBy,orderDirection);
        PaginationResponse response = DaoFactory.process(DaoFactory.NAME.JobsDAO,cr,request);
        List lst =  response.convertRowsToClasses(org.mlink.iwm.bean.ProjectJob.class);
        return new ResponsePage(response.getTotalCount(),lst);
    }

    public ResponsePage getOrderedJobs(Long projectId) throws Exception {
        JobsCriteria cr = new JobsCriteria(); cr.setProjectId(projectId);
        PaginationRequest request = new PaginationRequest("sequenceLevel");
        PaginationResponse response = DaoFactory.process(DaoFactory.NAME.JobsDAO,cr,request);
        List<ProjectJob> lst =  response.convertRowsToClasses(org.mlink.iwm.bean.ProjectJob.class);
        SessionUtil.setAttribute("jobsForOrdering",lst);
        return new ResponsePage(response.getTotalCount(),lst);
    }

    /**
     * reorder job sequence based on new sequence number for given job
     * @param jobId
     * @param  sequenceNumber
     * @return
     * @throws Exception
     */
    public ResponsePage updateJobSequence(String jobId, int sequenceNumber) throws Exception {
        List <ProjectJob> lst = (List <ProjectJob>) SessionUtil.getAttribute("jobsForOrdering");
        for (ProjectJob job : lst) {
            if(EqualsUtils.areEqual(job.getId(),jobId)){
                job.setSequenceLevel(sequenceNumber);
            }
        }
        Collections.sort(lst);
        return new ResponsePage(lst.size(),lst);
    }

    public String saveOrderedJobs() throws Exception {
    	ImplementationSF isf = ServiceLocator.getImplementationSFLocal();
        List <ProjectJob> lst = (List <ProjectJob>) SessionUtil.getAttribute("jobsForOrdering");
        //Collection<org.mlink.iwm.entity3.Job> vos = CopyUtils.copyProperties(org.mlink.iwm.entity3.Job.class,lst);
        org.mlink.iwm.entity3.Job job;
        List<org.mlink.iwm.entity3.Job> jobs = new ArrayList<org.mlink.iwm.entity3.Job>();
        for(ProjectJob projJob : lst){
        	job = isf.get(org.mlink.iwm.entity3.Job.class, Long.parseLong(projJob.getId()));
        	job.setSequenceLevel(projJob.getSequenceLevel());
        	//job.setProject(project);
        	jobs.add(job);
        }
        isf.updateJobSequences(jobs);
        SessionUtil.removeAttribute("jobsForOrdering");
        return ITEM_SAVED_OK_MSG;
    }


    public String generateGanttChart(Long projectId) throws Exception{
        String filename;
        ControlSF csf = ServiceLocator.getControlSFLocal( );
        Project vo = csf.get(Project.class, projectId);
        IntervalCategoryDataset dataset = createDataset(projectId);
        final JFreeChart chart = ChartFactory.createGanttChart(
                vo.getName(),  // chart title
                "Task",              // domain axis label
                "Date",              // range axis label
                dataset,             // data
                true,                // include legend
                true,                // tooltips
                false                // urls
        );
        ChartRenderingInfo info = new ChartRenderingInfo(new StandardEntityCollection());
        int rows = dataset.getColumnCount();
        filename = ServletUtilities.saveChartAsPNG(chart, 780, 100+rows*20, info, null);
        return filename;
    }

    public static IntervalCategoryDataset createDataset(Long projectId) throws Exception {
        final TaskSeries s1 = new TaskSeries("Earliest Start Date");
        final TaskSeries s2 = new TaskSeries("Started Date");
        JobsCriteria cr = new JobsCriteria();
        cr.setProjectId(projectId);
        final PaginationRequest request = new PaginationRequest("sequenceLevel");
        final PaginationResponse response = DaoFactory.process(DaoFactory.NAME.JobsDAO,cr,request);
        List <GanttTask> lst =  response.convertRowsToClasses(org.mlink.iwm.bean.GanttTask.class);
        for (int i = 0; i < lst.size(); i++) {
            GanttTask projectJob =  lst.get(i);
            if(projectJob.getEarliestStart()==null) {
                logger.warn("Earliest Date is null for" + projectJob.getDescription());
            }else{
                s1.add(new Task(projectJob.getDescription() + " " + String.valueOf(i+1), new SimpleTimePeriod(projectJob.getEarliestStart().getTime(), projectJob.getEarliestStart().getTime() + 60*1000*ConvertUtils.intVal(projectJob.getEstTime()))));
            }

            if(projectJob.getStartedDate()==null) {
                logger.warn("Started Date is null for" + projectJob.getDescription());
            }else{
                s2.add(new Task(projectJob.getDescription() + " " + String.valueOf(i+1), new SimpleTimePeriod(projectJob.getStartedDate().getTime(), projectJob.getStartedDate().getTime() + 60*1000*ConvertUtils.intVal(projectJob.getTotalTime()))));
            }
        }
        final TaskSeriesCollection collection = new TaskSeriesCollection();
        collection.add(s1);
        collection.add(s2);
        return collection;
    }

}


