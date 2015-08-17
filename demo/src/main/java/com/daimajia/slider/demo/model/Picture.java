package com.daimajia.slider.demo.model;

import java.util.ArrayList;

/**
 * Created by christianjandl on 06.07.15.

 ID: "96",
 taskId: "81",
 pic_link: "pics/ori/2015/2015-1415257-PE-908C_5.png",
 thumb_link: "pics/thumb/thumb_2015/thumb_2015-1415257-PE-908C_5.png",
 made_by: null,
 is_preview: "0",
 pic_date: "1432018811"


 */

public class Picture  {

 private String pic_link, thumb_link, made_by;
 private int ID, taskId, is_preview, pic_date;

 public Picture(){}


 private Picture(String pic_link, String thumb_link, String made_by, int ID, int taskId, int is_preview, int pic_date){

  this.pic_link = pic_link;
  this.thumb_link = thumb_link;
  this.made_by = made_by;
  this.ID = ID;
  this.taskId = taskId;
  this.is_preview = is_preview;
  this.pic_date = pic_date;
 }

 public int getID() {
  return ID;
 }

 public void setID(int ID) {
  this.ID = ID;
 }

 public void setPic_link(String pic_link) {
  this.pic_link = pic_link;
 }

 public String getThumb_link() {
  return thumb_link;
 }

 public void setThumb_link(String thumb_link) {
  this.thumb_link = thumb_link;
 }

 public String getMade_by() {
  return made_by;
 }

 public void setMade_by(String made_by) {
  this.made_by = made_by;
 }

 public int getTaskId() {
  return taskId;
 }

 public void setTaskId(int taskId) {
  this.taskId = taskId;
 }

 public int getIs_preview() {
  return is_preview;
 }

 public void setIs_preview(int is_preview) {
  this.is_preview = is_preview;
 }

 public int getPic_date() {
  return pic_date;
 }

 public void setPic_date(int pic_date) {
  this.pic_date = pic_date;
 }

 public String getPic_link() {
  return pic_link;

 }


}
