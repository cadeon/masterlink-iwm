package org.mlink.iwm.util;

import java.io.*;
import java.util.*;

/**
 * User: andrei
 * Date: Apr 25, 2007
 */
public class FileAccess {
    private String directoryPath;
    public FileAccess(String directoryPath) {
        this.directoryPath=directoryPath;
        File dir = new File(this.directoryPath);
        if(!dir.exists())
            dir.mkdirs();
    }

    public List <File> getFiles(){
        File dir = new File(directoryPath);
        File [] children = dir.listFiles();
        return Arrays.asList(children);
    }

    public List <File> getFiles(int offset, int pageSize, final String orderBy, final String orderDirection){
        File dir = new File(directoryPath);
        File [] children = dir.listFiles();
        Arrays.sort(children, new Comparator(){
            public int compare(Object o1, Object o2) {
                Comparable c1,c2;
                if("lastModified".equals(orderBy)){
                    c1 =((File)o1).lastModified();
                    c2 =((File)o2).lastModified();
                }else {
                    c1 =((File)o1).getName();
                    c2 =((File)o2).getName();
                }
                if("ASC".equals(orderDirection))
                    return c1.compareTo(c2);
                else
                    return c2.compareTo(c1);
            }
        });
        List <File> page = new ArrayList <File>();
        for (int i = offset; i < Math.min(offset+pageSize,children.length); i++) {
            page.add(children[i]);
        }
        return page;
    }

    public File getFile(String fileName){
        return new File(directoryPath+"/"+fileName);
    }

    public String getFileContent(String fileName) throws IOException{
        StringBuilder sb = new StringBuilder();
        BufferedReader in = new BufferedReader(new FileReader(getFile(fileName)));
        String str;
        while ((str = in.readLine()) != null) {
            sb.append(str).append("\n");
        }
        in.close();
        return sb.toString();
    }


    public byte[] getBytes(String fileName) throws IOException {
        File file = getFile(fileName);
        InputStream is = new FileInputStream(file);

        // Get the size of the file
        long length = file.length();

        if (length > Integer.MAX_VALUE) {
            // File is too large
        }

        // Create the byte array to hold the data
        byte[] bytes = new byte[(int)length];

        // Read in the bytes
        int offset = 0;
        int numRead;
        while (offset < bytes.length
               && (numRead=is.read(bytes, offset, bytes.length-offset)) >= 0) {
            offset += numRead;
        }

        // Ensure all the bytes have been read in
        if (offset < bytes.length) {
            throw new IOException("Could not completely read file "+file.getName());
        }

        // Close the input stream and return bytes
        is.close();
        return bytes;
    }

    /** keyword is the first meanigfull word in the first line of the file
     *
     * @param file
     * @return
     * @throws IOException
     */
    public String getKeyWord(File file) throws IOException{
        if (file.getName().endsWith("xls")) return "Excel";
        BufferedReader in = new BufferedReader(new FileReader(file));
        String str = in.readLine();
        in.close();
        if(str!=null){
            String [] result = str.split("[><\\s]",6);
            String rtn = "";
            for(String aResult : result) {
                if (aResult.length() != 0) {
                    rtn = aResult;
                    break;
                }
            }
            return rtn;
        }
        else
            return "";
    }



    public void deleteFile(String fileName){
        new File(directoryPath+"/"+fileName).delete();
    }

    public void saveFile(String name, String content) throws IOException{
        //if(getFile(name).exists()) throw new IOException("File " + name + " already exists. Choose a different name. ");
        BufferedWriter out = new BufferedWriter(new FileWriter(directoryPath+"/"+name));
        out.write(content);
        out.close();
    }

    public void saveFile(String name, byte [] content) throws IOException{
        FileOutputStream os = new FileOutputStream(new File(directoryPath+"/"+name));
        os.write(content);
    }



}
