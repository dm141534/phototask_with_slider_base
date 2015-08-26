package com.daimajia.slider.demo.adater;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.daimajia.slider.demo.R;
import com.daimajia.slider.demo.model.Log;
import com.daimajia.slider.demo.model.Task;

import java.util.List;

/**
 * Created by christianjandl on 26.08.15.
 */
public class CommentListAdapter extends BaseAdapter {

    private static final String TAG = CustomListAdapter.class.getSimpleName();
    private Activity activity;
    private LayoutInflater inflater;
    private List<Log> logItems;

    public CommentListAdapter(Activity activity, List<Log> logItems) {
        this.activity = activity;
        this.logItems = logItems;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null)
            convertView = inflater.inflate(R.layout.list_comments, null);

        TextView message = (TextView) convertView.findViewById(R.id.comment);
        TextView staff = (TextView) convertView.findViewById(R.id.by_staff);
        TextView messageDate = (TextView) convertView.findViewById(R.id.message_date);
        // getting Task data for the row
        Log m = logItems.get(position);
        // Set Text in each Row


        message.setText(m.getMessage());
        messageDate.setText("Datum " + String.valueOf(m.getDate()));
        staff.setText(m.getBy_staff());

        return convertView;
    }



    @Override
    public int getCount() {
        return logItems.size();
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public Object getItem(int location) {
        return logItems.get(location);
    }

}
