package com.daimajia.slider.demo.adater;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.daimajia.slider.demo.R;
import com.daimajia.slider.demo.model.Contact;
import java.util.List;

/**
 * Created by christianjandl on 31.08.15.
 */
public class ContactListAdapter extends BaseAdapter {

    private Activity activity;
    private LayoutInflater inflater;
    private List<Contact> contactItems;



    public ContactListAdapter(Activity activity, List<Contact> contactItems) {
        this.activity = activity;
        this.contactItems = contactItems;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null)
            convertView = inflater.inflate(R.layout.list_contacts, null);

        TextView secondname = (TextView) convertView.findViewById(R.id.secondname);
        TextView firstname = (TextView) convertView.findViewById(R.id.firstname);
        TextView company = (TextView) convertView.findViewById(R.id.company);
        TextView email = (TextView) convertView.findViewById(R.id.email);

        // getting Contact data for the row
        Contact c = contactItems.get(position);
        // Set Text in each Row
        secondname.setText(c.getSecondname());
        firstname.setText(c.getFirstname());
        company.setText(c.getCompany());
        email.setText(c.getEmailAddress());
        return convertView;
    }
    @Override
    public int getCount() {
        return contactItems.size();
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public Object getItem(int location) {
        return contactItems.get(location);
    }
}
