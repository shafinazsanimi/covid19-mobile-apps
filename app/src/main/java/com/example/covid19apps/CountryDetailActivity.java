package com.example.covid19apps;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

public class CountryDetailActivity extends AppCompatActivity {

    private int positionCountry;
    TextView country, cases, active, recovered, critical, deaths, newCases, newDeaths;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_country_detail);

        Intent intent = getIntent();
        positionCountry = intent.getIntExtra("position", 0);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Details of "+AffectedCountries.countryList.get(positionCountry).getCountry());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        country = findViewById(R.id.tvCountry);
        cases = findViewById(R.id.tvCases);
        active = findViewById(R.id.tvActive);
        recovered = findViewById(R.id.tvRecovered);
        critical = findViewById(R.id.tvCritical);
        deaths = findViewById(R.id.tvDeaths);
        newCases = findViewById(R.id.tvNewCases);
        newDeaths = findViewById(R.id.tvNewDeaths);

        country.setText(AffectedCountries.countryList.get(positionCountry).getCountry());
        cases.setText(AffectedCountries.countryList.get(positionCountry).getCases());
        active.setText(AffectedCountries.countryList.get(positionCountry).getActive());
        recovered.setText(AffectedCountries.countryList.get(positionCountry).getRecovered());
        critical.setText(AffectedCountries.countryList.get(positionCountry).getCritical());
        deaths.setText(AffectedCountries.countryList.get(positionCountry).getDeaths());
        newCases.setText(AffectedCountries.countryList.get(positionCountry).getNewCases());
        newDeaths.setText(AffectedCountries.countryList.get(positionCountry).getNewDeaths());
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()==android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }
}
