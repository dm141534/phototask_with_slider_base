package com.daimajia.slider.demo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.daimajia.slider.demo.util.Config;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class NewTaskActivity extends Activity {

    private static final String TAG = NewTaskActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_new_task, menu);
        return true;

    }

    public void saveNewTaskRequest(View view){
        LinearLayout newTaskLayout;
        newTaskLayout = (LinearLayout)findViewById(R.id.new_task_layout);
        //Intent i = getIntent();
        //String taskId = i.getStringExtra(DetailView.EXTRA_MESSAGE);

        EditText newName = (EditText) findViewById(R.id.new_name);
        EditText newPlate = (EditText) findViewById(R.id.new_plate);
        EditText newJobNumber = (EditText) findViewById(R.id.new_jobnumber);
        EditText newStaff = (EditText) findViewById(R.id.new_staff);
        EditText newMessage = (EditText) findViewById(R.id.new_comment);

        final String new_name = newName.getText().toString();
        final String new_plate = newPlate.getText().toString();
        final String new_jobnumber = newJobNumber.getText().toString();
        final String new_staff = newStaff.getText().toString();
        final String new_message = newMessage.getText().toString();

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("name", new_name);
        params.put("plate",new_plate);
        params.put("staff", new_staff);
        params.put("logmessage", new_message);
        params.put("jobnumber", new_jobnumber);

        Log.d(TAG, params.toString());

        //Keyboard appears only if on Focus
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(newTaskLayout.getWindowToken(), 0);

        JsonObjectRequest req = new JsonObjectRequest(Config.POST_NEW_TASK_URL, new JSONObject(params),
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
        Log.d(TAG, "POST send !");
        AppController.getInstance().addToRequestQueue(req);
        //Toast
        Toast.makeText(NewTaskActivity.this, "Auftrag" + new_name +"wurde erstellt!", Toast.LENGTH_SHORT).show();
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
