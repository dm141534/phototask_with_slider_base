package com.daimajia.slider.demo;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.daimajia.slider.demo.adater.CommentListAdapter;
import com.daimajia.slider.demo.model.Contact;
import com.daimajia.slider.demo.util.Config;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ContactActivity extends Activity {

    private static final String TAG = ContactActivity.class.getSimpleName();
    public final static String  EXTRA_MESSAGE =  "com.christianjandl.phototask.MESSAGE" ;

    private ListView listViewContacts;
    private CommentListAdapter adapter;

    private List<Contact> contactList = new ArrayList<Contact>();
    private static final String url = Config.CONTACTS_URL;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);


        //Json-Request
        JsonArrayRequest LogReq = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());
                        // Parsing json
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject obj = response.getJSONObject(i);

                                Contact c = new Contact();
                                c.setEmailAddress(obj.getString("emailAddress"));
                                c.setCompany(obj.getString("company"));
                                c.setFirstname(obj.getString("firstname"));
                                c.setSecondname(obj.getString("secondname"));
                                // adding task to tasks array
                                contactList.add(c);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        //adapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());

            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(LogReq);
        Log.d(TAG, contactList.toString());

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_contact, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
