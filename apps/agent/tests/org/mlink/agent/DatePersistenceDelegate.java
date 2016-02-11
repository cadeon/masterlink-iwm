package org.mlink.agent;

import java.beans.*;

/**
Temporary class to fix JDK bug 4733558 http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=4733558 which will be fixed in JDK 1.7

**/
    class DatePersistenceDelegate extends PersistenceDelegate {

        protected Expression instantiate(Object oldInstance, java.beans.Encoder out) {
            java.sql.Date d = (java.sql.Date)oldInstance;
            return new Expression(oldInstance,
                d.getClass(),
                "valueOf",
                new Object[]{
                    d.toString()
                }
            );
        }
    }

