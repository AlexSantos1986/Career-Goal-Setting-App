package com.alexsantos.careergoalsetting.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.alexsantos.careergoalsetting.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Alex on 17/06/2017.
 */
public class StatusActivity extends AppCompatActivity {

    private Toolbar mToobar;
    private Button saveStatus;
    private EditText mStatus;

    private DatabaseReference mStatusDatabase;
    private FirebaseUser mCurrentUser;
    private ProgressDialog mProgress;

@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_status);

    mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
    String current_uid =  mCurrentUser.getUid();
    mStatusDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(current_uid);



    mToobar = (Toolbar) findViewById(R.id.toolbar_status);
    setSupportActionBar(mToobar);
    getSupportActionBar().setTitle("Account Status");
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    mProgress = new ProgressDialog(StatusActivity.this);

    String status_value = getIntent().getStringExtra("status_value");


    saveStatus = (Button) findViewById(R.id.setting_status_button);
    mStatus = (EditText) findViewById(R.id.text_input_status);

    mStatus.setText(status_value);

    saveStatus.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

    mProgress.setTitle("Saving Changes");
    mProgress.setMessage("Please wait while we making changes");
    mProgress.show();

    Intent i = new Intent(StatusActivity.this, SettingsActivity.class);
    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
    startActivity(i);

    String status = mStatus.getText().toString();

    mStatusDatabase.child("status").setValue(status).addOnCompleteListener(new OnCompleteListener<Void>() {
        @Override
        public void onComplete(@NonNull Task<Void> task) {

            if(task.isSuccessful()){

                mProgress.dismiss();
            }else{

                Toast.makeText(getApplicationContext(), "There was some error in saving changes",Toast.LENGTH_LONG );
            }
        }
    });

            }
        });

    }
}
