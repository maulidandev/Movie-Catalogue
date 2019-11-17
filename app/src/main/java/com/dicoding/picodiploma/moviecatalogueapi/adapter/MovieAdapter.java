package com.dicoding.picodiploma.moviecatalogueapi.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.dicoding.picodiploma.moviecatalogueapi.R;
import com.dicoding.picodiploma.moviecatalogueapi.api.Constants;
import com.dicoding.picodiploma.moviecatalogueapi.model.Movie;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ListViewHolder> {
    private ArrayList<Movie> movies;
    private Context context;

    public MovieAdapter(Context context, ArrayList<Movie> movies) {
        this.movies = movies;
        this.context = context;
    }

    @NonNull
    @Override
    public MovieAdapter.ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, parent, false);
        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MovieAdapter.ListViewHolder holder, final int position) {
        Movie movie = movies.get(position);

        if (movie.getTitle() != null)
            holder.title.setText(movie.getTitle());
        else
            holder.title.setText(movie.getName());

        SimpleDateFormat input = new SimpleDateFormat("yyyy-mm-dd");
        SimpleDateFormat output = new SimpleDateFormat("dd MMM yyyy");
        try {
            if (movie.getRelease_date() != null)
                holder.date.setText(output.format(input.parse(movie.getRelease_date())));
            else if (movie.getFirst_air_date() != null)
                holder.date.setText(output.format(input.parse(movie.getFirst_air_date())));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        holder.description.setText(movie.getOverview());

        Glide.with(context)
                .load(Constants.IMAGE_SOURCE + movie.getPoster_path())
                .fitCenter()
                .into(holder.image);
        holder.image.setContentDescription(context.getResources().getString(R.string.image_movie) + movie.getTitle());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickCallback.onItemClicked(movies.get(position));
            }
        });
    }

    public void setData(ArrayList<Movie> movies) {
        this.movies.clear();
        this.movies.addAll(movies);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    private OnItemClickCallback onItemClickCallback;

    public void setOnItemClickCallback(OnItemClickCallback onItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback;
    }

    public class ListViewHolder extends RecyclerView.ViewHolder {
        private TextView title, description, date;
        private ImageView image;

        public ListViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.title);
            description = itemView.findViewById(R.id.overview);
            date = itemView.findViewById(R.id.release_date);
            image = itemView.findViewById(R.id.poster_path);
        }
    }

    public interface OnItemClickCallback {
        void onItemClicked(Movie data);
    }
}
