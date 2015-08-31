package com.daimajia.slider.demo.model;

/**
 * Created by christianjandl on 31.08.15.
 */
public class Contact {


    private String ID, company, emailAddress, secondname, firstname;


    public Contact() {
    }

    ;


    private Contact(String ID, String company, String emailAddress, String secondname, String firstname) {

        this.ID = ID;
        this.company = company;
        this.emailAddress = emailAddress;
        this.secondname = secondname;
        this.firstname = firstname;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getEmailAddress() {return emailAddress;}

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getSecondname() {
        return secondname;
    }

    public void setSecondname(String secondname) {
        this.secondname = secondname;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }
}



