package com.alexsantos.careergoalsetting.activity;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.alexsantos.careergoalsetting.LoginActivity;
import com.alexsantos.careergoalsetting.R;
import com.alexsantos.careergoalsetting.model.Career;
import com.alexsantos.careergoalsetting.utils.Constant;
import com.firebase.client.Firebase;
import com.firebase.client.Query;
import com.firebase.ui.FirebaseListAdapter;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import static com.alexsantos.careergoalsetting.R.layout.career;

public class BaseActivity extends AppCompatActivity {

    private ListView list;
    protected SearchView searchView;
    private String FirebaseID;
    FirebaseUser  mCurrentUser;
    Firebase mDatabaseRef;
    FirebaseListAdapter <Career> mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       Firebase.setAndroidContext(this);

      mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();

      String current_uid = mCurrentUser.getUid();

        mDatabaseRef = new Firebase(Constant.FIREBASE_URL).child("Users").child(current_uid).child("description");
       // mDatabaseRef = FirebaseDatabase.getInstance().getReferenceFromUrl(Constant.FIREBASE_URL).child("description").child(current_uid);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        list = (ListView) findViewById(R.id.listView);
        list.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);


        mAdapter = new FirebaseListAdapter<Career>(
                this,
                Career.class,
                R.layout.career,mDatabaseRef
                ) {
            @Override
            protected void populateView(View view, Career career, int position) {
                //((CheckBox)view.findViewById(R.id.checkBox1)).setChecked(career.isChecked());
                ((TextView)view.findViewById(R.id.date)).setText(career.getDate());
                ((TextView)view.findViewById(R.id.title)).setText(career.getTitle());

            }
        };





            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position,
                                        long id) {


                    Intent inte = new Intent(getApplicationContext(), DetailActivity.class);
                    inte.putExtra("FirebaseID", mAdapter.getRef(position).getKey());
                    startActivityForResult(inte, 0);
                }


            });

            registerForContextMenu(list);
        //Toast.makeText(BaseActivity.this,  list.getAdapter().getCount(), Toast.LENGTH_SHORT).show();

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.pesqContato).getActionView();

        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        searchView.setIconifiedByDefault(true);
        return true;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater m = getMenuInflater();
        m.inflate(R.menu.menu_context, menu);
    }


    @Override
    public boolean onContextItemSelected(MenuItem item) {

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        list.getAdapter();


        FirebaseID = mAdapter.getRef(info.position).getKey().toString();

        switch (item.getItemId()) {
            case R.id.delete_item:
                mDatabaseRef.child(FirebaseID).removeValue();
                Toast.makeText(getApplicationContext(), "Contact Successfully deleted", Toast.LENGTH_SHORT).show();
                return true;
        }
        return super.onContextItemSelected(item);
    }


    public void buildListView() {

        list.setAdapter(mAdapter);

    }

    protected void buildSearchListView(String query) {
        list.setAdapter(mAdapter);
    }

}
