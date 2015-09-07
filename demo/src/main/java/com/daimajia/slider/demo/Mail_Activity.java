package com.daimajia.slider.demo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.daimajia.slider.demo.adater.MyVolleySingleton;
import com.daimajia.slider.demo.model.Contact;
import com.daimajia.slider.demo.model.Picture;
import com.daimajia.slider.demo.util.Config;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Mail_Activity extends Activity {

    ImageLoader myImageLoader;
    NetworkImageView myNetworkImageView;
    private static final String TAG = Mail_Activity.class.getSimpleName();
    private ArrayList<String> Thumblink_Array = new ArrayList<String>();
    private ArrayList<Contact> contactList = new ArrayList<Contact>();
    ArrayList<String> nameList = new ArrayList<String>();

    String url_pics = Config.PICS_BY_ID_URL;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mail);

        myImageLoader = MyVolleySingleton.getInstance(this).getImageLoader();
            final GridView gridView = (GridView) findViewById(R.id.gridView);
            gridView.setAdapter(new MyImageAdapter());

        //JSON REQUEST for PICS ARRAY
        Intent i = getIntent();
        String taskId = i.getStringExtra(DetailView.EXTRA_MESSAGE);
        // Creating task request by id
        final String urlWithId = url_pics + taskId;
        Log.d(TAG, urlWithId);

        JsonArrayRequest picRequest = new JsonArrayRequest(urlWithId, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try
                    {
                        JSONObject obj = response.getJSONObject(i);
                        Picture pic = new Picture();
                        //THUMBLINK fehlt noch in API
                        //pic.setThumb_link("http://dm141534.students.fhstp.ac.at/phototask_api/" + obj.getString("thumb_link"));
                        pic.setPic_link("http://dm141534.students.fhstp.ac.at/phototask_api/" + obj.getString("pic_link"));
                        //Log.d(TAG, pic.getPic_link());
                        Thumblink_Array.add(pic.getPic_link());
                    }
                    catch(JSONException e){
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
            }
        });

        AppController.getInstance().addToRequestQueue(picRequest);

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
                VolleyLog.d(TAG, "Error: " + error.getMessage());
            }
        });
        AppController.getInstance().addToRequestQueue(contactRequest);
        //SPINNNER
        Spinner contactSpinner = (Spinner) findViewById(R.id.address_spinner);
        contactSpinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, nameList));

        //Spinner onitemclclicked
       contactSpinner
                .setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) {
                        TextView txt_email = (TextView) findViewById(R.id.email_address);
                        txt_email.setText("An: " + contactList.get(position).getEmailAddress());
                        Log.d(TAG, "Item selected" + position);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {
                        // TODO Auto-generated method stub
                    }
                });
    }

    static class ViewHolder{
        ImageView imageView;
    }

    class MyImageAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return Thumblink_Array.size();}

        @Override
        public Object getItem(int position) {
            return null;}

        @Override
        public long getItemId(int position) {
            return position;}

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View view = convertView;
            final ViewHolder gridViewImageHolder;

            if (view == null) {
                view = getLayoutInflater().inflate(R.layout.gridview_mail, parent, false);
                gridViewImageHolder = new ViewHolder();
                gridViewImageHolder.imageView = (ImageView) view.findViewById(R.id.networkImageView);
                view.setTag(gridViewImageHolder);
            } else {
                gridViewImageHolder = (ViewHolder) view.getTag();
            }
            myNetworkImageView = (NetworkImageView) gridViewImageHolder.imageView;
            myNetworkImageView.setDefaultImageResId(R.drawable.ic_launcher);
            myNetworkImageView.setErrorImageResId(R.drawable.house);
            myNetworkImageView.setAdjustViewBounds(true);
            myNetworkImageView.setImageUrl(Thumblink_Array.get(position), myImageLoader);
            return view;
        }
    }
        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.menu_mail_, menu);
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

