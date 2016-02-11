package org.mlink.iwm.util;



import java.lang.StringBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.TreeSet;


/** StringUtils is a static class that provides a set of common services that are needed by web applications
 * to manipulate string values.  Services offered by StringUtils include
 * converting source strings to values that can be used safely in JavaScript, XML, or
 * HTML.
 *
 * For example, a string containing the '>' character cannot be used safely as
 * an attribute value in an HTML tag since some browsers would mistakenly interpret the
 * value as the closing of the tag.  This class provides a method htmlSafeString which
 * will convert the '>' character in the string to its HTML safe equivalent value.
 *
 * Any other methods that deal with String manipulation (truncating strings to a
 * particular length, possibly) should be added to this class as the need arises.
 *
 */
public class StringUtils
{

    static public final String SPACE="&nbsp;";


   /**
   * Take a source string and return an equivalent string with all characters that
   * cannot be used in a JavaScript string value escaped correctly.
   *
   * @param source a String to convert to a JavaScript safe equivalent value
   * @return a String representing the input source String with all characters
   * that are dangerous for a JavaScript value escaped correctly.
   */
   static public String javascriptSafeString(String source)   {
     if (source==null) return "";
     StringTokenizer st = new StringTokenizer( source, "<>'\"\\\t\n\r", true );
     String target = new String();

     String temp;
     while ( st.hasMoreTokens() )
     {
        temp = st.nextToken();
        if ( temp.length() != 1 ) { target += temp; continue; }
        if ( "'".equals(temp))  { target += "\\'"; continue; }
        if ( "<".equals(temp))  { target += "\\<"; continue; }
        if ( ">".equals(temp))  { target += "\\>"; continue; }
        if ( "\"".equals(temp)) { target += "\\\""; continue; }
        if ( "\\".equals(temp)) { target += "\\\\"; continue; }
        if ( "\t".equals(temp)) { target += "\\t"; continue; }
        if ( "\n".equals(temp)) { target += "\\n"; continue; }
        if ( "\r".equals(temp)) { target += "\\r"; continue; }
        target += temp;
     }
     return target;
   }

   /**
   * Take a source string and return an equivalent string with all characters that
   * cannot be used in an XML attribute value escaped correctly.
   *
   * @param source a String to convert to an XML safe equivalent value
   * @return a String representing the input source String with all characters
   * that are dangerous for an XML attribute value escaped correctly.
   */
   static public String xmlSafeString(String source)   {
    if (source==null) return "";
    StringTokenizer st = new StringTokenizer( source, "&<>'\"\\\t\n\r", true );
    String target = new String();

    String temp;
    while ( st.hasMoreTokens() )
    {
       temp = st.nextToken();
       if ( temp.length() != 1 ) { target += temp; continue; }
       if ( "&".equals(temp)) { target += "&amp;"; continue; }
       if ( "<".equals(temp)) { target += "&lt;"; continue; }
       if ( ">".equals(temp)) { target += "&gt;"; continue; }
       if ( "\'".equals(temp)) { target += "&#039;"; continue; }
       if ( "\"".equals(temp)) { target += "&quot;"; continue; }
       if ( "\t".equals(temp)) { target += "&#009;"; continue; }
       if ( "\n".equals(temp)) { target += "&#010;"; continue; }
       if ( "\r".equals(temp)) { target += "&#013;"; continue; }
       target += temp;
    }
    return target;
   }


  /**
   * Take a source string and return an equivalent string with all characters that
   * cannot be used in an HTML tag escaped correctly.
   *
   * @param source a String to convert to an HTML safe equivalent value
   * @return a String representing the input source String with all characters
   * that are dangerous for an HTML attribute value escaped correctly.
   */
  static public String htmlSafeString(String source)  {
    if (source==null) return "";
    StringTokenizer st = new StringTokenizer( source, "&<>'\"\\\t\n\r", true );
    String target = new String("");

    String temp;

    while ( st.hasMoreTokens() )
    {
       temp = st.nextToken();
       if ( temp.length() != 1 ) { target += temp; continue; }
       if ( "&".equals(temp)) { target += "&amp;"; continue; }
       if ( "<".equals(temp)) { target += "&lt;"; continue; }
       if ( ">".equals(temp)) { target += "&gt;"; continue; }
       if ( "\'".equals(temp)) { target += "&#039;"; continue; }
       if ( "\"".equals(temp)) { target += "&quot;"; continue; }
       if ( "\t".equals(temp)) { target += "&#009;"; continue; }
       if ( "\n".equals(temp)) { target += "&#010;"; continue; }
       if ( "\r".equals(temp)) { target += "&#013;"; continue; }
       target += temp;
    }
    return target;
  }

