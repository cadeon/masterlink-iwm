package org.mlink.iwm.jms;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: andreipovodyrev
 * Date: Jul 5, 2007
 */
public interface Command extends Serializable {
    void execute() throws Exception;
}
