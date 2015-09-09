package com.daimajia.slider.demo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.preference.CheckBoxPreference;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.daimajia.slider.demo.adater.MyVolleySingleton;
import com.daimajia.slider.demo.model.Contact;
import com.daimajia.slider.demo.model.Mail;
import com.daimajia.slider.demo.model.Picture;
import com.daimajia.slider.demo.util.Config;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;



public class Mail_Activity extends Activity  {

    ImageLoader myImageLoader;
    NetworkImageView myNetworkImageView;
    private static final String TAG = Mail_Activity.class.getSimpleName();
    private ArrayList<String> Thumblink_Array = new ArrayList<String>();
    private ArrayList<Contact> contactList = new ArrayList<Contact>();
    ArrayList<String> nameList = new ArrayList<String>();
    private Button sendBtn;
    String url_pics = Config.PICS_BY_ID_URL;
    TextView txtemail, betreff, message;
    GridView gridview;
    CheckBox mycheckBox;
    //files Array für Json
    public ArrayList<String> fileList = new ArrayList<String>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mail);

        sendBtn = (Button) findViewById(R.id.button_send_mail);
        myImageLoader = MyVolleySingleton.getInstance(this).getImageLoader();
        final GridView gridView = (GridView) findViewById(R.id.gridView);

        gridView.setAdapter(new MyImageAdapter());
        //Handler für Spinner, da sonst die DropDownList nicht mehr existiert
        Handler spinnerHandler = new Handler();

        //Für EMAIL
        txtemail = (TextView) findViewById(R.id.email_address);
        betreff = (TextView) findViewById(R.id.email_about);
        message = (TextView) findViewById(R.id.email_message);
        gridview = (GridView) findViewById(R.id.gridView);
       // mycheckBox = (CheckBox) findViewById(R.id.checkbox);


        JsonArrayRequest contactRequest = new JsonArrayRequest(Config.CONTACTS_URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject obj = response.getJSONObject(i);
                        Contact con = new Contact();
                        con.setSecondname(obj.getString("secondname"));
                        con.setFirstname(obj.getString("firstname"));
                        con.setCompany(obj.getString("company"));
                        con.setEmailAddress(obj.getString("emailAddress"));
                        contactList.add(con);
                        nameList.add(con.getSecondname() + "  " + con.getFirstname());
                        //Log.d(TAG, con.getSecondname());

                    } catch (JSONException e) {
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
                    try {
                        JSONObject obj = response.getJSONObject(i);
                        Picture pic = new Picture();
                        //THUMBLINK fehlt noch in API
                        //pic.setThumb_link("http://dm141534.students.fhstp.ac.at/phototask_api/" + obj.getString("thumb_link"));
                        pic.setPic_link("http://dm141534.students.fhstp.ac.at/phototask_api/" + obj.getString("pic_link"));
                        //Log.d(TAG, pic.getPic_link());
                        Thumblink_Array.add(pic.getPic_link());
                    } catch (JSONException e) {
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
        //Spinner erstellen über Handler
        spinnerHandler.postDelayed(new AddContactSpinner(getApplicationContext(), nameList),1000 );

        sendBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                //Log.d(TAG, "Send Button gedrückt");
                Mail myMail = new Mail();
                myMail.setReceiver(txtemail.getText().toString());
                myMail.setBetreff(betreff.getText().toString());
                myMail.setText(message.getText().toString());

                myMail.setFiles(fileList);

                Log.d(TAG, myMail.getText().toString());
                Log.d(TAG, myMail.getBetreff().toString());
                Log.d(TAG, myMail.getReceiver().toString());
                Log.d(TAG,fileList.toString());

                //jetz alle Pics mit wo die checkbox gesetzt ist
                // zuerst checkbox automatisch setzen, wenn man auf das Bild klickt

                //jetz JSONPOST dann Toast und ab die Post
                // vlt nur haken auf Bild setzen, wenn bild ausgewählt ist -
                //checken ob Bild dabei ist oder nicht iein FLAG setzen

                fileList.clear();


            }
        });


    }

    static class ViewHolder {
        ImageView imageView;
        CheckBox checkBoxView;
    }

    class MyImageAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return Thumblink_Array.size();
        }

        @Override
        public Object getItem(int position) {

            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            View view = convertView;
            final ViewHolder gridViewImageHolder;

            if (view == null) {
                view = getLayoutInflater().inflate(R.layout.gridview_mail, parent, false);
                gridViewImageHolder = new ViewHolder();
                gridViewImageHolder.imageView = (ImageView) view.findViewById(R.id.networkImageView);
                gridViewImageHolder.checkBoxView = (CheckBox) view.findViewById(R.id.checkbox);
                view.setTag(gridViewImageHolder);
            } else {

                gridViewImageHolder = (ViewHolder) view.getTag();
            }
            myNetworkImageView = (NetworkImageView) gridViewImageHolder.imageView;
            myNetworkImageView.setDefaultImageResId(R.drawable.ic_launcher);
            myNetworkImageView.setErrorImageResId(R.drawable.house);
            myNetworkImageView.setAdjustViewBounds(true);
            myNetworkImageView.setImageUrl(Thumblink_Array.get(position), myImageLoader);
            myNetworkImageView.setId(position);

            //mycheckBox = (CheckBox) gridViewImageHolder.checkBoxView;
            //mycheckBox.setChecked(true);



            myNetworkImageView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    //  OnItemClicklistener für GRIDVIEW

                    //mycheckBox.setChecked(false);
                    Log.d(TAG, "Item Clicked: " + Thumblink_Array.get(position));

                    //fileList.add(Thumblink_Array.get(position).toString());

                    //überprüfen ob Pic schon ausgewählt wurde
                    if(fileList.contains(Thumblink_Array.get(position))){

                        fileList.remove(position);
                    }else{
                        fileList.add(Thumblink_Array.get(position));
                    }
                }
            });
            return view;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_mail_, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public class AddContactSpinner implements Runnable {

        private final Context mContext;
        private final ArrayList<String> nameList;

        public AddContactSpinner(Context mContext, ArrayList nameList){
            this.mContext = mContext;
            this.nameList = nameList;
        }

        public void run(){

        final Spinner contactSpinner = (Spinner) findViewById(R.id.address_spinner);
            contactSpinner.setAdapter(new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, nameList));
            contactSpinner.setOnItemSelectedListener(new CustomOnItemSelectedListener());}
    }
    public class CustomOnItemSelectedListener implements AdapterView.OnItemSelectedListener {
        public void onItemSelected(AdapterView<?> parent, View view, int pos,long id) {
            Toast.makeText(parent.getContext(),
                    "OnItemSelectedListener : " + parent.getItemAtPosition(pos).toString(),
                    Toast.LENGTH_SHORT).show();

            txtemail.setText( parent.getItemAtPosition(pos).toString());
        }

        @Override
        public void onNothingSelected(AdapterView<?> arg0) {
            // TODO Auto-generated method stub
        }

    }
}

