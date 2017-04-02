package com.example.keniel.test;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import static android.R.id.list;

/**
 * Created by Keniel on 12/17/2016.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private List<ForumItems> items;
    private Context context;

    public RecyclerAdapter(List<ForumItems> items, Context context) {
        this.items = items;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final ForumItems item = items.get(position);
        holder.title.setText(item.getTitle());
        holder.content.setText(item.getContent());

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle data = new Bundle();
                data.putString("title",item.getTitle());
                data.putDouble("latitude",item.getLat());
                data.putDouble("longitude",item.getLng());
                FireLocateFragment fragment = new FireLocateFragment();
                fragment.setArguments(data);
                FragmentManager fragmentManager =  ((Activity) context).getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.forumfragment, fragment).commit();
            }
        });
    }


    @Override
    public int getItemCount() {

        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public LinearLayout linearLayout;
        public TextView title;
        public TextView content;

        public ViewHolder(View itemView) {
            super(itemView);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.linearLayout);
            title = (TextView) itemView.findViewById(R.id.title);
            content = (TextView) itemView.findViewById(R.id.content);
        }
    }
}
