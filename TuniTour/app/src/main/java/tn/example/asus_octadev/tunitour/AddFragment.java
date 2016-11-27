package tn.example.asus_octadev.tunitour;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Address;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.schibstedspain.leku.LocationPickerActivity;

import java.io.IOException;
import java.util.ArrayList;

import tn.example.asus_octadev.tunitour.Adaper.AdapterAdd;
import tn.example.asus_octadev.tunitour.Adaper.AdapterEvent1;
import tn.example.asus_octadev.tunitour.Model.Add;
import tn.example.asus_octadev.tunitour.Model.Event;
import tn.example.asus_octadev.tunitour.Model.User;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AddFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AddFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    ProgressBar progres;
    ImageView connection;
    AdapterAdd adapter;
    RecyclerView recyclerView;
    DatabaseReference mDatabase ;
    ArrayList<Add> allSampleData;
    Uri selectedImage,url;
    FirebaseAuth mAuth;
    String lieux="";
    EditText name;
    Add post;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public AddFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddFragment newInstance(String param1, String param2) {
        AddFragment fragment = new AddFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_add, container, false);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        name= (EditText) view.findViewById(R.id.name);
        view.findViewById(R.id.done).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!name.getText().toString().equals("")) {
                    mAuth = FirebaseAuth.getInstance();
                    StorageReference firestorage;
                    firestorage = FirebaseStorage.getInstance().getReference();
                    StorageReference filepath = firestorage.child("users").child(selectedImage.getLastPathSegment());
                    filepath.putFile(selectedImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            url = taskSnapshot.getDownloadUrl();
                            Add add = new Add(mAuth.getCurrentUser().getUid(), name.getText().toString(), url + "", lieux, 0, "0");
                            mDatabase.child("ad").push().setValue(add);
                            updatCom();

                        }
                    });


                }
                else
                    Toast.makeText(getActivity(), "veuillez renseigner la description", Toast.LENGTH_SHORT).show();

            }
        });
        view.findViewById(R.id.camera).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 200);

            }
        });
        view.findViewById(R.id.position).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), LocationPickerActivity.class);
                startActivityForResult(i, 1);
            }
        });

        allSampleData = new ArrayList<>();
        progres= (ProgressBar)view.findViewById(R.id.progress);
        connection= (ImageView)view.findViewById(R.id.connection);

        recyclerView = (RecyclerView) view.findViewById(R.id.recy);

        recyclerView.setHasFixedSize(true);
        GridLayoutManager gl = new GridLayoutManager(getActivity(), 1, LinearLayoutManager.VERTICAL, false);

        recyclerView.setLayoutManager(gl);
        adapter = new AdapterAdd(allSampleData,getActivity());


        recyclerView.setAdapter(adapter);
        createDummyData();


        return view;
    }
    public void updatCom() {
        mDatabase.child("ad").limitToLast(1).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Get user value
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            User post = postSnapshot.getValue(User.class);
                            // System.out.println(post.email + " - " + post.password);if
                            if (post != null) {
                                String IdCom = postSnapshot.getKey().toString();
                                mDatabase.child("ad").child(IdCom).child("created").setValue(ServerValue.TIMESTAMP);
                            }
                        }


                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
    }

    private void createDummyData() {
        allSampleData.clear();

        if (!isOnline()) {

            Snackbar snackbar = Snackbar
                    .make(getActivity().findViewById(R.id
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

            remplirEvent();
        }


    }
    public Boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
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
        mDatabase.child("ad").
                addListenerForSingleValueEvent(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                // Get user value
                                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                    post = postSnapshot.getValue(Add.class);
                                    post.setId(postSnapshot.getKey());
                                    mDatabase = FirebaseDatabase.getInstance().getReference();
                                    String id=post.getIduser()+"";
                                    mDatabase.child("users").child(id).
                                            addListenerForSingleValueEvent(
                                                    new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(DataSnapshot dataSnapshot1) {
                                                            // Get user value
                                                                User post1 = dataSnapshot1.getValue(User.class);
                                                                post.setPhoto(post1.getProfile_image()+"");
                                                                post.setName(post1.getFull_name()+"");


                                                        }
                                                        @Override
                                                        public void onCancelled(DatabaseError databaseError) {
                                                        }
                                                    });


                                    allSampleData.add(post);
                                        adapter.notifyDataSetChanged();



                                }
                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                            }
                        });


    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 200 && resultCode == RESULT_OK
                && null != data) {
            selectedImage = data.getData();



        }
        if (requestCode == 1) {
            if(resultCode == RESULT_OK){

                String address = data.getStringExtra(LocationPickerActivity.LOCATION_ADDRESS);
                lieux=address;

            }
            if (resultCode == RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }
    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
