package com.example.wastelocator.Event;


import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.wastelocator.DB.Event;
import com.example.wastelocator.R;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {

    ArrayList<Event> eventArrayList = new ArrayList<>();
    private OnItemClickListener mListener;

    // constructor
    public EventAdapter(ArrayList<Event> eventArrayList) {
        this.eventArrayList = eventArrayList;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public interface OnItemClickListener {
        void OnItemClick(int position);
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from((parent.getContext())).inflate(R.layout.item_event, parent, false);
        return new EventViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        Event event = eventArrayList.get(position);
        // format date
        LocalDate localDate = event.getEventDate();
        LocalTime localTime = event.getEventTime();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh:mm a");

        holder.eventName.setText(event.getEventName());
        holder.eventDate.setText(localDate.format(dateFormatter));
        holder.eventTime.setText(localTime.format(timeFormatter));
        // Check if image is not null and then bind it
        if (event.getImageUri() != null && !event.getImageUri().isEmpty()) {
            holder.bindImage(Uri.parse(event.getImageUri()));
        }
        // Bind other event data as needed
    }

    @Override
    public int getItemCount() {
        return eventArrayList.size();
    }

    // initialise View (UI)
    public class EventViewHolder extends RecyclerView.ViewHolder {
        ImageView eventImage;
        TextView eventName, eventDate, eventTime, participants;
        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
            eventImage = itemView.findViewById(R.id.item_event_image);
            eventName = itemView.findViewById(R.id.item_event_name);
            eventDate = itemView.findViewById(R.id.item_event_date);
            eventTime = itemView.findViewById(R.id.item_event_time);
            participants = itemView.findViewById(R.id.item_event_participants);

            itemView.setOnClickListener(view -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    mListener.OnItemClick(position);
                }
            });
        }

        // handle loading of the image using its Uri.
        public void bindImage(Uri imageUri) {
            Glide.with(itemView.getContext()).load(imageUri).into(eventImage);
        }
    }
}
