package com.alexsantos.careergoalsetting.adapter;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.alexsantos.careergoalsetting.LoginActivity;
import com.alexsantos.careergoalsetting.R;
import com.alexsantos.careergoalsetting.model.Career;
import com.firebase.client.Firebase;
import com.firebase.client.Query;
import com.firebase.ui.FirebaseListAdapter;
/**
 * Created by Alex on 05/06/2017.
 */

public class CareerFirebaseAdapter extends FirebaseListAdapter<Career> {


    static final Class<Career> modelClass= Career.class;
    static final int modelLayout= R.layout.career;

    public CareerFirebaseAdapter(Activity activity, Firebase ref) {
        super(activity, modelClass, modelLayout, ref);


    }

    public CareerFirebaseAdapter(Activity activity,  Query ref) {
        super(activity, modelClass, modelLayout, ref);
    }

    @Override
    protected void populateView(View view, Career career, int position) {
        ((TextView)view.findViewById(R.id.description)).setText(career.getDescription());
        ((TextView)view.findViewById(R.id.date)).setText(career.getDate());
        ((TextView)view.findViewById(R.id.title)).setText(career.getTitle());



    }

}
