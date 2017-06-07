package com.alexsantos.careergoalsetting.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.alexsantos.careergoalsetting.R;
import com.alexsantos.careergoalsetting.model.Career;
import com.alexsantos.careergoalsetting.utils.Constant;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.Date;

public class DetailActivity extends AppCompatActivity {

    private Career career;
    private String FirebaseID;
    private Firebase myFirebaseRef;

    EditText descriptionText;
    EditText dateText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Firebase.setAndroidContext(this);

        myFirebaseRef = new Firebase(Constant.FIREBASE_URL);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getIntent().hasExtra("FirebaseID")) {

            FirebaseID = getIntent().getStringExtra("FirebaseID");
            Firebase refCareer = myFirebaseRef.child(FirebaseID);


            ValueEventListener refCareerListener = refCareer.addValueEventListener(new ValueEventListener() {

                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    career = snapshot.getValue(Career.class);

                    if ( career != null) {
                      descriptionText = (EditText) findViewById(R.id.editText1);
                        descriptionText.setText(career.getDescription());

                        dateText = (EditText) findViewById(R.id.editText2);
                        dateText.setText(career.getDate());


                    }
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {
                    Log.e("LOG", firebaseError.getMessage());
                }

            });


        }

     }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.detail, menu);
        if(FirebaseID == null){
            MenuItem item = menu.findItem(R.id.delCareer);
            item.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()){
            case R.id.saveCareer:

                save();

                return true;
            case R.id.delCareer:
                myFirebaseRef.child(FirebaseID).removeValue();
                Toast.makeText(getApplicationContext(), "Contact successfully deleted", Toast.LENGTH_SHORT).show();
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    public void save() {
        String description = ((EditText) findViewById(R.id.editText1)).getText().toString();
        String date = ((EditText) findViewById(R.id.editText2)).getText().toString();


        if (career == null) {
            career = new Career();
            career.setDescription(description);
            career.setDate(date);

            myFirebaseRef.child("Career Goal User").setValue(career);
            Toast.makeText(this, "Contact successfully Added!!!", Toast.LENGTH_SHORT).show();
        } else {

            career.setDescription(description);
            career.setDate(date);


            myFirebaseRef.child(FirebaseID).setValue(career);

            Toast.makeText(this, "Contact successfully Edited!!!", Toast.LENGTH_SHORT).show();
        }

        finish();
    }
}
