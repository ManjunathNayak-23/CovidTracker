package com.example.covidtracker;

import android.content.Intent;
import android.icu.text.NumberFormat;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.covidtracker.Model.FavouritedCountryModel;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.leo.simplearcloader.SimpleArcLoader;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    TextView totalDeath, totalRecovered, totalCases, updated;
    String url = "https://corona.lmao.ninja/v3/covid-19/all";
    static ArrayList<FavouritedCountryModel> models = new ArrayList<>();
    SimpleArcLoader simpleArcLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //   getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        totalDeath = findViewById(R.id.deathTv);
        totalRecovered = findViewById(R.id.recoveredTv);
        totalCases = findViewById(R.id.confirmedTv);
        updated = findViewById(R.id.updatedTv);
        simpleArcLoader = findViewById(R.id.simple_arc);


        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.dashboard);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.dashboard:
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
                        Intent intent3 = new Intent(getApplicationContext(), News.class);
                        startActivity(intent3);
                        finish();
                        overridePendingTransition(0, 0);
                        return true;
                }
                return false;
            }
        });

        fetchdata();
    }

    private void fetchdata() {
        simpleArcLoader.start();
        final StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.toString());
                    Integer totcases = Integer.parseInt(jsonObject.getString("cases"));
                    Integer updatedcases = Integer.parseInt(jsonObject.getString("active"));
                    Integer totDeath = Integer.parseInt(jsonObject.getString("deaths"));
                    Integer totRecovered = Integer.parseInt(jsonObject.getString("recovered"));


                    totalCases.setText(String.valueOf(NumberFormat.getNumberInstance(Locale.US).format(totcases)));
                    updated.setText(String.valueOf(NumberFormat.getNumberInstance(Locale.US).format(updatedcases)));
                    totalDeath.setText(String.valueOf(NumberFormat.getNumberInstance(Locale.US).format(totDeath)));
                    totalRecovered.setText(String.valueOf(NumberFormat.getNumberInstance(Locale.US).format(totRecovered)));
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
                Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }

}