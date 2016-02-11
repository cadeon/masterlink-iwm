package org.mlink.iwm.dao;

import org.mlink.iwm.util.CopyUtils;

import java.util.Map;

/**
 * User: andrei
 * Date: Oct 25, 2006
 */
public  class SearchCriteria {
    public enum ResultCategory{CHILDREN,FIRST_CHILDREN,PARENTS}
    private ResultCategory resultCategory=ResultCategory.CHILDREN;

    String filterText ;
    Long id;          //generic purpose

    int offset;
    int pageSize;
    String orderBy;
    String orderDirection;

    public SearchCriteria() {
    }

    public SearchCriteria(Map criteria) throws Exception{
        CopyUtils.copyProperties(this,criteria);
    }

    public SearchCriteria(Map criteria, ResultCategory rc) throws Exception{
        CopyUtils.copyProperties(this,criteria);
        this.resultCategory = rc;
    }

    public SearchCriteria(ResultCategory rc) throws Exception{
        this.resultCategory = rc;
    }

    public ResultCategory getResultCategory() {
        return resultCategory;
    }

    public String getFilterText() {
        if(filterText!=null && filterText.length()==0) filterText=null;
        return filterText;
    }

    public String getFilterText2() {
        return filterText==null?"":filterText;
    }

    public void setFilterText(String filterText) {
        this.filterText = filterText;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public String getOrderDirection() {
        return orderDirection;
    }

    public void setOrderDirection(String orderDirection) {
        this.orderDirection = orderDirection;
    }
}
