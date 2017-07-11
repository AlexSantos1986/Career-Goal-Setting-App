package com.alexsantos.careergoalsetting.activity;

import android.app.DatePickerDialog;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class DetailActivity extends AppCompatActivity {

    private Career career;
    private String FirebaseID;
    private EditText titleText;
    private AutoCompleteTextView descriptionText;
    private EditText dateText;
    private CheckBox checkBox ;
    private DatabaseReference myDatabaseRef;
    FirebaseUser mCurrentUser;
    private Calendar myCalendar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        myCalendar = Calendar.getInstance();

       Firebase.setAndroidContext(this);

        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();

        String current_uid = mCurrentUser.getUid();


        myDatabaseRef = FirebaseDatabase.getInstance().getReference().child("Users").child(current_uid).child("description");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Your Goal");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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


                  final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear,
                                              int dayOfMonth) {
                            // TODO Auto-generated method stub
                            myCalendar.set(Calendar.YEAR, year);
                            myCalendar.set(Calendar.MONTH, monthOfYear);
                            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                            updateLabel();
                        }

                    };

                    dateText.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            // TODO Auto-generated method stub
                            new DatePickerDialog(DetailActivity.this, date, myCalendar
                                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                                    myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                        }
                    });


                }


            }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }

            });

        }
    }

    private void updateLabel() {

        String myFormat = "dd/MM/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        dateText.setText(sdf.format(myCalendar.getTime()));

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
                    Toast.makeText(this, "Information successfully Added!!!", Toast.LENGTH_SHORT).show();
                } else {

                    career.setTitle(title);
                    career.setDescription(description);
                    career.setDate(date);


                    myDatabaseRef.child(FirebaseID).setValue(career);

                    Toast.makeText(this, "Information successfully Edited!!!", Toast.LENGTH_SHORT).show();
                }

                finish();
            }


        }
