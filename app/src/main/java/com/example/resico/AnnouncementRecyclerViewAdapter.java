package com.example.resico;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AnnouncementRecyclerViewAdapter extends RecyclerView.Adapter<AnnouncementRecyclerViewAdapter.MyViewHolder> {
    Context context;
    ArrayList<AnnouncementModel> announcementModels;
    private final AnnouncementRecyclerViewInterface announcementRecyclerViewInterface;


    public AnnouncementRecyclerViewAdapter(Context context, ArrayList<AnnouncementModel> announcementModels, AnnouncementRecyclerViewInterface announcementRecyclerViewInterface){
        this.context = context;
        this.announcementModels = announcementModels;
        this.announcementRecyclerViewInterface = announcementRecyclerViewInterface;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // This is where you inflate the layout (Giving a look to our rows)
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.announcement_list_item, parent, false);
        return new MyViewHolder(view, announcementRecyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        //assigning values to views we created in the recycler_view_row layout file
        // based on the position of the recycler view
        holder.title.setText(announcementModels.get(position).getAnnouncementTitle());
        holder.date.setText(announcementModels.get(position).announcementDate);

    }

    @Override
    public int getItemCount() {
        // the recycler view just wants to know the number of items you want displayed
        return announcementModels.size();
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder{
        // grabbing the view from our recycler_view_row layout file
        // like in the onCreate method

        TextView title, date;

        public MyViewHolder(@NonNull View itemView, AnnouncementRecyclerViewInterface announcementRecyclerViewInterface) {
            super(itemView);
            title = itemView.findViewById(R.id.announcementTitleView);
            date = itemView.findViewById(R.id.announcementDateView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (announcementRecyclerViewInterface != null){
                        int pos = getAdapterPosition();
                        if (pos != RecyclerView.NO_POSITION){
                            announcementRecyclerViewInterface.onItemClick(pos);
                        }
                    }
                }
            });

        }
    }
}
