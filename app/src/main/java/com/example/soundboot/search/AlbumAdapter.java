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
import com.example.soundboot.playlist.PlaylistModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.ViewHolder> {

    private final String TAG = "TrackAdapter";
    private Context context;
    private ArrayList<ArtistModel> artistModelArrayList;
    public static final String EXTRA_MESSAGE = "com.example.soundboot.MESSAGE";

    public AlbumAdapter(Context context, ArrayList<ArtistModel> artistModelArrayList) {
        this.context = context;
        this.artistModelArrayList = artistModelArrayList;
    }

    @NonNull
    @Override
    public AlbumAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_list_item, parent, false);
        AlbumAdapter.ViewHolder holder = new AlbumAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull AlbumAdapter.ViewHolder viewHolder, final int position) {
        Log.d(TAG,"onBindViewHolder: called");
        if(artistModelArrayList.get(position).getAlbumCover() != null){
            Picasso.get()
                    .load(artistModelArrayList.get(position).getAlbumCover())
                    .resize(350,350)
                    .into(viewHolder.image);
        }

        viewHolder.name.setText(artistModelArrayList.get(position).getAlbumTitle());
        viewHolder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Status", "onClick clicked on: " + artistModelArrayList.get(position).getAlbumTitle());
                Intent intent = new Intent(context, PlayerActivity.class);
                String albumUri = artistModelArrayList.get(position).getAlbumUri();
                intent.putExtra(EXTRA_MESSAGE, albumUri);
                view.getContext().startActivity(intent);
            }
        });
    }



    @Override
    public int getItemCount() {
        return artistModelArrayList.size();
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
