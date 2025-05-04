package com.example.d308mobileapplication.UI;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.example.d308mobileapplication.R;
import com.example.d308mobileapplication.entities.LogEntry;

import java.util.ArrayList;
import java.util.List;


import android.widget.Filter;




public class LogAdapter extends RecyclerView.Adapter<LogAdapter.LogViewHolder> implements Filterable {

    private List<LogEntry> logs = new ArrayList<>();
    private List<LogEntry> logsFull = new ArrayList<>();

    public void setLogs(List<LogEntry> list) {
        logs.clear();
        logs.addAll(list);
        logsFull.clear();
        logsFull.addAll(list);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public LogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.log_list_item, parent, false);
        return new LogViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull LogViewHolder holder, int pos) {
        LogEntry e = logs.get(pos);
        holder.id.setText(String.valueOf(e.getLogId()));
        holder.msg.setText(e.getMessage());
        holder.time.setText(e.getTimestamp());
    }

    @Override
    public int getItemCount() {
        return logs.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                List<LogEntry> filtered = new ArrayList<>();
                if (constraint == null || constraint.length() == 0) {
                    filtered.addAll(logsFull);
                } else {
                    String query = constraint.toString().toLowerCase().trim();
                    for (LogEntry e : logsFull) {
                        if (e.getMessage().toLowerCase().contains(query)
                        || e.getTimestamp().toLowerCase().contains(query)
                        || String.valueOf(e.getLogId()).contains(query)) {
                            filtered.add(e);
                        }
                    }
                }
                Filter.FilterResults results = new FilterResults();
                results.values = filtered;
                return results;
            }
            @Override
            @SuppressWarnings("unchecked")
            protected void publishResults(CharSequence constraint, FilterResults results) {
                logs.clear();
                logs.addAll((List<LogEntry>) results.values);
                notifyDataSetChanged();
            }
        };
    }
    class LogViewHolder extends RecyclerView.ViewHolder {
        TextView id, msg, time;
        LogViewHolder(View itemView) {
            super(itemView);
            id = itemView.findViewById(R.id.tvLogId);
            msg = itemView.findViewById(R.id.tvLogMessage);
            time = itemView.findViewById(R.id.tvLogTime);
        }
    }
}
