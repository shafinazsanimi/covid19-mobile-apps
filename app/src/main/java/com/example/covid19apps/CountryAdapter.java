package com.example.covid19apps;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class CountryAdapter extends ArrayAdapter<Country> {

    private Context context;
    private List<Country> countryList;
    private List<Country> countryListFiltered;

    public CountryAdapter(Context context, List<Country> countryList) {
        super(context, R.layout.list_country_item, countryList);

        this.context = context;
        this.countryList = countryList;
        this.countryListFiltered = countryList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_country_item, null, true);
        ImageView countryFlag = view.findViewById(R.id.country_flag);
        TextView countryName = view.findViewById(R.id.country_name);

        Glide.with(context).load(countryListFiltered.get(position).getFlag()).into(countryFlag);
        countryName.setText(countryListFiltered.get(position).getCountry());

        return view;
    }

    @Override
    public int getCount() {
        return countryListFiltered.size();
    }

    @Nullable
    @Override
    public Country getItem(int position) {
        return countryListFiltered.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                if (constraint == null || constraint.length() == 0) {
                    filterResults.count = countryList.size();
                    filterResults.values = countryList;
                }
                else {
                    List<Country> results = new ArrayList<>();
                    String searchString = constraint.toString().toLowerCase();

                    for (Country items:countryList) {
                        if (items.getCountry().toLowerCase().contains(searchString)) {
                            results.add(items);
                        }
                        filterResults.count = results.size();
                        filterResults.values = results;
                    }
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                countryListFiltered = (List<Country>) results.values;
                AffectedCountries.countryList = (List<Country>) results.values;
                notifyDataSetChanged();
            }
        };
        return filter;
    }

}
