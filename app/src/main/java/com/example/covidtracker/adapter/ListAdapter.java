package com.example.covidtracker.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.covidtracker.DetailScreen;
import com.example.covidtracker.Model.CountryName;
import com.example.covidtracker.R;

import java.util.ArrayList;
import java.util.List;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ListAdapterViewHolder> {
    private Context context;
    private List<CountryName> countryNames;


    public ListAdapter(Context context, List<CountryName> countryNames) {
        this.context = context;
        this.countryNames = countryNames;
    }

    @NonNull
    @Override
    public ListAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_list_view, parent, false);
        return new ListAdapterViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ListAdapterViewHolder holder, final int position) {
        holder.countryName.setText(countryNames.get(position).getCountry());
        if(position%2==0){
            holder.cardView.setCardBackgroundColor(Color.parseColor("#60BE93"));
        }else{
            holder.cardView.setCardBackgroundColor(Color.parseColor("#1B8D59"));
        }
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailScreen.class);
                intent.putExtra("name", countryNames.get(position).getCountry());
                v.getContext().startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return countryNames.size();
    }
    public void filterList(ArrayList<CountryName> filteredList){
        countryNames=filteredList;
    notifyDataSetChanged();
    }

    class ListAdapterViewHolder extends RecyclerView.ViewHolder {
        TextView countryName;
        CardView cardView;

        public ListAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            countryName = itemView.findViewById(R.id.countryname);
            cardView = itemView.findViewById(R.id.cardView);
        }
    }
}
