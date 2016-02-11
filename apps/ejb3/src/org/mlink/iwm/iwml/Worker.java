package org.mlink.iwm.iwml;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: andreipovodyrev
 * Date: May 23, 2007
 */
public class Worker {
    private String email;
    private String fname;
    private String lname;
    private Float billingRate;
    private String type;
    private String organization;
    private List <Skill> skills;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public Float getBillingRate() {
        return billingRate;
    }

    public void setBillingRate(Float billingRate) {
        this.billingRate = billingRate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public List<Skill> getSkills() {
        return skills;
    }

    public void setSkills(List<Skill> skills) {
        this.skills = skills;
    }


}