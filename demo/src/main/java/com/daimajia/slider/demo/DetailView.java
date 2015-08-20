package com.daimajia.slider.demo;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.ImageLoader;
import org.json.JSONException;
import org.json.JSONObject;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.daimajia.slider.demo.R;
import com.daimajia.slider.demo.AppController;
import com.daimajia.slider.demo.model.Task;
import com.daimajia.slider.demo.adater.VolleyImageLoader;

public class DetailView extends Activity {

    private static final String TAG = DetailView.class.getSimpleName();
    public final static String  EXTRA_MESSAGE =  "com.christianjandl.phototask.MESSAGE" ;
    // tasks json url
    private static final String url = "http://dm141534.students.fhstp.ac.at/phototask_api/api/tasks/" ;
    private static final String preview_url = "http://dm141534.students.fhstp.ac.at/phototask_api/";


    Task task = new Task();

    //Vorschaubild
    private NetworkImageView mNetworkImageView;
    private ImageLoader mImageLoader;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_view);
        pDialog = new ProgressDialog(this);

        final TextView name = (TextView) findViewById(R.id.detailname);
        final TextView plate = (TextView) findViewById(R.id.detail_plate);
        final TextView date = (TextView) findViewById(R.id.detail_date);

        mNetworkImageView = (NetworkImageView) findViewById(R.id.preview_image);

        Intent i = getIntent();
        String taskId = i.getStringExtra(MainActivity.EXTRA_MESSAGE);
        Log.d(TAG, taskId);

        mNetworkImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), Slider_MainActivity.class);
                i.putExtra(EXTRA_MESSAGE, task.getId());
                startActivity(i);

            }
        });

        // Showing progress dialog before making http request
       pDialog.setMessage("Bild wird geladen...");
       pDialog.show();

        // Creating task request by id
        final String urlWithId = url + taskId;
        JsonObjectRequest taskRequest = new JsonObjectRequest(Request.Method.GET, urlWithId,null,

                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());

                        // Parsing json
                        pDialog.hide();
                        try {

                                task.setName(response.getString("name"));
                                task.setPlate(response.getString("plate"));
                                task.setStaff(response.getString("staff"));
                                task.setJobnumber(response.getString("jobnumber"));
                                task.setId(response.getString("id"));
                                task.setDate(response.getInt("date"));

                            // Vorschaubild auslesen und setzen
                            JSONObject previewPic = response.getJSONObject("previewPic");
                            String previewImage = preview_url + previewPic.getString("pic_link");

                            Log.d(TAG, previewImage);
                            // Vorschaubild anzeigen
                           showPreview(previewImage);

                            name.setText(task.getName());
                            plate.setText(task.getPlate());
                            //date.setText(task.getDate());
                            Log.d(TAG, task.getName());

                        } catch (JSONException e) {
                                e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {



            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                pDialog.hide();
            }
        });
        AppController.getInstance().addToRequestQueue(taskRequest);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detail_view, menu);
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

    protected void showPreview(String previewImage) {
        //Load Preview Image and
        mImageLoader = VolleyImageLoader.getInstance(this.getApplicationContext()).getImageLoader();

        final String url = previewImage;
        //Log.d(TAG, url);
        mImageLoader.get(url, ImageLoader.getImageListener(mNetworkImageView,
                R.drawable.ic_launcher, android.R.drawable
                        .ic_dialog_alert));
        mNetworkImageView.setImageUrl(url, mImageLoader);
    }
}
