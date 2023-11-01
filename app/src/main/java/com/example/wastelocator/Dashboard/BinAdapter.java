package com.example.wastelocator.Dashboard;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wastelocator.R;

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

    public BinAdapter(ArrayList<Bin> bins) {
        this.binList = bins;
    }

    @NonNull
    @Override
    public BinViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bin, parent, false);
        return new BinViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull BinViewHolder holder, int position) {
        Bin bin = binList.get(position);
        holder.binId.setText(String.valueOf(bin.getId()));
        holder.binVolume.setText(String.valueOf(bin.getFillLevel()) + "%");
    }

    @Override
    public int getItemCount() {
        return binList.size();
    }

    public class BinViewHolder extends RecyclerView.ViewHolder {
        TextView binId;
        TextView binVolume;

        public BinViewHolder(@NonNull View itemView) {
            super(itemView);
                binId = itemView.findViewById(R.id.binId);
                binVolume = itemView.findViewById(R.id.binVolume);

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

