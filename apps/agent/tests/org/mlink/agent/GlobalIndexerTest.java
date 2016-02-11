package org.mlink.agent;

import org.mlink.agent.util.*;
import org.mlink.iwm.util.*;

import java.util.*;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.apache.lucene.store.*;
import org.apache.lucene.document.*;
import org.apache.lucene.analysis.*;
import org.apache.lucene.index.*;
import org.apache.lucene.search.*;
import org.apache.lucene.queryParser.*;


public class GlobalIndexerTest extends MlinkTestCase {
	
	private GlobalIndexer gi;
	Analyzer analyzer = new SmartAnalyzer();
	
    public GlobalIndexerTest(String name) {
        super(name);
    }

    public void setUp() throws Exception {
        super.setUp();
        //        dbConnect();
        gi = new GlobalIndexer("global_test");
    }

    public void tearDown() throws Exception {
        super.tearDown();
        gi = null;
    }

  public void testRetrival() throws Exception {
       QueryParser parser = new QueryParser("all", analyzer);
       final Query query = parser.parse("Fountain");
       final IndexReader reader = IndexReader.open("../../sample_index");
       final IndexSearcher searcher = new IndexSearcher(reader);
       //       Iterator it = searcher.doc(0).getFields().iterator();
       /**      while (it.hasNext())  {
         Field f = (Field) it.next();
       }
       **/
       p("QUERY:"+query.toString());
       final Hits hits = searcher.search(query);
       p(hits.length());
       for (int i = 0; i != hits.length() ; ++i) {
         Document doc = hits.doc(i);
         Iterator fields = doc.getFields().iterator();
         //         while (fields.hasNext()) {
           //           Field field = (Field) fields.next();
           //     String custom = "all,mlink.class";
           //        if (custom.indexOf(field.name()) < 0 ){
           //           p(""+i+":" + field.toString());
                        //                 }
         //  }
       }
       assertTrue(10 < hits.length());

 
  }

    
   
    public void testIndexClass() throws Exception {
       gi.indexClass("skill");
       QueryParser parser = new QueryParser("all", analyzer);
       final Query query = parser.parse("1?");
       final IndexReader reader = IndexReader.open("global_test");
       final IndexSearcher searcher = new IndexSearcher(reader);
       //       Iterator it = searcher.doc(0).getFields().iterator();
       /**      while (it.hasNext())  {
         Field f = (Field) it.next();
       }
       **/
       p("QUERY:"+query.toString());
       final Hits hits = searcher.search(query);
       p(hits.length());
       for (int i = 0; i != hits.length() ; ++i) {
         Document doc = hits.doc(i);
         Iterator fields = doc.getFields().iterator();
         while (fields.hasNext()) {
           Field field = (Field) fields.next();
           //     String custom = "all,mlink.class";
           //        if (custom.indexOf(field.name()) < 0 ){
                      p(""+i+":" + field.stringValue());
                        //                 }
         }
       }
       assertTrue(10 < hits.length());


    }

}
