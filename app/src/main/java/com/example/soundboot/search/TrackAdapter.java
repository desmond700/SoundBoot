package com.example.soundboot.search;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.soundboot.R;
import com.example.soundboot.player.PlayerActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Parsania Hardik on 03-Jan-17.
 */
public class TrackAdapter extends RecyclerView.Adapter<TrackAdapter.ViewHolder> {
    private final String TAG = "TrackAdapter";
    private Context context;
    private ArrayList<ArtistModel> artistModelArrayList;
    public static final String EXTRA_MESSAGE = "com.example.soundboot.MESSAGE";

    public TrackAdapter(Context context, ArrayList<ArtistModel> artistModelArrayList) {
        this.context = context;
        this.artistModelArrayList = artistModelArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_items, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int position) {
        Log.d(TAG,"onBindViewHolder: called");
        Picasso.get()
                .load(artistModelArrayList.get(position).getImageUrl())
                .resize(80,80)
                .into(viewHolder.image);
        viewHolder.tvname.setText(artistModelArrayList.get(position).getName());
        viewHolder.tvtrack.setText(artistModelArrayList.get(position).getTrackTitle());
        viewHolder.tvtracklength.setText(artistModelArrayList.get(position).getTrackDuration());
        viewHolder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Status", "onClick clicked on: " + artistModelArrayList.get(position).getName());
                Intent intent = new Intent(context, PlayerActivity.class);
                String trackUri = artistModelArrayList.get(position).getTrackUrl();
                intent.putExtra(EXTRA_MESSAGE, trackUri);
                view.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return artistModelArrayList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvname;
        TextView tvtrack;
        TextView tvtracklength;
        ImageView image;
        LinearLayout parentLayout;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvname = itemView.findViewById(R.id.name);
            tvtrack = itemView.findViewById(R.id.track);
            tvtracklength = itemView.findViewById(R.id.track_length);
            image = itemView.findViewById(R.id.imageView3);
            parentLayout = itemView.findViewById(R.id.parent_layout);
        }
    }
 
}