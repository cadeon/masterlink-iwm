package org.mlink.agent;

import org.mlink.agent.model.*;
import org.mlink.agent.util.*;

import java.util.*;

import junit.framework.*;

import org.apache.lucene.store.*;
import org.apache.lucene.document.*;
import org.apache.lucene.analysis.*;
import org.apache.lucene.index.*;
import org.apache.lucene.search.*;
import org.apache.lucene.queryParser.*;

public class BeanFinderTest extends MlinkTestCase {
	  Analyzer analyzer = new SmartAnalyzer();
    private BeanIndexer bi; 
    Job j1;
  String testdir = "global_test";
    public BeanFinderTest(String name) {
        super(name);
    }

    public void setUp() throws Exception {
        super.setUp();
        bi = new BeanIndexer(testdir);
        j1 = (Job) getBean("Job","INS_to_WFP");
        bi.add(j1); 
    }

    public void tearDown() throws Exception {
        super.tearDown();
    }
    
   
    public void testFindBeanByField() throws Exception {
        Map result = BeanHelper.toMap(j1); 
        p(result.toString());
        List beans = findBeans("description:1");    
        assertTrue(beans.toString().indexOf("estTime=60") > -1);
    }

    public void testFindByDefaultField() throws Exception {
        Map result = BeanHelper.toMap((Job) getBean("Job","INS_to_WFP")); 
        List beans = findBeans("60");    
         // assertTrue(beans.contains(result));
        assertTrue(beans.toString().indexOf("estTime=60") > -1);
    }
  

    public static Test suite() {
        return new TestSuite(BeanFinderTest.class);
    }

   public List <Map> findBeans(String search)  {
     final List <Map> beans  = new ArrayList<Map>();
     try {
       QueryParser parser = new QueryParser("all", analyzer);
       final Query query = parser.parse(search);
       final IndexReader reader = IndexReader.open(testdir);
       final IndexSearcher searcher = new IndexSearcher(reader);
       Iterator it = searcher.doc(0).getFields().iterator();
       while (it.hasNext())  {
         Field f = (Field) it.next();
       }
       p("QUERY:"+query.toString());
       final Hits hits = searcher.search(query);
            for (int i = 0; i != hits.length() ; ++i) {
                Map bean = new HashMap();
                Document doc = hits.doc(i);
                Iterator fields = doc.getFields().iterator();
                while (fields.hasNext()) {
                  Field field = (Field) fields.next();
                  String custom = "all,mlink.class";
                  if (custom.indexOf(field.name()) < 0 ){
                      bean.put(field.name(), field.toString());
                  }
                }
                bean.put("INDEX_SCORE",hits.score(i));
                beans.add(bean);
            }
            searcher.close();
            reader.close();
     } catch (Exception ex) {
       ex.printStackTrace();
       Assert.fail("CRAP: "+ex.toString());
     }
     return beans;

        /**
        Collections.sort(students, new Comparator()
        {
            public int compare(final Object o1, final Object o2)
            {
                final Integer id_1 = ((Student) o1).getId();
                final Integer id_2 = ((Student) o1).getId();
                for (int i = 0; i < ids.size(); i++)
                {
                    final Integer integer = (Integer) ids.get(i);
                    if (integer.equals(id_1))
                    {
                        return -1;
                    }
                    if (integer.equals(id_2))
                    {
                        return 1;
                    }
                }
                return 0;
            }
        });
        **/
  }
}
