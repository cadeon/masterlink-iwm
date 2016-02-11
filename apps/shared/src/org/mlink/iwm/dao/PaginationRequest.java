package org.mlink.iwm.dao;

import java.util.Map;
import java.util.HashMap;
import java.util.EnumMap;

/**
 * User: andrei
 * Date: Sep 18, 2006
 */
public class PaginationRequest {
    public static final int PAGE_SIZE_UNLIMITED = 10000;  // no paging, return as that many
    public static enum Direction {ASC,DESC,NODIR}
    public static EnumMap<Direction,String> DirectionValues = new EnumMap<Direction,String>(Direction.class);

    static{
        DirectionValues.put(Direction.ASC, "ASC");
        DirectionValues.put(Direction.DESC, "DESC");
        DirectionValues.put(Direction.NODIR, "NODIR");
    }

    private int offset;
    private int pageSize;
    private  Map <String,Object> parameters= new HashMap <String,Object>();

    private String orderBy;
    private Direction orderDirection;

    public PaginationRequest(int offset, int pageSize, String orderBy, String orderDirection) {
            if("DESC".equalsIgnoreCase(orderDirection)){
                this.orderDirection=Direction.DESC;
            }else if("ASC".equalsIgnoreCase(orderDirection)){
                this.orderDirection=Direction.ASC;
            }else{
                this.orderDirection=Direction.NODIR;
            }
        this.offset = offset;
        this.pageSize = pageSize;
        this.orderBy = orderBy;
    }

    public PaginationRequest(String orderBy, String direction) {
        this(0,PAGE_SIZE_UNLIMITED,orderBy,direction);
    }

    public PaginationRequest(String orderBy) {
        this(0,PAGE_SIZE_UNLIMITED,orderBy,DirectionValues.get(Direction.ASC));
    }

    public PaginationRequest() {
        this(null,null);
    }

    public int getOffset() {
        return offset;
    }

    public int getPageSize() {
        return pageSize;
    }

    public Map getParameters() {
        return parameters;
    }

    public void addParameter(String name, Object value){
        parameters.put(name,value);
    }

    public String getOrderBy() {
        return orderBy;
    }

    public Direction getOrderDirection() {
        return orderDirection;
    }
}
