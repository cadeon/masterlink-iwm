package org.mlink.iwm.dao;

import java.util.Map;
import java.util.List;

import org.mlink.iwm.dao.PaginationRequest;

/**
 * User: andrei
 * Date: Sep 18, 2006
 */
public class PaginationResponse extends DAOResponse {

    // number of rows in all pages combined
    private int totalCount;
    // starting row
    private int offset;
    // number of rows in page
    private int pageSize;

    public PaginationResponse(List<Map> rows, int totalCount, int offset, int pageSize) {
        this.rows = rows;
        this.totalCount = totalCount;
        this.offset = offset;
        this.pageSize = pageSize;
    }

    public PaginationResponse(List<Map> rows, int totalCount) {
        this.rows = rows;
        this.totalCount = totalCount;
        this.offset = 1;
        this.pageSize = PaginationRequest.PAGE_SIZE_UNLIMITED;
    }

    public PaginationResponse() {
        this.totalCount = 0;
        this.offset = 1;
        this.pageSize = PaginationRequest.PAGE_SIZE_UNLIMITED;
    }



    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getPageSize() {
        return pageSize;
    }

    public int getOffset() {
        return offset;
    }


}
