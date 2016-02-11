package org.mlink.iwm.dao;

import org.apache.log4j.Logger;
import org.mlink.iwm.notification.Attachment;
import org.mlink.iwm.util.DBAccess;

import java.sql.*;
import java.io.OutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.List;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: andreipovodyrev
 * Date: Jul 16, 2007
 */
public class AttachmentDAO extends ListDAOTemplate{

    protected static Properties nameToColumnMap = new Properties();
    static {
        nameToColumnMap.put("id","ID");
        nameToColumnMap.put("filename","FILE_NME");
        nameToColumnMap.put("description","DESCRIPTION");
        nameToColumnMap.put("createdDate","CREATED_DATE");
        nameToColumnMap.put("size","BYTES");
    }

    private static final Logger logger = Logger.getLogger(AttachmentDAO.class);
    private static final String SEQ_NAME = "ATTACHMENT_SEQ";
    private static final String OP_STREAM_METHOD = "getBinaryOutputStream";


    protected String getSql(SearchCriteria cr) {
        return "SELECT ID,FILE_NME,DESCRIPTION,CREATED_DATE,BYTES FROM ATTACHMENT";
    }

    protected  Properties getPropertyToColumnMap(){
        return nameToColumnMap;
    }


    public PaginationResponse getData(SearchCriteria cr, PaginationRequest request) throws SQLException {
        String sql = getSql(cr);
        List parameters = new ArrayList();
        parameters.add(cr.getId());
        return process(parameters,sql,request);
    }

    public  static Attachment getAttachment(long attachmentId) throws SQLException{
        String sqlSelect = "SELECT BIN_DATA,MIME_TYP,FILE_NME,DESCRIPTION FROM ATTACHMENT WHERE ID = ?";
        byte [] bytes;
        Connection con = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        String contentType=null,name=null,desc=null;
        Attachment at = null;
        try {
            con = DBAccess.getDBConnection();
            st = con.prepareStatement(sqlSelect);
            st.setLong(1, attachmentId);
            rs = st.executeQuery();
            Blob blob = null;
            while(rs.next()){
                blob = rs.getBlob(1);
                contentType = rs.getString(2);
                name = rs.getString(3);
                desc = rs.getString(4);
            }
            bytes = blob.getBytes(1,(int)blob.length());
            at = new Attachment(bytes,contentType,name,desc);
        } catch (SQLException e) {
           throw e;
        } finally {
            DBAccess.close(rs,st,con);
        }
        return at;
    }

    public  static void deleteAttachment(long attachmentId) throws SQLException{
        String sqlSelect = "DELETE FROM ATTACHMENT WHERE ID = ?";
        Connection con = null;
        PreparedStatement st = null;
        try {
            con = DBAccess.getDBConnection();
            //con.setAutoCommit(false);
            st = con.prepareStatement(sqlSelect);
            st.setLong(1, attachmentId);
            int res =  st.executeUpdate();
            logger.info("Result of delete attachment == "+res);
        } catch (SQLException e) {
            throw e;
        } finally {
            DBAccess.close(null,st,con);
        }
    }

    public  long  addAttachment(Attachment attachment) throws SQLException {
        Connection con = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        Long id = null;
        String fileName = attachment.getName();
        if(fileName.length()>30) fileName=fileName.substring(fileName.length()-30);
        if(fileName.contains("/")) fileName=fileName.substring(fileName.lastIndexOf("/"));
        String mimeType = attachment.getContentType();
        String desc = attachment.getDescription();
        byte[] data = attachment.getBytes();
        //Use the empty_blob syntax to create the BLOB locator.
        String sqlInsert = "INSERT INTO ATTACHMENT (ID, FILE_NME, MIME_TYP, DESCRIPTION, BYTES, BIN_DATA) VALUES(?,?,?,?,?, empty_blob())";
        //Get the BLOB locator from the table.
        String sqlSelect = "SELECT BIN_DATA FROM ATTACHMENT WHERE ID = ? for update";
        String sqlUpdate = "UPDATE ATTACHMENT SET BIN_DATA = ? WHERE ID = ?";

        OutputStream os = null;
        try {
            con = DBAccess.getDBConnection();
            con.setAutoCommit(false);     //ORA-22920: row containing the LOB value is not locked if commit is true
            id = DBAccess.generateID(SEQ_NAME,con);
            st = con.prepareStatement(sqlInsert);
            st.setInt(1, id.intValue());
            st.setString(2, fileName);
            st.setString(3, mimeType);
            st.setString(4, desc);
            st.setInt(5, data.length);
            //st.setDate(4, new java.sql.Date(System.currentTimeMillis()));
            st.executeUpdate();
            st.close();

            st = con.prepareStatement(sqlSelect);
            st.setInt(1, id.intValue());
            rs = st.executeQuery();

            rs.next();
            java.sql.Blob blob = rs.getBlob(1);
            try {
                java.lang.reflect.Method meth = blob.getClass().getMethod(OP_STREAM_METHOD,null);
                os = (OutputStream) meth.invoke(blob,null);
                os.write(data);
                os.flush();
            } catch (Exception e) {
                e.printStackTrace();
            }
            st.close();
            rs.close();

            st = con.prepareStatement(sqlUpdate);
            st.setBlob(1, blob);
            st.setInt(2, id.intValue());
            st.executeUpdate();
            con.commit();

        }catch(SQLException e){
            con.rollback();
            throw e;
        } finally {
            DBAccess.close(rs,st,con);
            try{if(os!=null) os.close();}catch(IOException e){logger.error(e);}
        }
        return id;

    }



}
