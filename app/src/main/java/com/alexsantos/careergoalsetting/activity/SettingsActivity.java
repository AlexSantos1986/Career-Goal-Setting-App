package com.alexsantos.careergoalsetting.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.alexsantos.careergoalsetting.R;
import com.alexsantos.careergoalsetting.StatusActivity;
import com.github.siyamed.shapeimageview.CircularImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


/**
 * Created by Alex on 17/06/2017.
 */
public class SettingsActivity extends AppCompatActivity {


    DatabaseReference mDatabaseRef;
    FirebaseUser mCurrentUser;

    private CircularImageView mDisplayImage;
    private TextView mName;
    private TextView mStatus;
    private Button mStatusButton;
    private Button mImageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);


        mName = (TextView) findViewById(R.id.display_name);
        mStatus = (TextView) findViewById(R.id.setting_status_name);

        mStatusButton = (Button) findViewById(R.id.setting_status_button);
        mImageButton = (Button) findViewById(R.id.setting_image_button);

        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();

        String current_uid = mCurrentUser.getUid();

        mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("Users").child(current_uid);

        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String name = dataSnapshot.child("name").getValue().toString();
                String status = dataSnapshot.child("status").getValue().toString();
                String image = dataSnapshot.child("image").getValue().toString();
                String thumb_image = dataSnapshot.child("thumb_image").getValue().toString();

                mName.setText(name);
                mStatus.setText(status);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mStatusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String statusValue = mStatus.getText().toString();

                Intent statusIntent = new Intent(SettingsActivity.this, StatusActivity.class);
                statusIntent.putExtra("status_value",statusValue);
                startActivity(statusIntent);
            }
        });

    }
}
