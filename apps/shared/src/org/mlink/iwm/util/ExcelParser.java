package org.mlink.iwm.util;

import java.io.File;
import java.io.InputStream;
import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;

import jxl.Workbook;
import jxl.Sheet;
import jxl.Cell;
import jxl.demo.XML;
import org.apache.log4j.Logger;

/**
 * User: andreipovodyrev
 * Date: Dec 4, 2007
 */
public class ExcelParser {
    protected static final Logger logger = Logger.getLogger(ExcelParser.class);
    protected Workbook workbook;

    public ExcelParser(File excelFile) throws Exception{
        workbook = Workbook.getWorkbook(excelFile);
    }

    public String echo() throws Exception{
        return printWorkBook();

    }

    public ExcelParser (InputStream is) throws Exception{
        workbook = Workbook.getWorkbook(is);
    }

    private String printWorkBook() {
        StringBuilder sb = new StringBuilder("Excel file echo.\n");
        Sheet [] sheets = workbook.getSheets();
        for (Sheet sheet : sheets) {
            sb.append("Sheet:" + sheet.getName());
            sb.append("\n");
            //digest(sheet);
            int rowNumber = sheet.getRows();
            for (int j = 0; j < rowNumber; j++) {
                Cell [] cells = sheet.getRow(j);
                for (Cell cell : cells) {
                    sb.append(cell.getContents()).append("\t");
                }
                sb.append("\n");
            }
        }
        return sb.toString();
    }


    public String toXML() throws Exception{
        //ByteArrayOutputStream out = new ByteArrayOutputStream();
        XML xml = new XML(workbook,System.out,"UTF8",false);
        //out.toByteArray().toString();
        //out.close();
        return null;
    }

    protected void finalize() throws Throwable {
        super.finalize();
        workbook.close();
    }


}
