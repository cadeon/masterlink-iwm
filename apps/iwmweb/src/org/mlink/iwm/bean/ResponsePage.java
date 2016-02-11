package org.mlink.iwm.bean;

import java.util.List;

/**
 * User: andrei
 * Date: Sep 18, 2006
 */
public class ResponsePage {
    int totalCount;
    List items;

    public ResponsePage(int totalCount, List items) {
        this.totalCount = totalCount;
        this.items = items;
    }

    public List getItems() {
        return items;
    }

    public int getTotalCount() {
        return totalCount;
    }
}
