package com.example.jaee.allocine.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jaee.allocine.MovieActivity;
import com.example.jaee.allocine.R;
import com.example.jaee.allocine.classes.Movie;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static com.example.jaee.allocine.utils.MainHelper.getDPValue;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Movie> movies;
    private Typeface builtTitlingBold, dinProCond;

    public MovieAdapter(Context context, ArrayList<Movie> movies) {
        this.context = context;
        this.movies = movies;

        builtTitlingBold = Typeface.createFromAsset(context.getAssets(),"BuiltTitlingBold.ttf");
        dinProCond = Typeface.createFromAsset(context.getAssets(),"DINPro-Cond.ttf");
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_movie, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MovieAdapter.ViewHolder holder, int position) {
        final Movie movie = movies.get(position);

        if(movie.getPosterLink() != null)
            Picasso.get().load(movie.getPosterLink()).into(holder.ivThumbnail);
        else
            Picasso.get().load(R.drawable.no_photo).resize(getDPValue(context, 100), getDPValue(context, 100)).into(holder.ivThumbnail);

        holder.tvTitle.setText(movie.getTitle());

        holder.tvDirectors.setText(movie.getDirectors());

        holder.tvActors.setText(movie.getActors());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, MovieActivity.class).putExtra("movie", movie));
            }
        });
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView ivThumbnail;
        private TextView tvTitle;
        private TextView tvDirectors;
        private TextView tvActors;

        ViewHolder(View itemView) {
            super(itemView);

            ivThumbnail = itemView.findViewById(R.id.ivThumbnail);

            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvTitle.setTypeface(builtTitlingBold);

            tvDirectors = itemView.findViewById(R.id.tvDirectors);
            tvDirectors.setTypeface(dinProCond);

            tvActors = itemView.findViewById(R.id.tvActors);
            tvActors.setTypeface(dinProCond);
        }
    }
}
