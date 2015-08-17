package com.daimajia.slider.demo.model;

/**
 * Created by christianjandl on 06.07.15.
 *
 *
 * {
 taskId: "81",
 ID: "160",
 message: "jkhjkhkjjkhk",
 by_staff: "Admin",
 date: "1432045349"


 */
public class Log {

    private String message, by_staff;
    private int taskId, ID, date;


    public Log(){}



    private Log(String message, String by_staff,  int taskId, int ID, int date) {
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

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }
}

