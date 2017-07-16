package com.alexsantos.careergoalsetting.activity;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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
import android.widget.ProgressBar;
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

import me.zhanghai.android.materialprogressbar.MaterialProgressBar;

import static com.alexsantos.careergoalsetting.R.layout.career;

public class BaseActivity extends AppCompatActivity {


    private RecyclerView recycleListView;
    protected SearchView searchView;
    private String FirebaseID;
    FirebaseUser  mCurrentUser;
    Firebase mDatabaseRef;
    FirebaseRecyclerAdapter<Career, CareerViewHolder> mAdapter;
    CareerFirebaseAdapter adapter;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       Firebase.setAndroidContext(this);


      mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();

      String current_uid = mCurrentUser.getUid();

        mDatabaseRef = new Firebase(Constant.FIREBASE_URL).child("Users").child(current_uid).child("description");
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        recycleListView = (RecyclerView) findViewById(R.id.recyclerView);
        recycleListView.setHasFixedSize(true);
        recycleListView.setLayoutManager(new LinearLayoutManager(this));



       mAdapter = new FirebaseRecyclerAdapter<Career, CareerViewHolder>(
                Career.class,
                R.layout.career,
                CareerViewHolder.class,
                mDatabaseRef

        ) {
            @Override
            public void populateViewHolder(final CareerViewHolder viewHolder, final Career career, final int position) {
                viewHolder.setTitle(career.getTitle());
                viewHolder.setDate(career.getDate());

            final int totalListCount = recycleListView.getAdapter().getItemCount();

            progressBar.setMax(totalListCount);

               viewHolder.checkBox.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {


               if (viewHolder.checkBox.isChecked()) {
               progressBar.setProgress(progressBar.getProgress() + (totalListCount / 5));


               Toast.makeText(BaseActivity.this, "Well done you achived " + (progressBar.getProgress()) + " more goal keep going!", Toast.LENGTH_SHORT).show();


               }else{

                   progressBar.setProgress(progressBar.getProgress() - (totalListCount / 5));


                   Toast.makeText(BaseActivity.this, "Update your goal", Toast.LENGTH_SHORT).show();



               }
           }

        });

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

   recycleListView.setAdapter(mAdapter);
    }


    public static class CareerViewHolder extends RecyclerView.ViewHolder{

        View mView;
       CheckBox checkBox;

        public CareerViewHolder(View itemView) {
            super(itemView);

            mView = itemView;
            checkBox = (CheckBox) mView.findViewById(R.id.checkBox);

        }

        public void setTitle(String title){
            TextView careerTitle = (TextView) mView.findViewById(R.id.title);
            careerTitle.setText(title);

        }
        public void setDate(String date) {
            TextView careerDate = (TextView) mView.findViewById(R.id.date);
            careerDate.setText(date);

        }

    }



}
