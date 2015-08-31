package com.daimajia.slider.demo;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.daimajia.slider.demo.adater.CommentListAdapter;
import com.daimajia.slider.demo.model.Log;
import com.daimajia.slider.demo.util.Config;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommentActivity extends Activity {

    private static final String TAG = CommentActivity.class.getSimpleName();
    public final static String  EXTRA_MESSAGE =  "com.christianjandl.phototask.MESSAGE" ;

    private ListView listViewComments;
    private CommentListAdapter adapter;

    private List<Log> LogList = new ArrayList<Log>();
    private static final String url = "http://dm141534.students.fhstp.ac.at/phototask_api/api/logs/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        listViewComments = (ListView) findViewById(R.id.list_comments);
        adapter = new CommentListAdapter(this, LogList);
        listViewComments.setAdapter(adapter);

        Intent i = getIntent();
        String taskId = i.getStringExtra(DetailView.EXTRA_MESSAGE);
        final String urlWithId = url + taskId;


        // Textbox für neue Nachricht eingeben
        EditText myTextBox = (EditText) findViewById(R.id.edit_text_message);
        myTextBox.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                TextView myOutputBox = (TextView) findViewById(R.id.myOutputBox);
                myOutputBox.setText(s);
            }
        });


        //Json-Request
        JsonArrayRequest LogReq = new JsonArrayRequest(urlWithId,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        android.util.Log.d(TAG, response.toString());
                        // Parsing json
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject obj = response.getJSONObject(i);
                                Log log = new Log();
                                log.setMessage(obj.getString("message"));
                                log.setBy_staff(obj.getString("by_staff"));
                                log.setDate(obj.getString("date"));
                                // adding task to tasks array
                                LogList.add(log);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        adapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());

            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(LogReq);
        android.util.Log.d(TAG, LogList.toString());

    }

    public void saveMessageRequest(View view){
        RelativeLayout mainLayout;
        mainLayout = (RelativeLayout)findViewById(R.id.mainLayout);
        Intent i = getIntent();
        String taskId = i.getStringExtra(DetailView.EXTRA_MESSAGE);

        EditText message_box = (EditText) findViewById(R.id.edit_text_message);
        final String new_message = message_box.getText().toString();

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("taskId", taskId);
        params.put("message", new_message);
        params.put("user","Admin");

        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mainLayout.getWindowToken(), 0);

        JsonObjectRequest req = new JsonObjectRequest(Config.POST_MESSAGE_URL, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            VolleyLog.v("Response:%n %s", response.toString(4));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: ", error.getMessage());
            }
        });
        AppController.getInstance().addToRequestQueue(req);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_comment, menu);
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
