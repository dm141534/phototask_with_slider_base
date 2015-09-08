package com.daimajia.slider.demo.util;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.daimajia.slider.demo.AppController;
import com.daimajia.slider.demo.model.Contact;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by christianjandl on 08.09.15.
 */
public class ContactListForSpinner {

    private ArrayList<Contact> contactList = new ArrayList<Contact>();
    ArrayList<String> nameList = new ArrayList<String>();

    JsonArrayRequest contactRequest = new JsonArrayRequest(Config.CONTACTS_URL, new Response.Listener<JSONArray>() {
        @Override
        public void onResponse(JSONArray response) {
            for (int i = 0; i < response.length(); i++) {
                try
                {
                    JSONObject obj = response.getJSONObject(i);
                    Contact con = new Contact();
                    con.setSecondname(obj.getString("secondname"));
                    con.setFirstname(obj.getString("firstname"));
                    con.setCompany(obj.getString("company"));
                    con.setEmailAddress(obj.getString("emailAddress"));
                    contactList.add(con);
                    nameList.add(con.getSecondname() + "  " + con.getFirstname());
                    //Log.d(TAG, con.getSecondname());
                }
                catch(JSONException e){
                    e.printStackTrace();
                }
            }
        }
    }, new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            //VolleyLog.d(TAG, "Error: " + error.getMessage());
        }
    });
    //AppController.getInstance().addToRequestQueue(contactRequest);


}
