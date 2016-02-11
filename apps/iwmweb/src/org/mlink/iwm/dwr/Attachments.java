package org.mlink.iwm.dwr;

import java.util.HashMap;
import java.util.List;

import org.mlink.iwm.bean.ResponsePage;
import org.mlink.iwm.dao.AttachmentDAO;
import org.mlink.iwm.dao.DaoFactory;
import org.mlink.iwm.dao.PaginationRequest;
import org.mlink.iwm.dao.PaginationResponse;
import org.mlink.iwm.dao.SearchCriteria;

/**
 * Created by IntelliJ IDEA.
 * User: andreipovodyrev
 * Date: Jul 26, 2007
 */
public class Attachments implements ReturnCodes{


    public ResponsePage getData(HashMap criteria, int offset, int pageSize, String orderBy, String orderDirection) throws Exception {
        SearchCriteria cr = new SearchCriteria(criteria);
        PaginationRequest request = new PaginationRequest(offset,pageSize,orderBy,orderDirection);
        PaginationResponse response = DaoFactory.process(DaoFactory.NAME.AttachmentDAO,cr,request);
        List lst =  response.convertRowsToClasses(org.mlink.iwm.bean.Attachment.class);
        return new ResponsePage(response.getTotalCount(),lst);
    }


    public String deleteItem(Long attachId) throws Exception {
        AttachmentDAO.deleteAttachment(attachId);
        return ITEM_SAVED_OK_MSG;
    }

}

