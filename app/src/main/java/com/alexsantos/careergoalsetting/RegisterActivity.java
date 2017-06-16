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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mDatabase;


    private EditText mUserName;
    private EditText mUserEmail;
    private EditText mUserPassword;
    private Button mButtonRegister;
    private TextView linkToLogin;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        mAuth = FirebaseAuth.getInstance();


        mUserName = (EditText) findViewById(R.id.userNameText);
        mUserEmail = (EditText) findViewById(R.id.userEmailText);
        mUserPassword = (EditText) findViewById(R.id.userPasswordText);
        mButtonRegister = (Button) findViewById(R.id.registerButton);
        linkToLogin = (TextView) findViewById(R.id.link_login);

        progressDialog = new ProgressDialog(this);


        mButtonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                register();
            }
        });

        linkToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginLinkIntent = new Intent(RegisterActivity.this, LoginActivity.class);
                loginLinkIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(loginLinkIntent);
            }
        });

    }

    public void register(){


        final String name = mUserName.getText().toString().trim();
        String email = mUserEmail.getText().toString().trim();
        String password = mUserPassword.getText().toString().trim();

        if(!TextUtils.isEmpty(name) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)){

            progressDialog.setMessage("Signing Up...");
            progressDialog.setMessage("Please wait while we create your account");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if(task.isSuccessful()){

                        FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
                        String user_id = current_user.getUid();
                        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id);

                        HashMap<String, String> userMap = new HashMap<String, String>();

                        userMap.put("name",name);
                        userMap.put("status","I'm using career goal setting app!!!");
                        userMap.put("image","default");
                        userMap.put("thumb_image","default");

                        mDatabase.setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if(task.isSuccessful()){
                                    progressDialog.dismiss();

                                    Intent mainIntent = new Intent(RegisterActivity.this, LoginActivity.class);
                                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(mainIntent);
                                    finish();
                                }

                            }
                        });

                    }else{
                        progressDialog.hide();
                        Toast.makeText(RegisterActivity.this,"Cannot Sigin. Please check the form and try again",Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }else{
            Toast.makeText(RegisterActivity.this,"Fields can't be blank",Toast.LENGTH_SHORT).show();
        }

    }
}
