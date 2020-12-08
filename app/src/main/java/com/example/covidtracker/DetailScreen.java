package com.example.covidtracker;

import android.content.Intent;
import android.graphics.Color;
import android.icu.text.NumberFormat;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.covidtracker.Model.CountryName;
import com.leo.simplearcloader.SimpleArcLoader;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DetailScreen extends AppCompatActivity {
    List<CountryName> list = new ArrayList<>();
    String url;
    String name;
    PieChart pieChart;
    SimpleArcLoader simpleArcLoader;
    TextView totalCases, recoveredCases, totalDeaths, totalActive, casesToday, deathToday, casesPerMillion, criticalPerMillion, recoveredPerMillion, textname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_detail_screen);
        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        pieChart = findViewById(R.id.pieChart);
        textname = findViewById(R.id.country);
        textname.setText(name);
        list = AffectedCountries.countryModelList;
        simpleArcLoader = findViewById(R.id.simple_arc);

        totalActive = findViewById(R.id.activecases);
        totalCases = findViewById(R.id.totalcases);
        recoveredCases = findViewById(R.id.recoveredcases);
        totalDeaths = findViewById(R.id.deathcases);
        casesToday = findViewById(R.id.today_cases);
        deathToday = findViewById(R.id.death);
        casesPerMillion = findViewById(R.id.casespermillion);
        criticalPerMillion = findViewById(R.id.criticalpermillion);
        recoveredPerMillion = findViewById(R.id.recoveredPerOneMillion);


        Fetch();
    }

    private void Fetch() {
        simpleArcLoader.start();
        url = "https://corona.lmao.ninja/v3/covid-19/countries/" + name;
        final StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.toString());
                    int cases = Integer.parseInt(jsonObject.getString("cases"));
                    int todayCases = Integer.parseInt(jsonObject.getString("todayCases"));
                    int deaths = Integer.parseInt(jsonObject.getString("deaths"));
                    int todayDeaths = Integer.parseInt(jsonObject.getString("todayDeaths"));
                    int recovered = Integer.parseInt(jsonObject.getString("recovered"));


                    int active = Integer.parseInt(jsonObject.getString("active"));
                    int casePerMillion = Integer.parseInt(jsonObject.getString("casesPerOneMillion"));
                    String criticalMillion = jsonObject.getString("criticalPerOneMillion");
                    String recoveredMillion = jsonObject.getString("recoveredPerOneMillion");
                    pieChart.addPieSlice(new PieModel("Cases", cases, Color.parseColor("#FFA726")));
                    pieChart.addPieSlice(new PieModel("Recovered", recovered, Color.parseColor("#66BB6A")));
                    pieChart.addPieSlice(new PieModel("Death", deaths, Color.parseColor("#EF5350")));
                    pieChart.addPieSlice(new PieModel("Active", active, Color.parseColor("#29B6F6")));
                    pieChart.startAnimation();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        totalActive.setText(String.valueOf(NumberFormat.getNumberInstance(Locale.US).format(active)));
                        totalActive.setText(String.valueOf(NumberFormat.getNumberInstance(Locale.US).format(active)));
                        totalCases.setText(String.valueOf(NumberFormat.getNumberInstance(Locale.US).format(cases)));
                        totalDeaths.setText(String.valueOf(NumberFormat.getNumberInstance(Locale.US).format(deaths)));
                        recoveredCases.setText(String.valueOf(NumberFormat.getNumberInstance(Locale.US).format(recovered)));
                        casesToday.setText(String.valueOf(NumberFormat.getNumberInstance(Locale.US).format(todayCases)));
                        deathToday.setText(String.valueOf(NumberFormat.getNumberInstance(Locale.US).format(todayDeaths)));
                        casesPerMillion.setText(String.valueOf(NumberFormat.getNumberInstance(Locale.US).format(casePerMillion)));
                        recoveredPerMillion.setText(recoveredMillion);
                        criticalPerMillion.setText(criticalMillion);
                    }else{
                        totalActive.setText(String.valueOf(active));
                        totalCases.setText(String.valueOf(cases));
                        totalDeaths.setText(String.valueOf(deaths));
                        recoveredCases.setText(String.valueOf(recovered));
                        casesToday.setText(String.valueOf(todayCases));
                        deathToday.setText(String.valueOf(todayDeaths));
                        casesPerMillion.setText(String.valueOf(casePerMillion));
                        recoveredPerMillion.setText(recoveredMillion);
                        criticalPerMillion.setText(criticalMillion);
                    }

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
                Toast.makeText(DetailScreen.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                simpleArcLoader.stop();
                simpleArcLoader.setVisibility(View.GONE);

            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }
}