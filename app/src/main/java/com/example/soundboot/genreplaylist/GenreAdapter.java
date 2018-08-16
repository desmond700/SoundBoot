package com.example.soundboot.genreplaylist;

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

import com.example.soundboot.GenreActivity;
import com.example.soundboot.R;
import com.example.soundboot.player.PlayerActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class GenreAdapter extends RecyclerView.Adapter<GenreAdapter.ViewHolder> {

    private final String TAG = "TrackAdapter";
    private Context context;
    private ArrayList<GenreModel> genreModelArrayList;
    public static final String EXTRA_MESSAGE = "com.example.soundboot.MESSAGE";

    public GenreAdapter(Context context, ArrayList<GenreModel> genreModelArrayList) {
        this.context = context;
        this.genreModelArrayList = genreModelArrayList;
    }

    @NonNull
    @Override
    public GenreAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_list_item, parent, false);
        GenreAdapter.ViewHolder holder = new GenreAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull GenreAdapter.ViewHolder viewHolder, final int position) {
        Log.d(TAG,"onBindViewHolder: called");
        if(genreModelArrayList.get(position).getImageUrl() != null){
            Picasso.get()
                    .load(genreModelArrayList.get(position).getImageUrl())
                    .resize(350,350)
                    .into(viewHolder.image);
        }

        viewHolder.name.setText(genreModelArrayList.get(position).getName());
        viewHolder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Status", "onClick clicked on: " + genreModelArrayList.get(position).getName());
                Intent intent = new Intent(context, PlayerActivity.class);
                String playlistUri = genreModelArrayList.get(position).getGenreHref();
                intent.putExtra(EXTRA_MESSAGE, playlistUri);
                view.getContext().startActivity(intent);
            }
        });
    }



    @Override
    public int getItemCount() {
        return genreModelArrayList.size();
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
