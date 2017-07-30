

package com.alexsantos.careergoalsetting.activity;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.alexsantos.careergoalsetting.authentication.LoginActivity;
import com.alexsantos.careergoalsetting.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessaging;

public class MainActivity extends BaseActivity{


    private FirebaseAuth mAuth;
    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);


    FirebaseMessaging.getInstance().subscribeToTopic("android");


    mAuth = FirebaseAuth.getInstance();

    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(false);


    FloatingActionButton myFab = (FloatingActionButton) findViewById(R.id.fab);
    myFab.setOnClickListener(new View.OnClickListener() {
        public void onClick(View v) {
    Intent i = new Intent(getApplicationContext(), DetailActivity.class);
    startActivity(i);

        }
    });

    buildListView();


    }

    @Override
    protected void onStart() {
    super.onStart();
    FirebaseUser currentUser = mAuth.getCurrentUser();
    if(currentUser == null){
    Intent mainIntent = new Intent(MainActivity.this, LoginActivity.class);
    mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
    startActivity(mainIntent);

    finish();
    }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

    getMenuInflater().inflate(R.menu.menu_main, menu);
    return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
        case R.id.logoutButton:

        FirebaseAuth.getInstance().signOut();
        Intent sigoutIntent = new Intent(MainActivity.this , LoginActivity.class);
        startActivity(sigoutIntent);
        finish();

        return true;

        case R.id.account_setting:
        Intent i = new Intent(MainActivity.this, SettingsActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);

        return true;
         default:
        return super.onOptionsItemSelected(item);
        }
    }
    }
