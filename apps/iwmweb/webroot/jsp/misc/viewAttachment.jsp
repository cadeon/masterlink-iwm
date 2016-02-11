<%@ page import="org.mlink.iwm.notification.Attachment"%>
<%@ page import="org.mlink.iwm.dao.AttachmentDAO"%>

<%
    try{
        Attachment at = AttachmentDAO.getAttachment(Long.valueOf(request.getParameter("id")).longValue());
        ServletOutputStream os = response.getOutputStream();
        String contentType = at.getContentType();
        if(contentType!=null)
            response.setContentType(contentType);
        else
            response.setContentType("text/plain");

        os.write(at.getBytes());    
        os.flush();
    }catch(Exception e){
        e.printStackTrace();
    }
%>