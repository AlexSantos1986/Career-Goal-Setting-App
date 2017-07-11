package com.alexsantos.careergoalsetting.activity;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.alexsantos.careergoalsetting.adapter.CareerFirebaseAdapter;
import com.alexsantos.careergoalsetting.model.Career;
import com.alexsantos.careergoalsetting.utils.Constant;
import com.firebase.client.Firebase;
import com.firebase.client.Query;
import com.firebase.ui.FirebaseListAdapter;
import com.firebase.ui.FirebaseRecyclerAdapter;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;
import java.util.ArrayList;
import static com.alexsantos.careergoalsetting.R.layout.career;

public class BaseActivity extends AppCompatActivity {

   //private ListView list;
    private RecyclerView recycleListView;
    protected SearchView searchView;
    private String FirebaseID;
    FirebaseUser  mCurrentUser;
    Firebase mDatabaseRef;
    FirebaseRecyclerAdapter<Career, CareerViewHolder> mAdapter;
   // private FirebaseListAdapter mAdapter;
    CareerFirebaseAdapter adapter;


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

        //list = (ListView) findViewById(R.id.listView);
        //list.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        recycleListView = (RecyclerView) findViewById(R.id.recyclerView);
        recycleListView.setHasFixedSize(true);
        recycleListView.setLayoutManager(new LinearLayoutManager(this));
        //final CareerFirebaseAdapter adapter = new  CareerFirebaseAdapter(this,mDatabaseRef,Career.class,  R.layout.career);

       mAdapter = new FirebaseRecyclerAdapter<Career, CareerViewHolder>(
                Career.class,
                R.layout.career,
                CareerViewHolder.class,
                mDatabaseRef

        ) {
            @Override
            public void populateViewHolder(CareerViewHolder viewHolder, Career career, int position) {
                viewHolder.setTitle(career.getTitle());
                viewHolder.setDate(career.getDate());
                viewHolder.setCheckBox(career.isChecked());

                final String user_id = getRef(position).getKey();

                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent inte = new Intent(getApplicationContext(), DetailActivity.class);
                        inte.putExtra("FirebaseID", user_id);
                        startActivityForResult(inte, 0);
                    }
                });

            }
        };
        /*
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
*/


         /*
            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position,
                                        long id) {


                    Intent inte = new Intent(getApplicationContext(), DetailActivity.class);
                    inte.putExtra("FirebaseID", mAdapter.getRef(position).getKey());
                    startActivityForResult(inte, 0);
                }


            });
           */
            registerForContextMenu(recycleListView);

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
        recycleListView.getAdapter();


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

        //list.setAdapter(mAdapter);
recycleListView.setAdapter(mAdapter);
    }

    protected void buildSearchListView(String query) {
        //list.setAdapter(mAdapter);
        recycleListView.setAdapter(mAdapter);
    }

    public static class CareerViewHolder extends RecyclerView.ViewHolder{

        View mView;
        public CareerViewHolder(View itemView) {
            super(itemView);

            mView = itemView;
        }

        public void setTitle(String title){
            TextView careerTitle = (TextView) mView.findViewById(R.id.title);
            careerTitle.setText(title);

        }
        public void setDate(String date) {
            TextView careerDate = (TextView) mView.findViewById(R.id.date);
            careerDate.setText(date);

        }

        public void setCheckBox(boolean checked){

            CheckBox checkBox = (CheckBox) mView.findViewById(R.id.checkBox);
            checkBox.setChecked(checked);


        }
    }



}
