package org.mlink.iwm.dwr;

import org.mlink.iwm.bean.ResponsePage;
import org.mlink.iwm.util.*;
import org.mlink.iwm.iwml.Parser;
import org.mlink.iwm.iwml.ParserFactory;
import org.mlink.iwm.iwml.WP30ExcelParser;
import org.mlink.sitar.demo.*;
import org.apache.log4j.Logger;

import java.util.*;
import java.io.File;

/**
 * User: andrei
 * Manages files uploaded by the IWM.
 * Date: Oct 25, 2006
 */
public class Uploads implements ReturnCodes{
    private static final Logger logger = Logger.getLogger(Uploads.class);

    public ResponsePage getData(HashMap criteria, int offset, int pageSize, String orderBy, String orderDirection) throws Exception {
        FileAccess fa = new FileAccess(Config.getFileStoreLocation());
        logger.debug("reading files from " + new File(Config.getFileStoreLocation()).getCanonicalPath());
        List <File> files = fa.getFiles(offset,pageSize,orderBy,orderDirection);
        List <Map> rtn = new ArrayList<Map>();
        for (File file : files) {
            Map map = new HashMap(3);
            if(!file.getName().endsWith(".log")){     //do not include log files in the list
                map.put("name",file.getName());
                map.put("lastModified", ConvertUtils.formatDatetime(new java.sql.Date(file.lastModified())));
                map.put("type", fa.getKeyWord(file));
                for (File file2 : files) {
                    if(file2.getName().equals(file.getName()+".log")) {
                        map.put("log", file2.getName());
                        break;
                    }
                }
                rtn.add(map);
            }
        }
        return new ResponsePage(rtn.size(),rtn);
    }


    public String deleteItem(String fileName) throws Exception {
        String rtn = ITEM_SAVED_OK_MSG;
        FileAccess fa = new FileAccess(Config.getFileStoreLocation());
        fa.deleteFile(fileName);
        if(fa.getFile(fileName+".log").exists()) fa.deleteFile(fileName+".log");
        return rtn;
    }

    public Object getItem(String filename) throws Exception {
        FileAccess fa = new FileAccess(Config.getFileStoreLocation());
        return fa.getFileContent(filename);
    }

    public Object getExcelItem(String filename) throws Exception {
        String rtn="";
        FileAccess fa = new FileAccess(Config.getFileStoreLocation());
        if(filename.startsWith("VP30")){
            WP30ExcelParser ep = new WP30ExcelParser(fa.getFile(filename));
            rtn =  ep.toXML();
        }else{
            ExcelParser ep = new ExcelParser(fa.getFile(filename));
            rtn =  ep.echo();
        }
        logger.debug(rtn);
        return rtn;
    }


    public String processFile(String filename) throws Exception {
        String rtn,log;
        FileAccess fa = new FileAccess(Config.getFileStoreLocation());
        if(filename.endsWith("xls") && filename.startsWith("VP30")){
            WP30ExcelParser ep = new WP30ExcelParser(fa.getFile(filename));
            SitarType sitar =  ep.digest();
            SitarAgentService service = new SitarAgentService();
            SitarAgentPort port = service.getSitarAgent();
            logger.debug("Sending WS message to TAPS");
            DataAgentResponse r = port.publishData(sitar);
            logger.debug("Result:"+ r.getResult());
            logger.debug("Message:"+r.getMessage());

            log =  "The following message was sent to TAPS system on " + new Date() + "\n" + ep.toXML();
            rtn = "Action performed successfuly. Web Service message was sent to TAPS system. See the log for details.";

        }else if(filename.endsWith("xls")){
            ExcelParser ep = new ExcelParser(fa.getFile(filename));
            log =  ep.echo();
            rtn = "Action performed successfuly. See the log for details.";
        }else { //assuming xml files else if (filename.endsWith("xml")){
            String content = new String(fa.getBytes(filename));
            Parser dg = ParserFactory.getParser(content);
            dg.process(content);
            log = dg.getLog();
            if(dg.getErrors().size()>0){
                rtn = "Action performed with errors. See the log for details.";
            }else{
                rtn = "Action performed successfuly. See the log for details.";
            }
        }

        FileAccess fLog = new FileAccess(Config.getFileStoreLocation());
        fLog.saveFile(filename+".log",log);
        logger.debug(rtn);
        return rtn;
    }

}
