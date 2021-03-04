package com.example.covid19apps;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.leo.simplearcloader.SimpleArcLoader;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;
import org.json.JSONException;
import org.json.JSONObject;

public class TrackerFragment extends Fragment {

    TextView cases, active, recovered, critical, deaths, newCases, newDeaths, affectedCountries;
    SimpleArcLoader saLoader;
    ScrollView scView;
    PieChart pChart;
    Button buttonTrack;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_tracker, container, false);

        cases = fragmentView.findViewById(R.id.tvCases);
        active = fragmentView.findViewById(R.id.tvActive);
        recovered = fragmentView.findViewById(R.id.tvRecovered);
        critical = fragmentView.findViewById(R.id.tvCritical);
        deaths = fragmentView.findViewById(R.id.tvDeaths);
        newCases = fragmentView.findViewById(R.id.tvNewCases);
        newDeaths = fragmentView.findViewById(R.id.tvNewDeaths);
        affectedCountries = fragmentView.findViewById(R.id.tvAffectedCountries);

        saLoader = fragmentView.findViewById(R.id.loader_data);
        scView = fragmentView.findViewById(R.id.scrollStatus);
        pChart = fragmentView.findViewById(R.id.pieChart);

        fetchData();

        buttonTrack = fragmentView.findViewById(R.id.track_button);
        buttonTrack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goTrackCountries();
            }
        });

        return fragmentView;
    }

    private void fetchData() {
        String url = "https://disease.sh/v3/covid-19/all";

        saLoader.start();
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.toString());

                    cases.setText(jsonObject.getString("cases"));
                    active.setText(jsonObject.getString("active"));
                    recovered.setText(jsonObject.getString("recovered"));
                    critical.setText(jsonObject.getString("critical"));
                    deaths.setText(jsonObject.getString("deaths"));
                    newCases.setText(jsonObject.getString("todayCases"));
                    newDeaths.setText(jsonObject.getString("todayDeaths"));
                    affectedCountries.setText(jsonObject.getString("affectedCountries"));

                    pChart.addPieSlice(new PieModel("Cases", Integer.parseInt(cases.getText().toString()), Color.parseColor("#D8D77D")));
                    pChart.addPieSlice(new PieModel("Active", Integer.parseInt(active.getText().toString()), Color.parseColor("#8FAFE0")));
                    pChart.addPieSlice(new PieModel("Recovered", Integer.parseInt(recovered.getText().toString()), Color.parseColor("#7DD8C5")));
                    pChart.addPieSlice(new PieModel("Deaths", Integer.parseInt(deaths.getText().toString()), Color.parseColor("#E08FB1")));
                    pChart.startAnimation();

                    saLoader.stop();
                    saLoader.setVisibility(View.GONE);
                    scView.setVisibility(View.VISIBLE);
                }
                catch (JSONException e) {
                    e.printStackTrace();
                    saLoader.stop();
                    saLoader.setVisibility(View.GONE);
                    scView.setVisibility(View.VISIBLE);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                saLoader.stop();
                saLoader.setVisibility(View.GONE);
                scView.setVisibility(View.VISIBLE);
                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(request);
    }

    public void goTrackCountries(){
        Intent intent = new Intent(getContext(), AffectedCountries.class);
        startActivity(intent);
    }
    
}
