package com.alexsantos.careergoalsetting.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentSender;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.alexsantos.careergoalsetting.R;
import com.alexsantos.careergoalsetting.StatusActivity;
import com.github.siyamed.shapeimageview.CircularImageView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.net.URI;
import java.util.Random;


/**
 * Created by Alex on 17/06/2017.
 */


public class SettingsActivity extends AppCompatActivity {


    private static final int PICK_IMAGE_REQUEST = 1;
    DatabaseReference mDatabaseRef;
    FirebaseUser mCurrentUser;
    //Storage
    private StorageReference mImageStorageRef;


    private CircularImageView mDisplayImage;
    private TextView mName;
    private TextView mStatus;
    private Button mStatusButton;
    private Button mImageButton;
    private ProgressDialog mProgressDialog;
    private Toolbar mToobar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_setting);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Add your Goal");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        mName = (TextView) findViewById(R.id.display_name);
        mStatus = (TextView) findViewById(R.id.setting_status_name);

        mStatusButton = (Button) findViewById(R.id.setting_status_button);
        mImageButton = (Button) findViewById(R.id.setting_image_button);
        mDisplayImage = (CircularImageView) findViewById(R.id.circularImageView);

        mImageStorageRef = FirebaseStorage.getInstance().getReference();
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

                //http://square.github.io/picasso/

                Picasso.with(SettingsActivity.this).load(image).into(mDisplayImage);

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
                statusIntent.putExtra("status_value", statusValue);
                startActivity(statusIntent);
            }
        });


        mImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);


             /*   CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(SettingsActivity.this);
                        */
            }
        });

    }

    //https://github.com/ArthurHub/Android-Image-Cropper library.

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK) {

            Uri imageUri = data.getData();

            CropImage.activity(imageUri)
                    .setAspectRatio(1, 1)
                    .start(this);

            //Toast.makeText(SettingsActivity.this, imageUri, Toast.LENGTH_LONG).show();
        }


        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {

                mProgressDialog = new ProgressDialog(SettingsActivity.this);
                mProgressDialog.setTitle("Uploading image...");
                mProgressDialog.setMessage("Please wait whle we upload and process the image");
                mProgressDialog.setCanceledOnTouchOutside(false);
                mProgressDialog.show();

                Uri resultUri = result.getUri();

             String current_user_id = mCurrentUser.getUid();

            StorageReference filePath = mImageStorageRef.child("profile_image").child(current_user_id +".jpg");

            filePath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                    if(task.isSuccessful()){

                       String download_url = task.getResult().getDownloadUrl().toString();
                        mDatabaseRef.child("image").setValue(download_url).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if(task.isSuccessful()){

                                    mProgressDialog.dismiss();
                                    Toast.makeText(SettingsActivity.this,"Success Uploading.", Toast.LENGTH_LONG).show();
                                }
                            }
                        });

                    }else{

                        Toast.makeText(SettingsActivity.this,"Error in uploaded.", Toast.LENGTH_LONG).show();
                        mProgressDialog.dismiss();
                    }
                }
            });

        } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
            Exception error = result.getError();
        }

        }
    }
/* // generate a random file each time an image is loaded.
    public static String random(){
        Random generator = new Random();
        StringBuilder sb = new StringBuilder();
        int randomLenght = generator.nextInt(15);
        char tempChar;
        for (int i =0; i < randomLenght; i++){

            tempChar = (char) (generator.nextInt(96) +32);
            sb.append(tempChar);
        }

        return sb.toString();
    }

*/
}

