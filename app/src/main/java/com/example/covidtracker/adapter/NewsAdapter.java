package com.example.covidtracker.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.covidtracker.Model.NewsModel;
import com.example.covidtracker.NewsWebView;
import com.example.covidtracker.R;

import java.util.ArrayList;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsAdapterViewHolder> {
    ArrayList<NewsModel> models;
    Context context;

    public NewsAdapter(ArrayList<NewsModel> models, Context context) {
        this.models = models;
        this.context = context;
    }

    @NonNull
    @Override
    public NewsAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.news_card,parent,false);

        return new NewsAdapterViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsAdapterViewHolder holder, int position) {
        final NewsModel item=models.get(position);
        Glide.with(context).load(item.getUrlToImage()).skipMemoryCache(false).diskCacheStrategy(DiskCacheStrategy.AUTOMATIC).into(holder.image);
        holder.title.setText(item.getTitle());
        holder.description.setText(item.getDescription());
        holder.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, item.getUrl());
                sendIntent.setType("text/plain");

                Intent shareIntent = Intent.createChooser(sendIntent, null);
                v.getContext().startActivity(shareIntent);

            }
        });
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, NewsWebView.class);
                intent.putExtra("url",item.getUrl());
                v.getContext().startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    class  NewsAdapterViewHolder extends RecyclerView.ViewHolder{
        ImageView image;
        TextView title,description;
        Button share;
        CardView cardView;
        public NewsAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            image=itemView.findViewById(R.id.image);
            title=itemView.findViewById(R.id.title);
            description=itemView.findViewById(R.id.description);
            share=itemView.findViewById(R.id.sharebutton);
            cardView=itemView.findViewById(R.id.newscardView);
        }
    }
}
