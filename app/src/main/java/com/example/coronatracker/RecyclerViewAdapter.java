package com.example.coronatracker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> implements Filterable {

    private List<CovidCountry> covidCountries;
    private List<CovidCountry> covidCountriesList;
    Context context;

    public RecyclerViewAdapter()
    {
        covidCountries = new ArrayList<>();
    }

    public void setData(ArrayList<CovidCountry> covidCountries)
    {
        this.covidCountries = covidCountries;
        covidCountriesList = new ArrayList<>(covidCountries);
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.list_of_countries,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CovidCountry covidCountry = covidCountries.get(position);

        holder.cardView.setAnimation(AnimationUtils.loadAnimation(context,R.anim.trans_animation));

        holder.mCountry.setText(covidCountry.getmCovidCountry());
        holder.mActive.setText(covidCountry.getmCases());
        holder.mRecovered.setText(covidCountry.getmRecovered());
        holder.mTodayCases.setText(covidCountry.getmTodayCases());
        holder.mCritical.setText(covidCountry.getmCritical());
        holder.mDeaths.setText(covidCountry.getmDeaths());

        Glide.with(context).load(covidCountry.getMflags()).apply(new RequestOptions().override(240,160) ).into(holder.imageView);

        boolean isExpanded = covidCountries.get(position).isExpanded();
        holder.expandableLayout.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
    }

    @Override
    public int getItemCount() {
        return covidCountries.size();
    }



    @Override
    public Filter getFilter() {
        return covidCountriesFilter;
    }

    private Filter covidCountriesFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            List<CovidCountry> filteredCovidCountry = new ArrayList<>();
            if(constraint == null || constraint.length()==0){
                filteredCovidCountry.addAll(covidCountriesList);
            }
            else{
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (CovidCountry itemCovidCountry: covidCountriesList){
                    if (itemCovidCountry.getmCovidCountry().toLowerCase().contains(filterPattern)){
                        filteredCovidCountry.add(itemCovidCountry);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredCovidCountry;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

            covidCountries.clear();
            covidCountries.addAll((List) results.values);
            notifyDataSetChanged();

        }
    };

    public class ViewHolder extends RecyclerView.ViewHolder  {

        TextView mCountry,mActive,mRecovered,mTodayCases,mCritical,mDeaths;
        CardView cardView;
        ImageView imageView;

        RelativeLayout expandableLayout,relativeLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mCountry = itemView.findViewById(R.id.tv_country);
            mActive = itemView.findViewById(R.id.tv_affected_list);
            mRecovered = itemView.findViewById(R.id.tv_recovery_list);
            mTodayCases = itemView.findViewById(R.id.tv_today_cases_list);
            mCritical = itemView.findViewById(R.id.tv_critical_list);
            mDeaths = itemView.findViewById(R.id.tv_total_deaths);

            expandableLayout = itemView.findViewById(R.id.expandable_layout);

            relativeLayout= itemView.findViewById(R.id.layout_title);
            cardView = itemView.findViewById(R.id.cv_main);
            imageView = itemView.findViewById(R.id.imageView2);

            relativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CovidCountry covidCountry = covidCountries.get(getAdapterPosition());
                    covidCountry.setExpanded(!covidCountry.isExpanded());
                    notifyItemChanged(getAdapterPosition());
                }
            });

        }

    }
}
