package org.mlink.agent.util;

import java.beans.*;
import java.lang.reflect.*;
import java.util.*;

public class BeanHelper  {
  public static Map toMap(Object obj) throws Exception {
     Map beanMap = new HashMap();
     final BeanInfo info = Introspector.getBeanInfo(obj.getClass());
	   final PropertyDescriptor[] props = info.getPropertyDescriptors();
     for (int i = 0; i != props.length; ++i) {
       final PropertyDescriptor prop = props[i];
       final String name = prop.getName();
       final Method reader = prop.getReadMethod();
       final Object value = reader.invoke(obj, new Object[]{});
       final String val = String.valueOf(value);
       beanMap.put(name,val);
     }
     return beanMap;
  }
}