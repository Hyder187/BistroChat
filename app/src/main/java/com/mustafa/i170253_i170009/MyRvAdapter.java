package com.mustafa.i170253_i170009;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MyRvAdapter extends RecyclerView.Adapter<com.mustafa.i170253_i170009.MyRvAdapter.MyViewHolder> implements Filterable {

    List<Profile> list;
    List<Profile> filteredList;
    Context c;
    public MyRvAdapter(List<Profile> list, Context c) {
        this.c=c;
        this.list=list;
        this.filteredList = list;
    }

    @NonNull
    @Override
    public com.mustafa.i170253_i170009.MyRvAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(c).inflate(R.layout.contacts_row,parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull com.mustafa.i170253_i170009.MyRvAdapter.MyViewHolder holder, final int position) {
        holder.name.setText(filteredList.get(position).getName());
        Picasso.get().load(filteredList.get(position).getDp()).fit().centerCrop().into(holder.image);


        holder.cl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(c, MessageListActivity.class);
                intent.putExtra("profileName",filteredList.get(position).getName());
                intent.putExtra("id",filteredList.get(position).getId());
                intent.putExtra("image",filteredList.get(position).getDp());

                c.startActivity(intent);
            }

        });


    }

    @Override
    public int getItemCount() {
        return filteredList.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String Key = constraint.toString();
                if(Key.isEmpty()){
                    filteredList = list;
                }
                else{
                    List<Profile> listFiltered = new ArrayList<>();
                    for (Profile row: list){
                        if(row.getName().toLowerCase().contains(Key.toLowerCase())){
                            listFiltered.add(row);

                        }
                    }
                    filteredList = listFiltered;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredList;
                return filterResults;

            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filteredList =  (List<Profile>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        ConstraintLayout cl;
        ImageView image;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.name);
            image=itemView.findViewById(R.id.rowImage);
            cl=itemView.findViewById(R.id.cl);

        }
    }
}
