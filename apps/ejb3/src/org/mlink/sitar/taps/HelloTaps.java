package org.mlink.sitar.taps;

import java.util.Calendar;

/**
 * Created by IntelliJ IDEA.
 * User: andreipovodyrev
 * Date: Sep 26, 2007
 *  WS code is generated
  jboss-4.2.1.GA/bin andreipovodyrev$ ./wsconsume.sh -k http://www.taps-vss.com:8080/axis/services/SitarAgent?wsdl -p org.mlink.sitar.taps
 */
public class HelloTaps {

    public void test(){
        try {
            SitarAgentService service = new SitarAgentService();
            SitarAgentPort port = service.getSitarAgent();
            WorkerType worker = new WorkerType();
            worker.setDNEC("dnec string");
            worker.setPNEC("pnec string");
            worker.setRate(50.0f);
            worker.setRating("rating string");
            worker.setReadiness(1.0f);
            worker.setSkillgroup("skill group string");
            worker.setSkilllevel("skill level string");
            worker.setUserId("userId string");

            WorkCenterType center  = new WorkCenterType();
            center.setName(" workcenter name");
            center.setReadiness(1.0f);
            center.getWorker().add(worker);

            DivisionType division = new DivisionType();
            division.setName(" workdivision name");
            division.setReadiness(1.0f);
            division.getWorkCenter().add(center);

            DepartmentType department = new DepartmentType();
            department.setName(" workdivision name");
            department.setReadiness(1.0f);
            department.getDivision().add(division);

            UnitType unit = new UnitType();
            unit.setName(" workdivision name");
            unit.setReadiness(1.0f);
            unit.getDepartment().add(department);

            SitarType sitar = new  SitarType();
            sitar.setGeneratedDate(Calendar.getInstance().getTimeInMillis());
            sitar.setName("test feed");
            sitar.getUnit().add(unit);

            sitar.getUnit().add(new UnitType());
            DataAgentResponse r = port.publishData(sitar);
            System.out.println(r);
            System.out.println("Result:"+ r.getResult());
            System.out.println("Message:"+r.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
