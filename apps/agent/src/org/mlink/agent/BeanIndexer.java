/*
 * Copyright (c) 2004 Brian McCallister. All Rights Reserved.
 */
package org.mlink.agent;


import java.util.*;
import org.apache.lucene.analysis.Analyzer;
import java.beans.*;
import java.lang.reflect.*;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.mlink.agent.util.SmartAnalyzer;

import javax.servlet.ServletContext;
import java.io.File;
import java.io.IOException;

public class BeanIndexer
{
    /**
     * Can change to servlet init param if ever need to
     */
    public static final String SERVLET_INDEX = "WEB-INF/bean-index";

    final File index;

    private final static Object mutex = new Object();
    private final Analyzer analyzer;

    public BeanIndexer(final Analyzer analyzer, final String dir) throws IOException {
        this.analyzer = analyzer;
        index = new File(dir);
        if (!index.exists())
        {
            index.mkdir();
            synchronized (mutex)
            {
                final IndexWriter writer = new IndexWriter(index, new SmartAnalyzer(), true);
                writer.close();
            }
        }
    }

    public BeanIndexer(final String dir) throws IOException
    {
       this(new StandardAnalyzer(), dir);
    }

    public BeanIndexer(final ServletContext ctx) throws IOException
    {
        this(new StandardAnalyzer(), ctx.getRealPath(SERVLET_INDEX));
    }

  public void addList(Collection coll) throws Exception {
       Document doc = null;
	     BeanInfo info = null;
	     PropertyDescriptor[] props = null;
      Iterator c = coll.iterator();
      final IndexWriter writer = new IndexWriter(index, analyzer, false);

      while(c.hasNext()) {
        Object instance = c.next();
        if (info == null) {
          info = Introspector.getBeanInfo(instance.getClass());
	        props = info.getPropertyDescriptors();
        }
        String contents = "";
          doc = new Document();
        for (int i = 0; i != props.length; ++i) {
            final PropertyDescriptor prop = props[i];
            final String name = prop.getName();
            final Method reader = prop.getReadMethod();
            final Object value = reader.invoke(instance, new Object[]{});
            final String val = String.valueOf(value);
            doc.add(indexField(name,val ));
            contents += val+" ";    
            }
        doc.add(indexField("all",contents));
        writer.addDocument(doc); 
      }
     writer.optimize();
     writer.close();
  }

  public void add(final Object instance) throws Exception {
    List list = new ArrayList();
    list.add(instance);
    addList(list);
  }

  public Field indexField(String name, String val) {
    return new Field(name, val, Field.Store.YES, Field.Index.TOKENIZED);
  }

}
