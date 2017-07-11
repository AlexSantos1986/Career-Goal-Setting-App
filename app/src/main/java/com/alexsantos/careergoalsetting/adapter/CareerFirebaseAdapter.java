package com.alexsantos.careergoalsetting.adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.alexsantos.careergoalsetting.LoginActivity;
import com.alexsantos.careergoalsetting.R;
import com.alexsantos.careergoalsetting.model.Career;
import com.firebase.client.Firebase;
import com.firebase.client.Query;
import com.firebase.ui.FirebaseListAdapter;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

import static com.alexsantos.careergoalsetting.R.layout.career;

/**
 * Created by Alex on 05/06/2017.
 */

public class CareerFirebaseAdapter extends BaseAdapter {

      Class<Career> modelClass;
     int modelLayout;
    private Context mContext;
    Activity activity;
    Firebase ref;

    public CareerFirebaseAdapter(Activity activity, Firebase ref, Class<Career> modelClass, int modelLayout) {
        super();

        this.activity = activity;
        this.ref = ref;
        this.modelClass = modelClass;
        this.modelLayout = modelLayout;
    }
    @Override
    public int getCount() {
        return 0;
    }
    @Override
    public String getItem(int position) {
        return null;
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    public void onItemSelected(int position) {
    }
    public class ViewHolder {
        public TextView nametext;
        public CheckBox tick;
    }
    @Override
    public View getView(final int position, View convertView,
                        ViewGroup parent) {
        // TODO Auto-generated method stub
        ViewHolder view = null;
        LayoutInflater inflator = ((Activity) mContext).getLayoutInflater();
        if (view == null) {
            view = new ViewHolder();
            convertView = inflator.inflate(  R.layout.career, null);
            view.nametext = (TextView) convertView.findViewById(R.id.date);
            //view.tick=(CheckBox)convertView.findViewById(R.id.checkBox);
            view.tick.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView,
                                             boolean isChecked) {
                    // Set the value of checkbox to maintain its state.
                    if (isChecked) {
                        //do sometheing here
                    }
                    else
                    {
                        // code here
                    }
                }
            });
            convertView.setTag(view);
        } else {
            view = (ViewHolder) convertView.getTag();
        }

        return convertView;
    }


}