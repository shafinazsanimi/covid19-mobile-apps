package com.example.covid19apps;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.leo.simplearcloader.SimpleArcLoader;

import org.eazegraph.lib.models.PieModel;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AffectedCountries extends AppCompatActivity {

    EditText search;
    ListView listView;
    SimpleArcLoader saLoader;

    public static List<Country> countryList = new ArrayList<>();
    Country country;
    CountryAdapter countryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_affected_countries);

        search = findViewById(R.id.search_country);
        listView = findViewById(R.id.country_list);

        saLoader = findViewById(R.id.loader_country);
        
        fetchData();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(getApplicationContext(), CountryDetailActivity.class).putExtra("position", position));
            }
        });

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                countryAdapter.getFilter().filter(s);
                countryAdapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void fetchData() {
        String url = "https://disease.sh/v3/covid-19/countries";

        saLoader.start();
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);

                    for (int i=0; i<jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        String countryName = jsonObject.getString("country");
                        String cases = jsonObject.getString("cases");
                        String active = jsonObject.getString("active");
                        String recovered = jsonObject.getString("recovered");
                        String critical = jsonObject.getString("critical");
                        String deaths = jsonObject.getString("deaths");
                        String newCases = jsonObject.getString("todayCases");
                        String newDeaths = jsonObject.getString("todayDeaths");

                        JSONObject object = jsonObject.getJSONObject("countryInfo");
                        String flagURL = object.getString("flag");

                        country = new Country(flagURL, countryName, cases, active, recovered, critical, deaths, newCases, newDeaths);
                        countryList.add(country);
                    }
                    countryAdapter = new CountryAdapter(AffectedCountries.this, countryList);
                    listView.setAdapter(countryAdapter);
                    saLoader.stop();
                    saLoader.setVisibility(View.GONE);
                }
                catch (JSONException e) {
                    e.printStackTrace();
                    saLoader.stop();
                    saLoader.setVisibility(View.GONE);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                saLoader.stop();
                saLoader.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }

}
