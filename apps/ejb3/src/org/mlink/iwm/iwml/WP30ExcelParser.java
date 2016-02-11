package org.mlink.iwm.iwml;

import java.io.File;
import java.io.StringWriter;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.bind.JAXBException;

import jxl.Cell;
import jxl.Sheet;

import org.mlink.iwm.util.ExcelParser;
import org.mlink.sitar.demo.MeasureType;
import org.mlink.sitar.demo.MeasuresType;
import org.mlink.sitar.demo.ReadinessType;
import org.mlink.sitar.demo.SitarType;
import org.mlink.sitar.demo.UnitType;

/**
 * User: andreipovodyrev
 * this file is a parser for excel files of the
 * ./iwm_v35/apps/ejb3/src/org/mlink/iwm/iwml/VP30 Data Table 17Dec07b-noblankrows.xls
 * format
 * Date: Dec 15, 2007
 */
public class WP30ExcelParser extends ExcelParser {
    private Sheet sheet;
    private SitarType sitar;
    private final int ORG_NAME_COL=3;
    final int ORG_FIRST_ROW_NUMBER=12;
    final int AFT_OFFSET_FROM_ORG_NAME=3;
    final static NumberFormat nf = NumberFormat.getPercentInstance();
    String fileName;

    public WP30ExcelParser(File excelFile) throws Exception {
        super(excelFile);
        this.sheet = workbook.getSheet(0);
        this.fileName=excelFile.getName();
    }


