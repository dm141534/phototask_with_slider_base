package com.daimajia.slider.demo;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.ImageLoader;
import org.json.JSONException;
import org.json.JSONObject;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.daimajia.slider.demo.model.Task;
import com.daimajia.slider.demo.adater.VolleyImageLoader;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DetailView extends Activity {

    private static final String TAG = DetailView.class.getSimpleName();
    // tasks json url
    private static final String url = "http://dm141534.students.fhstp.ac.at/phototask_api/api/tasks/" ;
    private static final String preview_url = "http://dm141534.students.fhstp.ac.at/phototask_api/";

    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    public static final int MEDIA_TYPE_IMAGE = 1;
    public final static String  EXTRA_MESSAGE =  "com.christianjandl.phototask.MESSAGE" ;
    //pictures are stored in folder
    private static final String IMAGE_DIRECTORY_NAME = "Phototask";
    //Uri to store image
    private Uri fileUri;
    private String taskId;
    private ImageView imgPreview;
    private Button btnCapturePicture;
    private Button btnComments;

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
        final TextView staff = (TextView) findViewById(R.id.detail_staff);
        final TextView number = (TextView) findViewById(R.id.detail_number);

        imgPreview = (ImageView) findViewById(R.id.imgPreview);
        btnCapturePicture = (Button) findViewById(R.id.add_pics);
        btnComments = (Button) findViewById(R.id.comments);
        mNetworkImageView = (NetworkImageView) findViewById(R.id.preview_image);

        Intent i = getIntent();
        final String taskId = i.getStringExtra(MainActivity.EXTRA_MESSAGE);
        //Log.d(TAG, taskId);

        mNetworkImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), Slider_MainActivity.class);
                i.putExtra(EXTRA_MESSAGE, task.getId());
                startActivity(i);

            }
        });

        btnComments.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){

                //Log.d(TAG, "Comment-Button clicked");
                Intent i = new Intent(getApplicationContext(),CommentActivity.class);
                i.putExtra(EXTRA_MESSAGE,taskId);
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
                                task.setDate(response.getString("date"));

                            // Vorschaubild auslesen und setzen
                            JSONObject previewPic = response.getJSONObject("previewPic");
                            String previewImage = preview_url + previewPic.getString("pic_link");

                            //Log.d(TAG, previewImage);
                            // Vorschaubild anzeigen
                           showPreview(previewImage);

                            name.setText(task.getName());
                            plate.setText("Kennzeichen:  " + task.getPlate());

                            long dv = Long.valueOf(task.getDate()) * 1000;
                            Date df = new java.util.Date(dv);
                            String vv = new SimpleDateFormat("MM dd,yyyy").format(df);

                            date.setText("Datum:  " + vv);
                            staff.setText("Mitarbeiter: " + task.getStaff());
                            number.setText("Auftragsnummer: " + task.getJobnumber());


                            //Log.d(TAG, task.getName());

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
/**
 * Capture image button click event
 * */
public void captureImage(View view){
    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

    fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
    intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
    //ein Versuch
    intent.putExtra(EXTRA_MESSAGE, task.getId());
    // start the image capture Intent
    startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);

}
    /**
     * Receiving activity result method will be called after closing the camera
     * */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // if the result is capturing Image
        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // successfully captured the image
                // display it in image view



                launchUploadActivity(true);
            } else if (resultCode == RESULT_CANCELED) {
                // user cancelled Image capture
                Toast.makeText(getApplicationContext(),
                        "User cancelled image capture", Toast.LENGTH_SHORT)
                        .show();
            } else {
                // failed to capture image
                Toast.makeText(getApplicationContext(),
                        "Sorry! Failed to capture image", Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }

    /*
     * Display image from a path to ImageView
     */
    private void previewCapturedImage() {
        try {
            // hide video preview

            imgPreview.setVisibility(View.VISIBLE);

            // bimatp factory
            BitmapFactory.Options options = new BitmapFactory.Options();

            // downsizing image as it throws OutOfMemory Exception for larger
            // images
            options.inSampleSize = 8;

            final Bitmap bitmap = BitmapFactory.decodeFile(fileUri.getPath(),
                    options);

            imgPreview.setImageBitmap(bitmap);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    private void launchUploadActivity(boolean isImage){
        Intent i = new Intent(DetailView.this, Upload_Activity.class);
        i.putExtra("filePath", fileUri.getPath());
        i.putExtra("isImage", isImage);
        i.putExtra("taskId", task.getId());
        startActivity(i);
    }
    /**
     * Creating file uri to store image/video
     */
    public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }
    /*
 * returning image / video
 */
    private static File getOutputMediaFile(int type) {

        // External sdcard location
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                IMAGE_DIRECTORY_NAME);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(IMAGE_DIRECTORY_NAME, "Oops! Failed create "
                        + IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "IMG_" + timeStamp + ".jpg");
        } else  {
            return null;
        }

        return mediaFile;
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
