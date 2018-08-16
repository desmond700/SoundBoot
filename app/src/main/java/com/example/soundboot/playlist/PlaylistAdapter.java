package com.example.soundboot.playlist;

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

public class PlaylistAdapter extends RecyclerView.Adapter<PlaylistAdapter.ViewHolder> {

    private final String TAG = "TrackAdapter";
    private Context context;
    private ArrayList<PlaylistModel> playlistModelArrayList;
    public static final String EXTRA_MESSAGE = "com.example.soundboot.MESSAGE";

    public PlaylistAdapter(Context context, ArrayList<PlaylistModel> playlistModelArrayList) {
        this.context = context;
        this.playlistModelArrayList = playlistModelArrayList;
    }

    @NonNull
    @Override
    public PlaylistAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_list_item, parent, false);
        PlaylistAdapter.ViewHolder holder = new PlaylistAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull PlaylistAdapter.ViewHolder viewHolder, final int position) {
        Log.d(TAG,"onBindViewHolder: called");
        if(playlistModelArrayList.get(position).getImageUri() != null){
            Picasso.get()
                    .load(playlistModelArrayList.get(position).getImageUri())
                    .resize(350,350)
                    .into(viewHolder.image);
        }

        viewHolder.name.setText(playlistModelArrayList.get(position).getName());
        viewHolder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Status", "onClick clicked on: " + playlistModelArrayList.get(position).getName());
                Intent intent = new Intent(context, PlayerActivity.class);
                String playlistUri = playlistModelArrayList.get(position).getPlaylistUri();
                intent.putExtra(EXTRA_MESSAGE, playlistUri);
                view.getContext().startActivity(intent);
            }
        });
    }



    @Override
    public int getItemCount() {
        return playlistModelArrayList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        ImageView image;
        LinearLayout parentLayout;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.list_name);
            image = itemView.findViewById(R.id.list_img);
            parentLayout = itemView.findViewById(R.id.list);
        }
    }
}
