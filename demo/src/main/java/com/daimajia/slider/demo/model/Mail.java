package com.daimajia.slider.demo.model;

import java.util.ArrayList;

/**
 * Created by christianjandl on 08.09.15.
 */
public class Mail {

    private String receiver, betreff, text;
    private ArrayList files;

    public Mail() {
    }

    private Mail(String ID, String company, String emailAddress, String secondname, String firstname) {
        this.receiver = receiver;
        this.betreff = betreff;
        this.text = text;
        this.files = files;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getBetreff() {
        return betreff;
    }

    public void setBetreff(String betreff) {
        this.betreff = betreff;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public ArrayList getFiles() {
        return files;
    }

    public void setFiles(ArrayList files) {
        this.files = files;
    }
}
