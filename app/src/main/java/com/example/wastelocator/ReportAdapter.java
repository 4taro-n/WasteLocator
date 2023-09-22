package com.example.wastelocator;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
public class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.ReportViewHolder> {
    private ArrayList<Report> reportList;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }
    private OnItemClickListener mListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public ReportAdapter(ArrayList<Report> vehicleList) {
        this.reportList = vehicleList;
    }
    @NonNull
    @Override
    public ReportViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_report, parent, false);
        return new ReportViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(@NonNull ReportViewHolder holder, int position) {
        Report report = reportList.get(position);
        holder.reportTitle.setText(report.getTitle());
        holder.reportDescription.setText(report.getDescription());
    }
    @Override
    public int getItemCount() {
        return reportList.size();
    }
    public class ReportViewHolder extends RecyclerView.ViewHolder
    {
        TextView reportTitle;
        TextView reportDescription;
        public ReportViewHolder(@NonNull View itemView) {super(itemView);
            reportTitle = itemView.findViewById(R.id.reportTitle);
            reportDescription = itemView.findViewById(R.id.reportDescription);

            itemView.setOnClickListener(v -> {
                if (mListener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        mListener.onItemClick(position);
                    }
                }
            });
        }
    }
}