package tn.example.asus_octadev.tunitour;

import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.games.event.Events;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

import tn.example.asus_octadev.tunitour.Adaper.AdapterEvent1;
import tn.example.asus_octadev.tunitour.Model.Event;

public class EventsFragment extends AppCompatActivity {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    ArrayList<Event> allSampleData;
    View view;
    ProgressBar progres;
    ImageView connection;
    AdapterEvent1 adapter;
    RecyclerView recyclerView;
    DatabaseReference mDatabase ;
    String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_events);
        Bundle extras = getIntent().getExtras();
        type=extras.getString("type");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle(type);
        allSampleData = new ArrayList<>();
        progres= (ProgressBar)findViewById(R.id.progress);
        connection= (ImageView)findViewById(R.id.connection);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        recyclerView.setHasFixedSize(true);
        GridLayoutManager gl = new GridLayoutManager(this, 1, LinearLayoutManager.VERTICAL, false);

        recyclerView.setLayoutManager(gl);
        adapter = new AdapterEvent1(allSampleData,this);


        recyclerView.setAdapter(adapter);
        createDummyData();

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(menuItem);
    }
    // TODO: Rename method, update argument and hook method into UI event

    private void createDummyData() {
        allSampleData.clear();

        if (!isOnline()) {
            connection.setVisibility(View.VISIBLE);
            progres.setVisibility(View.GONE);
            Snackbar snackbar = Snackbar
                    .make(EventsFragment.this.findViewById(R.id
                            .main_content), "No internet connection!", Snackbar.LENGTH_INDEFINITE)
                    .setAction("RETRY", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            createDummyData();
                        }
                    });

// Changing message text color
            snackbar.setActionTextColor(Color.RED);

// Changing action button text color
            View sbView = snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(Color.WHITE);
            snackbar.show();
        } else {
            connection.setVisibility(View.GONE);
            progres.setVisibility(View.VISIBLE);
            remplirEvent();
        }


    }
    public Boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) EventsFragment.this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()&& cm.getActiveNetworkInfo().isAvailable()&& cm.getActiveNetworkInfo().isConnected())
        {
            return true;
        }
        else
        {
            return false;
        }
    }


    public void remplirEvent()

    {allSampleData.clear();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("Events").
                addListenerForSingleValueEvent(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                // Get user value
                                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                    Event post = postSnapshot.getValue(Event.class);
                                    post.setId(postSnapshot.getKey());
                                    if(post.getType().equals(type))
                                    {
                                        allSampleData.add(post);
                                        progres.setVisibility(View.GONE);
                                        adapter.notifyDataSetChanged();

                                    }

                                }
                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                            }
                        });


    }

}
