package com.daimajia.slider.demo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.daimajia.slider.demo.model.Picture;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class Slider_MainActivity extends Activity implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener{

    private SliderLayout mySlider;
    private static final String url = "http://dm141534.students.fhstp.ac.at/phototask_api/api/pics/";
    private static final String TAG = Slider_MainActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_slider);
        mySlider = (SliderLayout)findViewById(R.id.slider);

        //get INtent Extra (ID)
        Intent i = getIntent();
        String taskId = i.getStringExtra(DetailView.EXTRA_MESSAGE);

        // Creating task request by id
        final String urlWithId = url + taskId;
       // Log.d(TAG,urlWithId);

        // Creating volley request obj
        JsonArrayRequest picRequest = new JsonArrayRequest(urlWithId, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                // Log.d(TAG, response.toString());

                // Parsing json
                for (int i = 0; i < response.length(); i++) {
                    try
                    {
                        JSONObject obj = response.getJSONObject(i);
                        Picture pic = new Picture();
                        pic.setPic_link("http://dm141534.students.fhstp.ac.at/phototask_api/" + obj.getString("pic_link"));

                        TextSliderView textSliderView = new TextSliderView(getApplicationContext());
                        textSliderView
                                .description(pic.getPic_link())
                                .image(pic.getPic_link())
                                .setScaleType(BaseSliderView.ScaleType.CenterCrop);

                        //add your extra information
                        textSliderView.bundle(new Bundle());
                        textSliderView.getBundle();

                        mySlider.addSlider(textSliderView);

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

        /* important adds Request */
        AppController.getInstance().addToRequestQueue(picRequest);
        //Slider erstellen
        mySlider.setPresetTransformer(SliderLayout.Transformer.Default);
        mySlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Top);
        mySlider.setCustomAnimation(new DescriptionAnimation());
        mySlider.setDuration(10000);
        mySlider.addOnPageChangeListener(this);

    }

    @Override
    protected void onStop() {
        // To prevent a memory leak on rotation, make sure to call stopAutoCycle() on the slider before activity or fragment is destroyed
        mySlider.stopAutoCycle();
        super.onStop();
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {
        Toast.makeText(this,slider.getBundle().get("extra") + "",Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

    @Override
    public void onPageSelected(int position) {
        Log.d("Slider Demo", "Page Changed: " + position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {}
}
