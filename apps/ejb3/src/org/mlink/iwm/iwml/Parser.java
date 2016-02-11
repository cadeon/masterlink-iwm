package org.mlink.iwm.iwml;

import org.xml.sax.SAXException;
import org.apache.commons.digester.Digester;

import java.util.List;
import java.util.ArrayList;
import java.io.InputStream;
import java.io.IOException;
import java.io.StringReader;

/**
 * Created by IntelliJ IDEA.
 * User: andreipovodyrev
 * Date: Jun 7, 2007
 */
public abstract class Parser {
    StringBuilder log = new StringBuilder();
    List <String> errors = new ArrayList<String>();

    void log(String statetement){
        log.append(statetement).append("\n");
    }

    void addError(String error){
        errors.add(error);
    }

    public String getLog(){
        return log.toString();
    }

    public List<String> getErrors(){
        return errors;
    }

    public void process(String iwml)  {
        try {
            store(parse(iwml));
        } catch (Exception e) {
            addError(e.getMessage());
            log(e.getMessage());
        }
    }

    /**
     * @param is
     * @return
     * @throws org.xml.sax.SAXException
     * @throws java.io.IOException
     */
    public List parse(InputStream is) throws SAXException, IOException {
        Digester digester = prepare();
        return  (List) digester.parse(is);
    }

    public List parse(String iwml) throws SAXException, IOException {
        Digester digester = prepare();
        return  (List) digester.parse(new StringReader(iwml));
    }

    private void store(List objects){
        for (Object object : objects) {
            Updator u = UpdatorFactory.getUpdator(object);
            try{
                u.process();
            }catch(Exception e){
                addError(e.getMessage());
            }

            log(u.getLog());
        }
    }

    public void process(InputStream is)  {
        try {
            store(parse(is));
        } catch (Exception e) {
            addError(e.getMessage());
            log(e.getMessage());
        }
    }

    protected abstract Digester prepare();
}
