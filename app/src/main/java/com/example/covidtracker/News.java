package com.example.covidtracker;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.covidtracker.Model.NewsModel;
import com.example.covidtracker.adapter.NewsAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.leo.simplearcloader.SimpleArcLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class News extends AppCompatActivity {
    RecyclerView recyclerView;
    String url = "http://newsapi.org/v2/top-headlines?country=in&category=health&apiKey=47f4287bd2824fa4a835a267455e0546";
    ArrayList<NewsModel> arrayList = new ArrayList<>();
    NewsModel model;
    SimpleArcLoader simpleArcLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //   getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_news);
        recyclerView = findViewById(R.id.newsRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        simpleArcLoader = findViewById(R.id.simple_arc);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.news);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.dashboard:
                        Intent intent3 = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent3);
                        finish();
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.location:
                        Intent intent = new Intent(getApplicationContext(), AffectedCountries.class);
                        startActivity(intent);
                        finish();
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.emergency:
                        Intent intent1 = new Intent(getApplicationContext(), Emergency.class);
                        startActivity(intent1);
                        finish();
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.news:

                        return true;
                }
                return false;
            }
        });
        fetchNews();

    }

    private void fetchNews() {
        simpleArcLoader.start();
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.toString());
                    JSONArray jsonArray = jsonObject.getJSONArray("articles");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        String title = jsonObject1.getString("title");
                        String imageurl = jsonObject1.getString("urlToImage");
                        String desc = jsonObject1.getString("description");
                        String url = jsonObject1.getString("url");

                        if (title.equals("null")) {
                            title = "Not Provided";
                        }
                        if (desc.equals("null")) {
                            desc = "Not provided";
                        }
                        model = new NewsModel(title, desc, url, imageurl);
                        arrayList.add(model);

                    }
                    NewsAdapter adapter = new NewsAdapter(arrayList, News.this);
                    recyclerView.setAdapter(adapter);
                    simpleArcLoader.stop();
                    simpleArcLoader.setVisibility(View.GONE);

                } catch (JSONException e) {
                    e.printStackTrace();
                    simpleArcLoader.stop();
                    simpleArcLoader.setVisibility(View.GONE);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(News.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                simpleArcLoader.stop();
                simpleArcLoader.setVisibility(View.GONE);
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);

    }
}