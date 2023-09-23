package com.example.wastelocator;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class BinAdapter extends RecyclerView.Adapter<BinAdapter.BinViewHolder> {
    private ArrayList<Bin> binList;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }
    private OnItemClickListener mListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public BinAdapter(ArrayList<Bin> vehicleList) {
        this.binList = vehicleList;
    }
    @NonNull
    @Override
    public BinViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bin, parent, false);
        return new BinViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(@NonNull BinViewHolder holder, int position) {
        Bin bin = binList.get(position);
        holder.binId.setText(bin.getId());
        holder.binVolume.setText(bin.getVolume());
//        holder.binLatitude.setText(bin.getLatitude());
//        holder.binLongitude.setText(bin.getLongitude());
    }
    @Override
    public int getItemCount() {
        return binList.size();
    }
    public class BinViewHolder extends RecyclerView.ViewHolder
    {
        TextView binId;
        TextView binVolume;
//        TextView binLatitude;
//        TextView binLongitude;
        public BinViewHolder(@NonNull View itemView) {super(itemView);
            binId = itemView.findViewById(R.id.binId);
            binVolume = itemView.findViewById(R.id.binVolume);
//            binLatitude = itemView.findViewById(R.id.binLatitude);
//            binLongitude = itemView.findViewById(R.id.binLongitude);


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
