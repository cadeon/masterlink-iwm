package org.mlink.iwm.util;

import junit.framework.Test;
import junit.framework.TestSuite;
import junit.framework.TestCase;

import java.io.File;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.FileReader;
import java.util.List;
import java.util.Iterator;

/**
 * FileAccess Tester.
 *
 * @author <Authors name>
 * @since <pre>04/25/2007</pre>
 * @version 1.0
 */
public class FileAccessTest extends TestCase {
    public FileAccessTest(String name) {
        super(name);
    }

    public void setUp() throws Exception {
        super.setUp();
    }

    public void tearDown() throws Exception {
        super.tearDown();
    }

    public void testGetFiles() throws Exception {
        FileAccess fa = new FileAccess(".");
        System.out.println(new File(".").getCanonicalPath());
        List <File> files = fa.getFiles();
        for (Iterator<File> iterator = files.iterator(); iterator.hasNext();) {
            File file =  iterator.next();
            System.out.println(file.getName() + " " +file.lastModified());

        }
    }

    public void testGetFiles2() throws Exception {
        FileAccess fa = new FileAccess(".");
        System.out.println(new File(".").getCanonicalPath());
        System.out.println("Paginated output, sort by name asc");
        List <File> files = fa.getFiles(2,2,"name","ASC");
        for (Iterator<File> iterator = files.iterator(); iterator.hasNext();) {
            File file =  iterator.next();
            System.out.println(file.getName() + " " +file.lastModified());
        }

        System.out.println("Paginated output, sort by name desc");
        files = fa.getFiles(2,2,"name","DESC");
        for (Iterator<File> iterator = files.iterator(); iterator.hasNext();) {
            File file =  iterator.next();
            System.out.println(file.getName() + " " +file.lastModified());
        }

        System.out.println("Paginated output, sort by lastModified asc");
        files = fa.getFiles(2,2,"lastModified","ASC");
        for (Iterator<File> iterator = files.iterator(); iterator.hasNext();) {
            File file =  iterator.next();
            System.out.println(file.getName() + " " +file.lastModified());
        }

        System.out.println("Paginated output, sort by lastModified desc");
        files = fa.getFiles(2,2,"lastModified","DESC");
        for (Iterator<File> iterator = files.iterator(); iterator.hasNext();) {
            File file =  iterator.next();
            System.out.println(file.getName() + " " +file.lastModified());
        }
    }

    public void testGetFile() throws Exception {
        FileAccess fa = new FileAccess(".");
        File file = fa.getFile("iwmv35.xml");
        try {
            BufferedReader in = new BufferedReader(new FileReader(file));
            String str;
            while ((str = in.readLine()) != null) {
                System.out.println(str);
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void testGetKeyWord() throws Exception {
        String s1 = "<abc> <adfasf>";
        String [] result = s1.split("[><\\s]",6);
        for (int x=0; x<result.length; x++)
            System.out.println(x + " : " + result[x]);    }

    public void testSaveFile() throws Exception {
        FileAccess fa = new FileAccess("C:/jboss-4.0.4.GA/server/default/iwmupload");
        fa.saveFile("andrei","andrei");
    }

    public void testDeleteFile() throws Exception {
        FileAccess fa = new FileAccess(".");
        fa.deleteFile("andrei");
    }

    public static Test suite() {
        return new TestSuite(FileAccessTest.class);
    }
}
