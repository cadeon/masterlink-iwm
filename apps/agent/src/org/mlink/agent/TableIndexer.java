/*
 * Copyright (c) 2004 Brian McCallister. All Rights Reserved.
 */
package org.mlink.agent;

import java.util.*;

import java.sql.*;


import org.apache.lucene.analysis.Analyzer;
import java.beans.*;
import java.lang.reflect.*;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.mlink.agent.util.SmartAnalyzer;
import org.mlink.iwm.util.*;

import javax.servlet.ServletContext;
import java.io.File;
import java.io.IOException;

public class TableIndexer
{
    /**
     * Can change to servlet init param if ever need to
     */
    public static final String SERVLET_INDEX = "WEB-INF/bean-index";

    final File index;
    private final Analyzer analyzer;

    public TableIndexer(final Analyzer analyzer, 
                        final String dir) throws IOException {
        this.analyzer = analyzer;
        index = new File(dir);
        if (!index.exists()) {
          index.mkdir();
          final IndexWriter writer = new IndexWriter(index, 
                                                     new SmartAnalyzer(), 
                                                     true);
          writer.close();
        }
    }

    public TableIndexer(final String dir) throws IOException {
        this(new SmartAnalyzer(), dir);
    }

    public TableIndexer(final ServletContext ctx) throws IOException {
        this(new SmartAnalyzer(), ctx.getRealPath(SERVLET_INDEX));
    }

  public void index(String table) throws Exception {
    String schema = System.getProperty("database.schema",
                                       "mlink_blake");

    List <Map> coll = DBAccess.execute("select * from "+schema+"."+table);

       Document doc = null;
      Iterator c = coll.iterator();
      final IndexWriter writer = new IndexWriter(index, analyzer, false);
      //      int i = 0;  
      while(c.hasNext()) {
        String contents = "";
        doc = new Document();
        Map map = (Map) c.next();
        Iterator keys = map.keySet().iterator();
        while(keys.hasNext()) {
          String key =(String) keys.next();
          String val = map.get(key).toString();
          doc.add(indexField(key,val));
            contents += val+" ";    
            }
        doc.add(indexField("class",table));
        doc.add(indexField("all",contents));
        writer.addDocument(doc); 
        //        i ++;
        //        if(i%20==0)  writer.optimize();
      }
      //     writer.optimize();
     writer.close();
  }


  public Field indexField(String name, String val) {
    return new Field(name, val, Field.Store.YES, Field.Index.TOKENIZED);
  }

}
