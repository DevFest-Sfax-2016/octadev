package tn.example.asus_octadev.tunitour.Adaper;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.ArrayList;

import tn.example.asus_octadev.tunitour.Model.Commentaire;
import tn.example.asus_octadev.tunitour.R;


/**
 * Created by ASUS-OCTADEV on 2016-08-16.
 */

public class Adapter_commentaire extends RecyclerView.Adapter<Adapter_commentaire.CommentaireViewHolder>  {
    private ArrayList<Commentaire> mDataSet;
    Context mcontext;

    public Adapter_commentaire(ArrayList<Commentaire> mDataSet, Context mcontect) {
        this.mDataSet = mDataSet;
        this.mcontext=mcontect;

    }


    @Override
    public CommentaireViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_line, parent, false);
        CommentaireViewHolder userViewHolder = new CommentaireViewHolder(v);

        return userViewHolder;
    }

    @Override
    public void onBindViewHolder(final CommentaireViewHolder holder, final int position) {


           Commentaire s= mDataSet.get(position);
            Log.v("LOG_TAG", "response ch5" + s);
            holder.octadev_commentaire.setText(s.getComment());
            holder.mobidev_img_pub.setText(s.getUsername());


        // holder.octadev_id_pub.setText(mDataSet.get(position).getUser_uid());

    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }


    public static class CommentaireViewHolder extends RecyclerView.ViewHolder {
        TextView octadev_commentaire,mobidev_img_pub, octadev_id_pub;
        ImageView octadev_image_comment;

        CommentaireViewHolder(View itemView) {
            super(itemView);
            octadev_commentaire = (TextView) itemView.findViewById(R.id.octadev_commentaire);
            mobidev_img_pub = (TextView) itemView.findViewById(R.id.mobidev_img_pub);

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