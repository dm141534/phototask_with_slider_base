package com.daimajia.slider.demo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.daimajia.slider.demo.adater.CommentListAdapter;
import com.daimajia.slider.demo.model.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

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

                        // notifying list adapter about data changes
                        // so that it renders the list view with updated data
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
