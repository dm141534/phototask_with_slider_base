package com.daimajia.slider.demo;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.daimajia.slider.demo.util.Config;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class NewContactActivity extends Activity {

    private static final String TAG = NewContactActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_contact);
    }

    public void saveNewContactRequest(View view){
        LinearLayout newTaskLayout;
        newTaskLayout = (LinearLayout)findViewById(R.id.new_contact_layout);

        EditText newSecondName = (EditText) findViewById(R.id.new_secondname);
        EditText newFirstName = (EditText) findViewById(R.id.new_firstname);
        EditText newEmail = (EditText) findViewById(R.id.new_email);
        EditText newCompany = (EditText) findViewById(R.id.new_company);

        final String new_secondname = newSecondName.getText().toString();
        final String new_firstname = newFirstName.getText().toString();
        final String new_email = newEmail.getText().toString();
        final String new_company = newCompany.getText().toString();

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("secondname", new_secondname);
        params.put("firstname",new_firstname);
        params.put("emailAddress", new_email);
        params.put("company", new_company);

        Log.d(TAG, params.toString());

        //Keyboard appears only if on Focus
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(newTaskLayout.getWindowToken(), 0);

        JsonObjectRequest req = new JsonObjectRequest(Config.POST_NEW_CONTACT_URL, new JSONObject(params),
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
        Toast.makeText(NewContactActivity.this, "Neuer Kontakt " + new_secondname + " wurde erstellt!", Toast.LENGTH_SHORT).show();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_new_contact, menu);
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
}
