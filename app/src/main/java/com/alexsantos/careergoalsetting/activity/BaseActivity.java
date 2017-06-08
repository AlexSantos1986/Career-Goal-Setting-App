package com.alexsantos.careergoalsetting.activity;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.alexsantos.careergoalsetting.R;
import com.alexsantos.careergoalsetting.adapter.CareerFirebaseAdapter;
import com.alexsantos.careergoalsetting.utils.Constant;
import com.firebase.client.Firebase;
import com.firebase.client.Query;

public class BaseActivity extends AppCompatActivity {

    private ListView list;
    protected SearchView searchView;
    private CareerFirebaseAdapter mAdapter;
    private Firebase myFirebaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Firebase.setAndroidContext(this);
        myFirebaseRef = new Firebase(Constant.FIREBASE_URL);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        list = (ListView) findViewById(R.id.listView);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View arg1, int arg2,
                                    long arg3) {

                Intent inte = new Intent(getApplicationContext(), DetailActivity.class);

                inte.putExtra("FirebaseID", mAdapter.getRef(arg2).getKey());
                startActivityForResult(inte, 0);
            }
        });

        registerForContextMenu(list);

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
        CareerFirebaseAdapter adapter = (CareerFirebaseAdapter) list.getAdapter();

        String FirebaseID = adapter.getRef(info.position).getKey().toString();


        switch (item.getItemId()) {
            case R.id.delete_item:
                myFirebaseRef.child(FirebaseID).removeValue();
                Toast.makeText(getApplicationContext(), "Contact Successfully deleted", Toast.LENGTH_SHORT).show();
                return true;
        }
        return super.onContextItemSelected(item);
    }




    public void buildListView() {
        mAdapter = new CareerFirebaseAdapter(this, myFirebaseRef);
        list.setAdapter(mAdapter);


    }


    protected void buildSearchListView(String query) {
        Query queryRef = myFirebaseRef.startAt(query).endAt(query+"\uf8ff").orderByChild("description");
        mAdapter = new CareerFirebaseAdapter(this, queryRef);
        list.setAdapter(mAdapter);
    }

}
