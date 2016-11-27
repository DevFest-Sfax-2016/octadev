package tn.example.asus_octadev.tunitour.Adaper;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import tn.example.asus_octadev.tunitour.DetailEvent;
import tn.example.asus_octadev.tunitour.Model.Add;
import tn.example.asus_octadev.tunitour.Model.Commentaire;
import tn.example.asus_octadev.tunitour.Model.Event;
import tn.example.asus_octadev.tunitour.Model.User;
import tn.example.asus_octadev.tunitour.R;
import tn.example.asus_octadev.tunitour.Utils.MyApplication;
import tn.example.asus_octadev.tunitour.Utils.UrlStatic;


/**
 * Created by ASUS-OCTADEV on 2016-08-29.
 */

public class AdapterAdd extends RecyclerView.Adapter<AdapterAdd.OffreViewHolder> {
    private ArrayList<Add> mDataSet;
    Context mcontext;
    DatabaseReference mDatabase;
    ArrayList<Commentaire> liste;
    Adapter_commentaire adapter;
    EditText message;


    public AdapterAdd(ArrayList<Add> mDataSet, Context mcontect) {
        this.mDataSet = mDataSet;
        this.mcontext = mcontect;
    }


    @Override
    public OffreViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_add, parent, false);
        OffreViewHolder userViewHolder = new OffreViewHolder(v);

        return userViewHolder;
    }

    @Override
    public void onBindViewHolder(final OffreViewHolder holder, final int position) {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        liste = new ArrayList<Commentaire>();
        holder.name.setText(mDataSet.get(position).getName());
        holder.titre.setText(mDataSet.get(position).getDescription());
        holder.lieu.setText(mDataSet.get(position).getLieux());
        Long time = (Long) (System.currentTimeMillis());
        String result = (String) DateUtils.getRelativeTimeSpanString(mDataSet.get(position).getCreated(), time, 0);
        holder.date.setText(result + "");
        Picasso.with(mcontext).load(mDataSet.get(position).getPhoto()).into(holder.user);
        if (!mDataSet.get(position).getImage().equals(""))
            Picasso.with(mcontext).load(mDataSet.get(position).getImage()).into(holder.content);
        else
            holder.content.setVisibility(View.GONE);


        holder.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLike(mDataSet.get(position).getId(), Integer.parseInt(mDataSet.get(position).getLike()));
            }
        });
        holder.comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ListeComment(mDataSet.get(position).getId());
                final Dialog dialog = new Dialog(mcontext);
                dialog.setContentView(R.layout.liste_comment);
                RecyclerView recyclerView = (RecyclerView) dialog.findViewById(R.id.recy);
                LinearLayoutManager layoutManager = new LinearLayoutManager(mcontext);

                recyclerView.setLayoutManager(layoutManager);
                adapter = new Adapter_commentaire(liste, mcontext);
                recyclerView.setAdapter(adapter);
                dialog.show();
                message = (EditText) dialog.findViewById(R.id.comment);

                dialog.findViewById(R.id.send).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(! message.getText().toString().equals(""))
                        {
                        FirebaseAuth mAuth = FirebaseAuth.getInstance();
                        Commentaire o = new Commentaire(message.getText().toString(), mAuth.getCurrentUser().getUid(), 0);
                        mDatabase.child("ad_comments").child(mDataSet.get(position).getId()).push().setValue(o);
                        message.setText("");
                        updatCom(mDataSet.get(position).getId());
                        sendnoification(mDataSet.get(position).getFcm());}
                        else
                            Toast.makeText(mcontext, "remplir le contenu de votre commentaire !", Toast.LENGTH_SHORT).show();


                    }
                });
            }
        });
        holder.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent share = new Intent(android.content.Intent.ACTION_SEND);
                share.setType("text/plain");
                share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                share.putExtra(Intent.EXTRA_SUBJECT, mDataSet.get(position).getDescription() + "");
                share.putExtra(Intent.EXTRA_TEXT, mDataSet.get(position).getImage());
                mcontext.startActivity(Intent.createChooser(share, "Share link!"));
            }
        });


    }

    private void sendnoification(final String fcm) {
        StringRequest strReq = new StringRequest(Request.Method.POST,
                UrlStatic.notifier, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse networkResponse = error.networkResponse;

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("fcm", fcm + "");


                return params;
            }
        };
        strReq.setRetryPolicy(new DefaultRetryPolicy(500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MyApplication.getInstance().addToRequestQueue(strReq);

    }

    public void updatCom(final String id) {
        mDatabase.child("ad_comments").child(id).limitToLast(1).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Get user value
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            User post = postSnapshot.getValue(User.class);
                            // System.out.println(post.email + " - " + post.password);if
                            if (post != null) {
                                String IdCom = postSnapshot.getKey().toString();
                                mDatabase.child("ad_comments").child(id).child(IdCom).child("created_at").setValue(ServerValue.TIMESTAMP);
                            } else
                                Toast.makeText(mcontext, "null", Toast.LENGTH_SHORT).show();
                        }


                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
    }

    private void ListeComment(String id) {
        liste.clear();
        mDatabase.child("ad_comments").child(id + "").addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Get user value
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            final Commentaire post = postSnapshot.getValue(Commentaire.class);
                            mDatabase.child("users").child(post.getUser_uid()).addListenerForSingleValueEvent(
                                    new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            // Get user value

                                            User user = dataSnapshot.getValue(User.class);
                                            if (user != null) {
                                                post.setUsername(user.getFull_name());
                                                liste.add(post);
                                                adapter.notifyDataSetChanged();
                                            }

                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {
                                        }
                                    });

                        }


                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
    }

    private void setLike(String id, int l) {
        mDatabase.child("ad").child(id).child("like").setValue(l + 1 + "");
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }


    public static class OffreViewHolder extends RecyclerView.ViewHolder {
        ImageView like, comment, share, content, user;
        TextView name, lieu, date, titre;

        OffreViewHolder(View itemView) {
            super(itemView);
            user = (ImageView) itemView.findViewById(R.id.user);
            like = (ImageView) itemView.findViewById(R.id.like);
            comment = (ImageView) itemView.findViewById(R.id.comment);
            share = (ImageView) itemView.findViewById(R.id.share);
            content = (ImageView) itemView.findViewById(R.id.content);
            name = (TextView) itemView.findViewById(R.id.name);
            lieu = (TextView) itemView.findViewById(R.id.lieu);
            date = (TextView) itemView.findViewById(R.id.date);
            titre = (TextView) itemView.findViewById(R.id.titre);


        }
    }


    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    private int position;

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }


}
