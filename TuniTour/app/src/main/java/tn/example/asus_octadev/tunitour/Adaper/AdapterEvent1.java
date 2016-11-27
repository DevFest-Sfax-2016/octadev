package tn.example.asus_octadev.tunitour.Adaper;

import android.app.usage.UsageEvents;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import tn.example.asus_octadev.tunitour.DetailEvent;
import tn.example.asus_octadev.tunitour.Model.Event;
import tn.example.asus_octadev.tunitour.R;


/**
 * Created by ASUS-OCTADEV on 2016-08-29.
 */

public class AdapterEvent1 extends  RecyclerView.Adapter<AdapterEvent1.OffreViewHolder> {
    private ArrayList<Event> mDataSet;
    Context mcontext;

    public AdapterEvent1(ArrayList<Event> mDataSet, Context mcontect) {
        this.mDataSet = mDataSet;
        this.mcontext = mcontect;
    }


    @Override
    public OffreViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_event1, parent, false);
        OffreViewHolder userViewHolder = new OffreViewHolder(v);

        return userViewHolder;
    }

    @Override
    public void onBindViewHolder(final OffreViewHolder holder, final int position) {

        holder.titre.setText(mDataSet.get(position).getName());
        holder.nblike.setText(mDataSet.get(position).getLike());
        holder.desc.setText(mDataSet.get(position).getType());

            holder.viewp.setVisibility(View.GONE);
        Long time = (Long) (System.currentTimeMillis());
        String result = (String) DateUtils.getRelativeTimeSpanString(mDataSet.get(position).getTime(), time, 0);
        //Toast.makeText(Detail_Annonce.this, result+"", Toast.LENGTH_SHORT).show()


        holder.time.setText(result);
        String[] img=mDataSet.get(position).getImages().split("#");
        Picasso.with(mcontext).load(img[0]).into(holder.image);

    holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(mcontext, DetailEvent.class);
                intent.putExtra("id",mDataSet.get(position).getId());
                mcontext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }


    public static class OffreViewHolder extends RecyclerView.ViewHolder {
        TextView titre,desc,nblike;
        ImageView image;
        LinearLayout item;
        LinearLayout viewp;
        TextView time;
        OffreViewHolder(View itemView) {
            super(itemView);
            desc=(TextView) itemView.findViewById(R.id.desc);
            titre = (TextView) itemView.findViewById(R.id.titre);
            image = (ImageView) itemView.findViewById(R.id.image);
            nblike= (TextView) itemView.findViewById(R.id.nblike);
            item= (LinearLayout) itemView.findViewById(R.id.item);
            viewp = (LinearLayout) itemView.findViewById(R.id.view);
            time= (TextView) itemView.findViewById(R.id.time);

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
