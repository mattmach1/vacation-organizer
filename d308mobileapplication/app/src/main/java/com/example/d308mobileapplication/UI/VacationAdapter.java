package com.example.d308mobileapplication.UI;

import android.content.Context;
import android.content.Intent;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.d308mobileapplication.R;
import com.example.d308mobileapplication.entities.Vacation;

import java.util.ArrayList;
import java.util.List;

public class VacationAdapter extends RecyclerView.Adapter<VacationAdapter.VacationViewHolder> implements Filterable {
    private List<Vacation> mVacations = new ArrayList<>();
    private List<Vacation> mVacationsFull = new ArrayList<>();
    private final Context context;
    private final LayoutInflater mInflater;
    public VacationAdapter(Context context){
        mInflater= LayoutInflater.from(context);
        this.context=context;
        mVacations = new ArrayList<>();
        mVacationsFull = new ArrayList<>();
    }

    public void setVacations(List<Vacation> vacations) {
        mVacations.clear();
        mVacations.addAll(vacations);

        mVacationsFull.clear();
        mVacationsFull.addAll(vacations);

        notifyDataSetChanged();
    }

    @Override
    public Filter getFilter() {
        return vacationFilter;
    }

    private final Filter vacationFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Vacation> filtered = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filtered.addAll(mVacationsFull);
            } else {
                String query = constraint.toString().toLowerCase().trim();
                for (Vacation v : mVacationsFull) {
                    if (v.getTitle().toLowerCase().contains(query)) {
                        filtered.add(v);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filtered;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            mVacations.clear();
            mVacations.addAll((List<Vacation>) results.values);
            notifyDataSetChanged();
        }
    };
    public class VacationViewHolder extends RecyclerView.ViewHolder {
        private final TextView vacationItemView;
        public VacationViewHolder(@NonNull View itemView) {

            super(itemView);
            vacationItemView=itemView.findViewById(R.id.textView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position=getAdapterPosition();
                    final Vacation current=mVacations.get(position);
                    Log.d("VacationAdapter", "Vacation ID: " + current.getVacationID());
                    Intent intent=new Intent(context,VacationDetails.class);
                    intent.putExtra("vacationID", current.getVacationID());
                    intent.putExtra("name", current.getTitle());
                    intent.putExtra("hotel", current.getHotel());
                    intent.putExtra("vacationStartDate", current.getStartDate());
                    intent.putExtra("vacationEndDate", current.getEndDate());
                    context.startActivity(intent);
                }
            });
        }
    }

    @NonNull
    @Override
    public VacationAdapter.VacationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView=mInflater.inflate(R.layout.vacation_list_item,parent,false);
        return new VacationViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull VacationAdapter.VacationViewHolder holder, int position) {
        if(mVacations!=null){
            Vacation current=mVacations.get(position);
            String name=current.getTitle();
            holder.vacationItemView.setText(name);
        }
        else{
            holder.vacationItemView.setText("No vacation name");
        }
    }

    @Override
    public int getItemCount() {
        if(mVacations!=null) {
            return mVacations.size();
        }
        else return 0;
    }
}
