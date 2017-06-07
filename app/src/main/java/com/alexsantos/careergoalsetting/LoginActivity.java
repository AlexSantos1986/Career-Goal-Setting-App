package com.alexsantos.careergoalsetting;

/**
 * Created by Alex on 02/06/2017.
 */

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alexsantos.careergoalsetting.activity.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {


    private EditText mEmail;
    private EditText mPassword;
    private Button mSignin;
    private TextView mRegisterNow;


    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
        mAuth = FirebaseAuth.getInstance();

        mEmail = (EditText) findViewById(R.id.emailLoginText);
        mPassword = (EditText) findViewById(R.id.passwordLoginText);
        mSignin = (Button) findViewById(R.id.signinButton);
        mRegisterNow = (TextView) findViewById(R.id.sendToRegisterButton);



        mSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startLogin();
            }
        });

        mRegisterNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendToRegisterIntent =  new Intent(LoginActivity.this, RegisterActivity.class);
                sendToRegisterIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(sendToRegisterIntent);
            }
        });
    }

    // method called by the sign in button
    public void startLogin(){

        String email = mEmail.getText().toString().trim();
        String  password = mPassword.getText().toString().trim();

        if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)){

            mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if(task.isSuccessful()){

                        checkExist();
                    }else{
                        Toast.makeText(LoginActivity.this, "Email or password is incorrect",Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }else{

            Toast.makeText(LoginActivity.this, "Fields can not be blank!", Toast.LENGTH_SHORT).show();
        }

    }
    //method called if the task id successfull it will ckeck if the user id alrealdy exist in the database
    public void checkExist(){

        final String user_Id = mAuth.getCurrentUser().getUid();

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

               if(dataSnapshot.hasChild(user_Id)){


                   Intent loginIntent = new Intent(LoginActivity.this,MainActivity.class);
                   loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                   startActivity(loginIntent);

               }else{
                   Toast.makeText(LoginActivity.this, "You need to set an account", Toast.LENGTH_SHORT).show();
               }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


}
