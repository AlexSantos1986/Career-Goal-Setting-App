package com.alexsantos.careergoalsetting.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.alexsantos.careergoalsetting.R;
import com.alexsantos.careergoalsetting.model.Career;
import com.alexsantos.careergoalsetting.utils.Constant;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

public class DetailActivity extends AppCompatActivity {

    private Career career;
    private String FirebaseID;
    private Firebase myFirebaseRef;
    private EditText titleText;
    private AutoCompleteTextView descriptionText;
    private EditText dateText;
    private Button button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        button = (Button) findViewById(R.id.button);

        Firebase.setAndroidContext(this);

        myFirebaseRef = new Firebase(Constant.FIREBASE_URL);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Add your Goal");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        if (getIntent().hasExtra("FirebaseID")) {

            FirebaseID = getIntent().getStringExtra("FirebaseID");
            Firebase refCareer = myFirebaseRef.child(FirebaseID);


            final ValueEventListener refCareerListener = refCareer.addValueEventListener(new ValueEventListener() {

                @Override
                public void onDataChange(DataSnapshot snapshot) {

                    career = snapshot.getValue(Career.class);
                    if (career != null) {

                        titleText = (EditText) findViewById(R.id.editText3);
                        titleText.setText(career.getTitle());

                        descriptionText = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView);
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
        if (FirebaseID == null) {
            MenuItem item = menu.findItem(R.id.delContact);
            item.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.saveContact:

                save();

                return true;
            case R.id.delContact:

                myFirebaseRef.child(FirebaseID).removeValue();
                Toast.makeText(getApplicationContext(), "Contact successfully deleted", Toast.LENGTH_SHORT).show();
                finish();

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }

    public void save() {
        String description = ((AutoCompleteTextView) findViewById(R.id.autoCompleteTextView)).getText().toString();
        String date = ((EditText) findViewById(R.id.editText2)).getText().toString();
        String title = ((EditText) findViewById(R.id.editText3)).getText().toString();


        if (career == null) {
            career = new Career();
            career.setTitle(title);
            career.setDescription(description);
            career.setDate(date);


            myFirebaseRef.push().setValue(career);
            Toast.makeText(this, "Contact successfully Added!!!", Toast.LENGTH_SHORT).show();
        } else {

            career.setTitle(title);
            career.setDescription(description);
            career.setDate(date);


            myFirebaseRef.child(FirebaseID).setValue(career);

            Toast.makeText(this, "Contact successfully Edited!!!", Toast.LENGTH_SHORT).show();
        }

        finish();
    }



}
