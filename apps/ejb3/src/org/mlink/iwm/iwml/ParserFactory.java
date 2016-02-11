package org.mlink.iwm.iwml;

/**
 * Created by IntelliJ IDEA.
 * User: andreipovodyrev
 * Date: Jun 7, 2007
 */
public class ParserFactory {

    public static Parser getParser(String content){
      if(content.indexOf("<iwml")>=0){
          return new IWMLDigester();
      }else if(content.indexOf("<sitar-jobs")>=0){
          return new SitarJobDigester();
      }else{
          return new DefaultParser();
      }
    }



}
