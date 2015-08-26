package com.daimajia.slider.demo.model;

/**
 * Created by christianjandl on 06.07.15.
 *
 *
*/
public class Log {

    private String message, by_staff, date;
    private int taskId, ID;


    public Log(){}



    private Log(String message, String by_staff,  int taskId, int ID, String date) {
        this.message = message;
        this.by_staff= by_staff;
        this. taskId= taskId;
        this.ID = ID;
        this.date = date;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getBy_staff() {
        return by_staff;
    }

    public void setBy_staff(String by_staff) {
        this.by_staff = by_staff;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}

