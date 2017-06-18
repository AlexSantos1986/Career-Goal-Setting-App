package com.alexsantos.careergoalsetting.activity;

import android.provider.ContactsContract;
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
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class DetailActivity extends AppCompatActivity {

    private Career career;
    private String FirebaseID;
    private EditText titleText;
    private AutoCompleteTextView descriptionText;
    private EditText dateText;
    private Button button;
    private DatabaseReference myDatabaseRef;
    FirebaseUser mCurrentUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);


       Firebase.setAndroidContext(this);

        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();

        String current_uid = mCurrentUser.getUid();


        myDatabaseRef = FirebaseDatabase.getInstance().getReference().child("Users").child(current_uid).child("description");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Add your Goal");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        button = (Button) findViewById(R.id.button);


        if (getIntent().hasExtra("FirebaseID")) {

            FirebaseID = getIntent().getStringExtra("FirebaseID");
            DatabaseReference refCareer = myDatabaseRef.child(FirebaseID);



             refCareer.addValueEventListener(new com.google.firebase.database.ValueEventListener() {
                @Override
                public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {

                    career = dataSnapshot.getValue(Career.class);
                    if (career != null) {

                        HashMap<String, Career> userMap = new HashMap<String, Career>();



                        titleText = (EditText) findViewById(R.id.editText3);
                        titleText.setText(career.getTitle());

                        descriptionText = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView);
                        descriptionText.setText(career.getDescription());

                        dateText = (EditText) findViewById(R.id.editText2);
                        dateText.setText(career.getDate());

                        userMap.put("description",career);


                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

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

                myDatabaseRef.child(FirebaseID).removeValue();
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


            myDatabaseRef.push().setValue(career);
            Toast.makeText(this, "Contact successfully Added!!!", Toast.LENGTH_SHORT).show();
        } else {

            career.setTitle(title);
            career.setDescription(description);
            career.setDate(date);


            myDatabaseRef.child(FirebaseID).setValue(career);

            Toast.makeText(this, "Contact successfully Edited!!!", Toast.LENGTH_SHORT).show();
        }

        finish();
    }


}
