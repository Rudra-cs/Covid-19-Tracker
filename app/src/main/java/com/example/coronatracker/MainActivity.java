package com.example.coronatracker;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private TextView tvTotalConfirmed, tvTotalDeaths, tvTotalRecovered,totalAffected;
    private ProgressBar progressBar;
    RecyclerView recyclerView;
    RecyclerViewAdapter adapter;
    ArrayList<CovidCountry> covidCountries;
    SwipeRefreshLayout swipeRefreshLayout;

    ImageButton mbtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mbtn = findViewById(R.id.info);
        tvTotalConfirmed = findViewById(R.id.tvTotalConfirmed);
        tvTotalDeaths = findViewById(R.id.tvTotalDeath);
        tvTotalRecovered = findViewById(R.id.tvTotalRecovered);
        totalAffected = findViewById(R.id.total_country);
        progressBar = findViewById(R.id.progress_circular_home);
        recyclerView = findViewById(R.id.rv_home);
        covidCountries = new ArrayList<>();
        LinearLayoutManager manager= new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        recyclerView.setHasFixedSize(true);
        adapter = new RecyclerViewAdapter();
        recyclerView.setAdapter(adapter);


        mbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                change();
            }
        });

        swipeRefreshLayout = findViewById(R.id.swipeRefresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                adapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        //Call Volley
        getData();
        moreData();
    }

    private void change() {
        Intent in = new Intent(this, InfoActivity.class);
        startActivity(in);

    }


    private void moreData(){

        String url = "https://corona.lmao.ninja/v2/countries";


        covidCountries = new ArrayList<>();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if (response != null) {
                    Log.e("Error", "onResponse: " + response);
                }
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject data = jsonArray.getJSONObject(i);

                        JSONObject countryInfo = data.getJSONObject("countryInfo");
                        covidCountries.add(new CovidCountry(data.getString("country"), data.getString("cases"),data.getString("recovered"),
                                countryInfo.getString("flag"),data.getString("todayCases"),data.getString("active"),data.getString("deaths")));
                    }
                    adapter.setData(covidCountries);
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Error","onResponse: "+ error);

                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }


    private void getData() {
        RequestQueue queue= Volley.newRequestQueue(this);

        String url =" https://corona.lmao.ninja/v2/all";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressBar.setVisibility(View.GONE);

                try {
                    JSONObject jsonObject = new JSONObject(response.toString());

                    tvTotalConfirmed.setText(jsonObject.getString("cases"));
                    tvTotalDeaths.setText(jsonObject.getString("deaths"));
                    tvTotalRecovered.setText(jsonObject.getString("recovered"));
                    totalAffected.setText(jsonObject.getString("affectedCountries"));
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("ERROR","JSON NOT PARSING");
                }
            }
        }, new Response.ErrorListener(){


            @Override
            public void onErrorResponse(VolleyError error) {

                progressBar.setVisibility(View.GONE);
                Log.d("Error Response", error.toString());
                Toast.makeText(getApplicationContext(),"Something Went Wrong",Toast.LENGTH_SHORT).show();
            }

        });
        queue.add(stringRequest);
    }

    @Override
    public boolean onCreateOptionsMenu( Menu menu) {
        MenuInflater inflater =getMenuInflater();
        inflater.inflate(R.menu.search_menu,menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });
        return true;
    }
}
