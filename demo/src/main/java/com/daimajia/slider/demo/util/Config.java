package com.daimajia.slider.demo.util;

import java.util.Arrays;
import java.util.List;

/**
 * Created by christianjandl on 24.08.15.
 */
public class Config {


        // File upload url (replace the ip with your server address)
        public static final String FILE_UPLOAD_URL = "http://dm141534.students.fhstp.ac.at/phototask_api/php/upload_android.php";
        public static final String FILE_UPLOAD_TABLE_URL = "http://dm141534.students.fhstp.ac.at/phototask_api/php/upload.php";

        // Directory name to store captured images and videos
        public static final String IMAGE_DIRECTORY_NAME = "Android File Upload";
        // URL-JSON Ressources
        public static final String POST_MESSAGE_URL = "http://dm141534.students.fhstp.ac.at/phototask_api/api/logentry";
        public static final String CONTACTS_URL = "http://dm141534.students.fhstp.ac.at/phototask_api/api/contacts";
        public static final String POST_NEW_TASK_URL = "http://dm141534.students.fhstp.ac.at/phototask_api/api/newtask";
        public static final String PICS_BY_ID_URL = "http://dm141534.students.fhstp.ac.at/phototask_api/api/pics/";


        // VALUES for Grid View
        public static final int NUM_OF_COLUMNS = 3;
        public static final int GRID_PADDING = 8; // in dp
        public static final String PHOTO_ALBUM = "androidhive";
        public static final List<String> FILE_EXTN = Arrays.asList("jpg", "jpeg", "png");
    }

