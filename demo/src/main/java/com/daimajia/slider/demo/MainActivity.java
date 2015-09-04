package com.daimajia.slider.demo;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.daimajia.slider.demo.R;
import com.daimajia.slider.demo.adater.CustomListAdapter;
import com.daimajia.slider.demo.AppController;
import com.daimajia.slider.demo.model.Picture;
import com.daimajia.slider.demo.model.Task;

import junit.framework.Test;

public class MainActivity extends Activity {

	private static final String TAG = MainActivity.class.getSimpleName();
	public final static String  EXTRA_MESSAGE =  "com.christianjandl.phototask.MESSAGE" ;

	// tasks json url
	private static final String url = "http://dm141534.students.fhstp.ac.at/phototask_api/api/tasks";
	private ProgressDialog pDialog;
	private List<Task> taskList = new ArrayList<Task>();
	private ArrayList<Picture> picList = new ArrayList<Picture>();
	private ListView listView;
	private CustomListAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		listView = (ListView) findViewById(R.id.list);
		adapter = new CustomListAdapter(this, taskList);
		listView.setAdapter(adapter);

		pDialog = new ProgressDialog(this);
		// Showing progress dialog before making http request
		pDialog.setMessage("Loading...");
		pDialog.show();

		// Creating volley request obj
		JsonArrayRequest movieReq = new JsonArrayRequest(url,
				new Response.Listener<JSONArray>() {
					@Override
					public void onResponse(JSONArray response) {
						Log.d(TAG, response.toString());
						hidePDialog();

						// Parsing json
						for (int i = 0; i < response.length(); i++) {
							try {
								JSONObject obj = response.getJSONObject(i);
								Task task = new Task();

								task.setName(obj.getString("name"));
								task.setPlate(obj.getString("plate"));
								//task.setStaff(obj.getString("staff"));
								task.setJobnumber(obj.getString("jobnumber"));
								task.setId(obj.getString("id"));
								task.setDate(obj.getString("date"));

								JSONObject preview_pic_obj = response.getJSONObject(i).getJSONObject("previewPic");
								Picture preview_pic = new Picture();
								preview_pic.setThumb_link(preview_pic_obj.getString("thumb_link"));
								Log.d(TAG, preview_pic.getThumb_link().toString());
								task.setPreviewpic(preview_pic);

								taskList.add(task);

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
						hidePDialog();
					}
				});
		// Adding request to request queue
		AppController.getInstance().addToRequestQueue(movieReq);


		// On Click für Listview
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				long item = adapter.getItemId(position);
				Task selectedTask = taskList.get(position);

				Toast.makeText(getApplicationContext(),"Ausgewählter Auftrag: " + selectedTask.getName(), Toast.LENGTH_LONG).show();
				Intent i = new Intent(getApplicationContext(), DetailView.class);
				String selectedId = selectedTask.getId();
				i.putExtra(EXTRA_MESSAGE,selectedId);
				startActivity(i);
			}
		});
	}


	@Override
	public void onDestroy() {
		super.onDestroy();
		hidePDialog();
	}
	private void hidePDialog() {
		if (pDialog != null) {
			pDialog.dismiss();
			pDialog = null;
		}
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		//Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
    }
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
			case R.id.action_search:
				//openSearch();
				Log.d(TAG,"Search test");
				return true;

			case R.id.action_settings:
				Log.d(TAG,"Settings test");
				//openSettings();
				return true;

			case R.id.contact_list:
				Log.d(TAG, "Kontaktliste");
				Intent i_contact = new Intent(getApplicationContext(), ContactActivity.class);
				startActivity(i_contact);

			case R.id.new_task:
				Intent i_new_task = new Intent(getApplicationContext(), NewTaskActivity.class);
				startActivity(i_new_task);

			default:
				return super.onOptionsItemSelected(item);
		}
	}
}
