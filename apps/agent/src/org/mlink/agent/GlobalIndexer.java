
package org.mlink.agent;

//import org.mlink.agent.dao.AgentDAO;
import org.mlink.iwm.util.*;
import java.util.*;
import javax.naming.InitialContext;

public class GlobalIndexer extends BaseAgent {
  //  private BeanIndexer bi;
  private TableIndexer ti;
  //  private AgentDAO ad;

 public GlobalIndexer() {
    this("GlobalIndexer");
 }   
 public GlobalIndexer(String dir)  {
    super(dir);
    try{
      //      ad = AgentDAO.getInstance();
      System.out.println("HERE");
      //      bi  = new BeanIndexer(dir);
      ti  = new TableIndexer(dir);
      System.out.println("THERE"+ti);
    }
    catch(Exception ex) {
      ex.printStackTrace();
      log(ex.toString());
    }
  }

  public void indexClass(String name) throws Exception{
    ti.index(name);
    //      Collection list =  ad.getTable(name);
      /**    Iterator it =map.iterator();
    while(it.hasNext()) {
      bi.add(it.next());
    }
      **/
    //    System.out.println(list.size());
    //    bi.addList(list);
  }
 
  public Collection run(Collection col) {
    return col;
  }


}