   static public String htmlWithBreaks(String source)   {
    if (source==null) return "";
    StringTokenizer st = new StringTokenizer( source, "\n\r", true );
    String target = new String("");

    String temp;
    while ( st.hasMoreTokens() )    {
       temp = st.nextToken();
       if ( temp.length() != 1 ) { target += temp; continue; }
       if ( "\n".equals(temp)) { target += "<br>"; continue; }
       if ( "\r".equals(temp)) { target += "<br>"; continue; }
       target += temp;
    }
    return target;
  }


   static public String truncate(String source, int size)
   {

      if (source.length() <= size)
      {
         return source;
      }
      else
      {
         if (size > 4)
           return source.substring(0,size-3) + "...";
         else
           return source.substring(0,size);
      }
   }


   public static String valueOf(char c)
   {
       char ac[] = new char[1];
       ac[0] = c;
       return new String(ac);
   }

   public static String valueOf(double d)
   {
       return Double.toString(d);
   }

   public static String valueOf(float f)
   {
       return Float.toString(f);
   }

   public static String valueOf(int i)
   {
       char ac[] = new char[11];
       int j = ac.length;
       boolean flag = i < 0;
       if(!flag)
           i = -i;
       for(; i <= -10; i /= 10)
           ac[--j] = Character.forDigit(-(i % 10), 10);

       ac[--j] = Character.forDigit(-i, 10);
       if(flag)
           ac[--j] = '-';
       return new String(ac, j, ac.length - j);
   }

   public static String valueOf(long l)
   {
       return Long.toString(l, 10);
   }

   public static String valueOf(Object obj)
   {
       return obj != null ? obj.toString() : "";
   }

   public static String valueOf(boolean flag)
   {
       return flag ? "true" : "false";
   }

   public static String valueOf(char ac[])
   {
       return new String(ac);
   }

   public static Long getLong(String str){
       Long rtn = null;
       try{
           rtn  = Long.valueOf(str);
       }catch(NumberFormatException e){}
       return rtn;
   }
   public static Integer getInteger(String str){
       Integer rtn = null;
       try{
           rtn  = Integer.valueOf(str);
       }catch(NumberFormatException e){}
       return rtn;
   }

   public static int getInt(String str){
       int rtn = 0;
       try{
           rtn  = Integer.parseInt(str);
       }catch(NumberFormatException e){}
       return rtn;
   }

   public static int forceInt(String str){
       int rtn = 0;
       try{
           rtn  = Double.valueOf(str).intValue();
       }catch(Exception e){}
       return rtn;
   }

   public static Double getDouble(String str){
       Double rtn = null;
       try{
           rtn  = Double.valueOf(str);
       }catch(NumberFormatException e){}
       return rtn;
   }

    public static String sumListItems(List list) {
        double sum = 0d;
        for (int i = 0; i < list.size(); i++) {
            String value =(String) list.get(i);
            if(value == null || "".equals(value))
                continue;
            sum = sum + Double.parseDouble(value);
        }
        return String.valueOf(sum);
    }

    public static List convertCSVToList(String ids){
        StringTokenizer st = new StringTokenizer(ids, ",");
        List idList = new ArrayList();
        while (st.hasMoreTokens()) {
            idList.add(st.nextToken());
        }
        return idList;
    }

    public static String convertListToCSV(List ids){
        StringBuffer rtn = new StringBuffer();
        TreeSet ts = new TreeSet(ids);  // remove dups if any
        for (int i = 0; i < ts.size(); i++) {
            String id =(String) ids.get(i);
            if(i==0)
                 rtn.append(id);
            else
                 rtn.append(","+id);
        }

        return rtn.toString();
    }

    public static String parseMinutes(String timeInMinutes){
        if(timeInMinutes==null) return null;
        int time = Integer.parseInt(timeInMinutes);
        int minutes = time%60;
		String pad = (minutes<10?"0":"");
		return  "" + (int)Math.floor(time/60)+":" + pad + minutes;
    }

    public static String parseMinutes(Integer timeInMinutes){
        if(timeInMinutes==null) return null;
        int time = timeInMinutes;
        int minutes = time%60;
		String pad = (minutes<10?"0":"");
		return  "" + (int)Math.floor(time/60)+":" + pad + minutes;
    }

    //in xml "" is same as null
    public static boolean compareXMLString(String str, String xmlString){
    	 boolean equals = true;
    	 if(!((str == null || (str.length()==0)) && (xmlString == null || (xmlString.length()==0))))	{
    		 equals = xmlString.equals(str);
    	 }
    	 return equals;
    }
}



