package org.mlink.iwm.iwml;

import org.apache.commons.digester.Digester;

/**
 * Created by IntelliJ IDEA.
 * User: andreipovodyrev
 * Date: Jun 7, 2007
 */
public class DefaultParser extends Parser{

    protected Digester prepare() {
        return null;
    }

    public void process(String data)  {
        log("Parser did not find any data that can be persisted by the system");
        addError("Parser did not find any data that can be persisted by the system");
    }


}
