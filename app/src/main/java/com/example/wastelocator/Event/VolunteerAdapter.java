package com.example.wastelocator.Event;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wastelocator.DB.Volunteer;
import com.example.wastelocator.R;

import java.util.ArrayList;

public class VolunteerAdapter extends RecyclerView.Adapter<VolunteerAdapter.VolunteerViewHolder> {

    ArrayList<Volunteer> volunteerArrayList = new ArrayList<>();
    public VolunteerAdapter(ArrayList<Volunteer> volunteers) {
        this.volunteerArrayList = volunteers;
    }

    @NonNull
    @Override
    public VolunteerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_volunteer, parent, false);
        return new VolunteerViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull VolunteerViewHolder holder, int position) {
        Volunteer volunteer = volunteerArrayList.get(position);
        holder.name.setText(volunteer.getName());
        holder.email.setText(volunteer.getEmail());
        holder.mobileNum.setText(volunteer.getMobileNum());
        // Bind other volunteer data as needed
    }

    @Override
    public int getItemCount() {
        return volunteerArrayList.size();
    }

    public class VolunteerViewHolder extends RecyclerView.ViewHolder {
        TextView name, email, mobileNum;

        public VolunteerViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.item_volunteer_name);
            email = itemView.findViewById(R.id.item_volunteer_email);
            mobileNum = itemView.findViewById(R.id.item_volunteer_phone);
        }
    }
}