    public String toXML() {
        logger.debug("Sheet:" + sheet.getName());
        if(sitar==null) sitar = digest();
        StringWriter sw = new StringWriter();
        try {
            javax.xml.bind.JAXBContext jaxbContext = javax.xml.bind.JAXBContext.newInstance("org.mlink.sitar.demo");
            // create an object to marshal
            // create a Marshaller and do marshal
            javax.xml.bind.Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(javax.xml.bind.Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            marshaller.marshal(sitar, sw);
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return sw.toString();
    }

    public SitarType digest() {
        List <Organization> orgs = getOrganizations();
        SitarType sitar = new SitarType();
        sitar.setAction("Sitar Demo message");

        //there might be a date embedded in the filename like VP30_02282008_blabla.... Check if it is there and use it
        SimpleDateFormat fmt = new SimpleDateFormat("MMddyyyy");
        try {
            Date date = fmt.parse(fileName.substring(5,13));
            sitar.setGeneratedTimeMsecs(date.getTime());

        } catch (ParseException e) {
            //just use today's date
            sitar.setGeneratedTimeMsecs(System.currentTimeMillis());
        }

        for (Organization organization : orgs) {
            if (organization.getParent().length() == 0) {
                sitar.getUnits().add(organization.getUnit());
            }
        }
        return sitar;
    }


    /**
     * @return cells
     */
    private List <Organization> getOrganizations(){
        List <Organization> orgs = new ArrayList<Organization>();
        Cell [] orgCells =  sheet.getColumn(ORG_NAME_COL);
        for (int i = ORG_FIRST_ROW_NUMBER; i < orgCells.length; i++) {
            Cell orgCell = orgCells[i];
            if (orgCell.getContents().length() != 0) {
                orgs.add(new Organization(orgCell));
            }
        }
        for (int i = 0; i < orgs.size(); i++) {
            Organization org =  orgs.get(i);
            if(org.getParent()!=null || org.getParent().length()!=0){
                for (Organization org1 : orgs) {
                    if (org.getParent().equals(org1.getName())) {
                        org1.getUnit().getSubUnit().add(org.getUnit());
                        break;
                    }
                }
            }
        }
        return orgs;
    }




    class Organization{
        Cell cell;
        UnitType unit = new UnitType();
        int rowSize;

        public Organization(Cell cell) {
            this.cell = cell;
            unit.setName(getName());
            unit.setType(sheet.getCell(cell.getColumn()+1,cell.getRow()).getContents());
            rowSize= sheet.getRow(cell.getRow()).length;
            Cell [] cells = sheet.getRow(cell.getRow());

            MeasuresType aft = new MeasuresType();
            int cursor=cell.getColumn()+AFT_OFFSET_FROM_ORG_NAME;

            for (int i = cursor; i < rowSize ; i++,cursor++) {
                String measureType=findMeasureType(cells[i]);
                if("Certifications".equals(measureType)){
                    break;
                }
                MeasureType measure = new MeasureType();
                measure.setType(measureType);
                measure.setValue(findMeasureValue(cells[i]));
                aft.getMeasures().add(measure);
            }

            MeasuresType certs = new MeasuresType();
            cursor++;
            certs.setAverageValue(findMeasureValue(cells[cursor]));
            for (int i = cursor; i < rowSize ; i++,cursor++) {
                String measureType=findMeasureType(cells[i]);
                if("Qualifications".equals(measureType)){
                    break;
                }
                MeasureType measure = new MeasureType();
                measure.setType(findMeasureType(cells[i]));
                measure.setValue(findMeasureValue(cells[i]));
                certs.getMeasures().add(measure);
            }

            MeasuresType quals = new MeasuresType();
            cursor++;
            quals.setAverageValue(findMeasureValue(cells[cursor]));
            for (int i = cursor; i < rowSize ; i++,cursor++) {
                String measureType=findMeasureType(cells[i]);
                if("Licensing".equals(measureType)){
                    break;
                }
                MeasureType measure = new MeasureType();
                measure.setType(findMeasureType(cells[i]));
                measure.setValue(findMeasureValue(cells[i]));
                quals.getMeasures().add(measure);
            }

            MeasuresType licenses = new MeasuresType();
            cursor++;
            licenses.setAverageValue(findMeasureValue(cells[cursor]));
            for (int i = cursor; i < rowSize ; i++,cursor++) {
                String measureType=findMeasureType(cells[i]);
                if("QFTF".equals(measureType)){
                    break;
                }
                MeasureType measure = new MeasureType();
                measure.setType(findMeasureType(cells[i]));
                measure.setValue(findMeasureValue(cells[i]));
                licenses.getMeasures().add(measure);
            }

            MeasuresType qft = new MeasuresType();
            for (int i = cursor; i < rowSize ; i++,cursor++) {
                String measureType=findMeasureType(cells[i]);
                if("IRFT".equals(measureType)){
                    break;
                }
                MeasureType measure = new MeasureType();
                measure.setType(findMeasureType(cells[i]));
                measure.setValue(findMeasureValue(cells[i]));
                qft.getMeasures().add(measure);
            }

            MeasuresType rft = new MeasuresType();
            for (int i = cursor; i < rowSize ; i++,cursor++) {
                String measureType=findMeasureType(cells[i]);
                if("".equals(measureType)){
                    break;
                }
                MeasureType measure = new MeasureType();
                measure.setType(findMeasureType(cells[i]));
                measure.setValue(findMeasureValue(cells[i]));
                rft.getMeasures().add(measure);
            }

            ReadinessType readiness = new ReadinessType();
            readiness.setAvailableForTasking(aft);
            readiness.setQualifiedForTasking(qft);
            readiness.setReadyForTasking(rft);
            readiness.setCertifications(certs);
            readiness.setQualifications(quals);
            readiness.setLicences(licenses);
            unit.setReadiness(readiness);
        }

        public Cell getCell() {
            return cell;
        }
        public String getName(){
            return cell.getContents();
        }

        public String getParent(){
            return sheet.getCell(cell.getColumn()+2,cell.getRow()).getContents();
        }

        public UnitType getUnit() {
            return unit;
        }


        private String findMeasureType(Cell cell){
            Cell typeCell=cell;
            for (int i = ORG_FIRST_ROW_NUMBER-1; i >=0 ; i--) {
                typeCell = sheet.getCell(cell.getColumn(),i);
                if(typeCell.getContents().length()>0){
                    break;
                }
            }
            return typeCell.getContents();
        }

        private double findMeasureValue(Cell cell) {
            double rtn=0;
            try{
                rtn = nf.parse(cell.getContents()).doubleValue();
            }catch(ParseException e){
                e.printStackTrace();
            }
            return rtn;
        }
    }
}
