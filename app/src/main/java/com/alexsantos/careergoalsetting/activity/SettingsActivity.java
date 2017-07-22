package com.alexsantos.careergoalsetting.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
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

import java.io.ByteArrayOutputStream;
import java.io.File;

import id.zelory.compressor.Compressor;


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
        getSupportActionBar().setTitle("Your Profile");
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


                if(!image.equals("default")){

                    //http://square.github.io/picasso/
                    Picasso.with(SettingsActivity.this).load(image).placeholder(R.drawable.avatar_profile).into(mDisplayImage);

                }


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

            }
        });

    }

    /**
     * https://github.com/ArthurHub/Android-Image-Cropper library.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK) {

            Uri imageUri = data.getData();

            CropImage.activity(imageUri)
                    .setAspectRatio(1, 1)
                    .start(this);

        }


        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {

            mProgressDialog = new ProgressDialog(SettingsActivity.this);
            mProgressDialog.setTitle("Uploading image...");
            mProgressDialog.setMessage("Please wait while we upload and process the image");
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.show();

                Uri resultUri = result.getUri();

            String current_user_id = mCurrentUser.getUid();

            File thumb = new File(resultUri.getPath());

            Bitmap bitmap = new Compressor(this)
                    .setMaxWidth(200)
                    .setMaxHeight(200)
                    .setQuality(75)
                .compressToBitmap(thumb);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);

            byte[] thumb_byte = outputStream.toByteArray();


            StorageReference filePath = mImageStorageRef.child("profile_image").child(current_user_id +".jpg");
            StorageReference thumb_filepath = mImageStorageRef.child("profile_image").child("thumb").child(current_user_id + ".jpg");


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

}

