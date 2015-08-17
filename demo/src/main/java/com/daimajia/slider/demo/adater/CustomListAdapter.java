package com.daimajia.slider.demo.adater;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.List;

import com.daimajia.slider.demo.R;
import com.daimajia.slider.demo.AppController;
import com.daimajia.slider.demo.model.Picture;
import com.daimajia.slider.demo.model.Task;

public class CustomListAdapter extends BaseAdapter {

	private static final String TAG = CustomListAdapter.class.getSimpleName();
	private Activity activity;
	private LayoutInflater inflater;
	private List<Task> taskItems;
	ImageLoader imageLoader = AppController.getInstance().getImageLoader();

	public CustomListAdapter(Activity activity, List<Task> taskItems) {
		this.activity = activity;
		this.taskItems = taskItems;
	}

	@Override
	public int getCount() {
		return taskItems.size();
	}

	@Override
	public Object getItem(int location) {



		return taskItems.get(location);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if (inflater == null)
			inflater = (LayoutInflater) activity
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		if (convertView == null)
			convertView = inflater.inflate(R.layout.list_row, null);

		if (imageLoader == null)
			imageLoader = AppController.getInstance().getImageLoader();
		NetworkImageView thumbNail = (NetworkImageView) convertView
				.findViewById(R.id.thumbnail);
		TextView name = (TextView) convertView.findViewById(R.id.name);
		TextView plate = (TextView) convertView.findViewById(R.id.plate);
		TextView date = (TextView) convertView.findViewById(R.id.date);
		//TextView staff = (TextView) convertView.findViewById(R.id.staff);
		TextView jobnumber = (TextView) convertView.findViewById(R.id.jobnumber);


		// getting Task data for the row
		Task m = taskItems.get(position);

		// thumbnail image Object:
		Picture previewPic = new Picture();
		previewPic = m.getPreviewpic();
		String previewLink = new StringBuilder().append("http://dm141534.students.fhstp.ac.at/phototask_api/").append(previewPic.getThumb_link()).toString();
		Log.d(TAG, previewLink);


		thumbNail.setImageUrl(previewLink, imageLoader);


		
		// Name
		name.setText(m.getName());
		
		// plate
		plate.setText("Kennzeichen: " + String.valueOf(m.getPlate()));
		
		// jobnumber
		jobnumber.setText(String.valueOf(m.getJobnumber()));
		
		// release year
		//staff.setText(String.valueOf(m.getDate()));

		return convertView;
	}
	public void onItemClicked(AdapterView<?> parent, View view, int pos,long id) {
		Toast.makeText(parent.getContext(),
				"OnItemSelectedListener : " + parent.getItemAtPosition(pos).toString(),
				Toast.LENGTH_SHORT).show();
	}


}