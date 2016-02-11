package org.mlink.iwm.bean;

import org.apache.log4j.Logger;
import org.mlink.iwm.util.CopyUtils;

import java.util.StringTokenizer;
import java.util.Map;

/**
 * User: andrei
 * Date: Oct 21, 2006
 */
public class ServicePlan {
    private static final Logger logger = Logger.getLogger(ServicePlan.class);

    //06/22/05 two last numbers in the string are dummy and here to support v1 data conversion
    private static final String defPlanString     ="1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,0,0,0,0";
    private static final String emptyPlanString   ="0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0";

    private boolean spring;
    private boolean summer;
    private boolean fall;
    private boolean winter;
    private boolean january;
    private boolean february;
    private boolean march;
    private boolean april;
    private boolean may;
    private boolean june;
    private boolean july;
    private boolean august;
    private boolean september;
    private boolean october;
    private boolean november;
    private boolean december;
    private boolean monday;
    private boolean tuesday;
    private boolean wednesday;
    private boolean thursday;
    private boolean friday;
    private boolean saturday;
    private boolean sunday;


    private String rangeStartDate;
    private String rangeEndDate;
    private String id;

    public ServicePlan() {
    }

    public ServicePlan(boolean setDefault) {
        if(setDefault) decodePlanData(ServicePlan.defPlanString);
    }

    public ServicePlan(Map map) throws Exception{
        CopyUtils.copyProperties(this,map);
    }

    public boolean isValid(){
        return !emptyPlanString.equals(codePlanData());
    }

    public String getRangeStartDate() {
        return rangeStartDate;
    }

    public void setRangeStartDate(String rangeStartDate) {
        this.rangeStartDate = rangeStartDate;
    }

    public String getRangeEndDate() {
        return rangeEndDate;
    }

    public void setRangeEndDate(String rangeEndDate) {
        this.rangeEndDate = rangeEndDate;
    }

    public boolean isSpring() {
        return spring;
    }

    public void setSpring(boolean spring) {
        this.spring = spring;
    }

    public boolean isSummer() {
        return summer;
    }

    public void setSummer(boolean summer) {
        this.summer = summer;
    }

    public boolean isFall() {
        return fall;
    }

    public void setFall(boolean fall) {
        this.fall = fall;
    }

    public boolean isWinter() {
        return winter;
    }

    public void setWinter(boolean winter) {
        this.winter = winter;
    }

    public boolean isJanuary() {
        return january;
    }

    public void setJanuary(boolean january) {
        this.january = january;
    }

    public boolean isFebruary() {
        return february;
    }

    public void setFebruary(boolean february) {
        this.february = february;
    }

    public boolean isMarch() {
        return march;
    }

    public void setMarch(boolean march) {
        this.march = march;
    }

    public boolean isApril() {
        return april;
    }

    public void setApril(boolean april) {
        this.april = april;
    }

    public boolean isMay() {
        return may;
    }

    public void setMay(boolean may) {
        this.may = may;
    }

    public boolean isJune() {
        return june;
    }

    public void setJune(boolean june) {
        this.june = june;
    }

    public boolean isJuly() {
        return july;
    }

    public void setJuly(boolean july) {
        this.july = july;
    }

    public boolean isAugust() {
        return august;
    }

    public void setAugust(boolean august) {
        this.august = august;
    }

    public boolean isSeptember() {
        return september;
    }

    public void setSeptember(boolean september) {
        this.september = september;
    }

    public boolean isOctober() {
        return october;
    }

    public void setOctober(boolean october) {
        this.october = october;
    }

    public boolean isNovember() {
        return november;
    }

    public void setNovember(boolean november) {
        this.november = november;
    }

    public boolean isDecember() {
        return december;
    }

    public void setDecember(boolean december) {
        this.december = december;
    }

    public boolean isMonday() {
        return monday;
    }

    public void setMonday(boolean monday) {
        this.monday = monday;
    }

    public boolean isTuesday() {
        return tuesday;
    }

    public void setTuesday(boolean tuesday) {
        this.tuesday = tuesday;
    }

    public boolean isWednesday() {
        return wednesday;
    }

    public void setWednesday(boolean wednesday) {
        this.wednesday = wednesday;
    }

    public boolean isThursday() {
        return thursday;
    }

    public void setThursday(boolean thursday) {
        this.thursday = thursday;
    }

    public boolean isFriday() {
        return friday;
    }

    public void setFriday(boolean friday) {
        this.friday = friday;
    }

    public boolean isSaturday() {
        return saturday;
    }

    public void setSaturday(boolean saturday) {
        this.saturday = saturday;
    }

    public boolean isSunday() {
        return sunday;
    }

    public void setSunday(boolean sunday) {
        this.sunday = sunday;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public void decodePlanData(String str){
        if(str==null) return;
        StringTokenizer st = new StringTokenizer(str,",");
        if(st.countTokens() == 25){
            spring=parseRadio((String)st.nextElement());
            summer=parseRadio((String)st.nextElement());
            fall=parseRadio((String)st.nextElement());
            winter=parseRadio((String)st.nextElement());
            january=parseRadio((String)st.nextElement());
            february=parseRadio((String)st.nextElement());
            march=parseRadio((String)st.nextElement());
            april=parseRadio((String)st.nextElement());
            may=parseRadio((String)st.nextElement());
            june=parseRadio((String)st.nextElement());
            july=parseRadio((String)st.nextElement());
            august=parseRadio((String)st.nextElement());
            september=parseRadio((String)st.nextElement());
            october=parseRadio((String)st.nextElement());
            november=parseRadio((String)st.nextElement());
            december=parseRadio((String)st.nextElement());
            monday=parseRadio((String)st.nextElement());
            tuesday=parseRadio((String)st.nextElement());
            wednesday=parseRadio((String)st.nextElement());
            thursday=parseRadio((String)st.nextElement());
            friday=parseRadio((String)st.nextElement());
            saturday=parseRadio((String)st.nextElement());
            sunday=parseRadio((String)st.nextElement());
            //dummy
            st.nextElement();st.nextElement();
        }else{
            logger.debug("invalid ServicePlan Data format");
        }
    }

    public String codePlanData(){
        StringBuilder sb = new StringBuilder();
        sb.append(parseRadio(spring)).append(",");
        sb.append(parseRadio(summer)).append(",");
        sb.append(parseRadio(fall)).append(",");
        sb.append(parseRadio(winter)).append(",");
        sb.append(parseRadio(january)).append(",");
        sb.append(parseRadio(february)).append(",");
        sb.append(parseRadio(march)).append(",");
        sb.append(parseRadio(april)).append(",");
        sb.append(parseRadio(may)).append(",");
        sb.append(parseRadio(june)).append(",");
        sb.append(parseRadio(july)).append(",");
        sb.append(parseRadio(august)).append(",");
        sb.append(parseRadio(september)).append(",");
        sb.append(parseRadio(october)).append(",");
        sb.append(parseRadio(november)).append(",");
        sb.append(parseRadio(december)).append(",");
        sb.append(parseRadio(monday)).append(",");
        sb.append(parseRadio(tuesday)).append(",");
        sb.append(parseRadio(wednesday)).append(",");
        sb.append(parseRadio(thursday)).append(",");
        sb.append(parseRadio(friday)).append(",");
        sb.append(parseRadio(saturday)).append(",");
        sb.append(parseRadio(sunday)).append(",");
        //dummy
        sb.append("0"+ ",");
        sb.append("0");
        return sb.toString();
    }
    private String parseRadio(boolean value){
        return value?"1":"0";
    }
    private boolean parseRadio(String value){
        return "1".equals(value);
    }
}

