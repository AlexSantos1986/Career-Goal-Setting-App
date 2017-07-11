package com.alexsantos.careergoalsetting;

/**
 * Created by Alex on 02/06/2017.
 */

import android.app.ProgressDialog;
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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

public class LoginActivity extends AppCompatActivity {


    private EditText mEmail;
    private EditText mPassword;
    private Button mSignin;
    private TextView mRegisterNow;
    private ProgressDialog mLoginProgressDialog;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();

        mEmail = (EditText) findViewById(R.id.emailLoginText);
        mPassword = (EditText) findViewById(R.id.passwordLoginText);
        mSignin = (Button) findViewById(R.id.signinButton);
        mRegisterNow = (TextView) findViewById(R.id.sendToRegisterButton);
        mLoginProgressDialog = new ProgressDialog(this);



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

        final String email = mEmail.getText().toString().trim();
        final String  password = mPassword.getText().toString().trim();

        if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)){

            mLoginProgressDialog.setTitle("Logging In");
            mLoginProgressDialog.setMessage("Please wait while check your credential");
            mLoginProgressDialog.setCanceledOnTouchOutside(false);
            mLoginProgressDialog.show();
            loginUser(email, password);

        }else{

            Toast.makeText(LoginActivity.this, "Fields can not be blank!", Toast.LENGTH_SHORT).show();
        }

    }

    private void loginUser(String email, String password) {

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){
                    mLoginProgressDialog.dismiss();
                    Intent loginIntent = new Intent(LoginActivity.this,MainActivity.class);
                    loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(loginIntent);
                    finish();
                }else {
                    mLoginProgressDialog.hide();
                    Toast.makeText(LoginActivity.this, "Cannot Sign In, Please check the form and try again!", Toast.LENGTH_SHORT).show();
                }

            }
        });


    }



}
