package com.dicoding.picodiploma.consumerapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.dicoding.picodiploma.consumerapp.R;
import com.dicoding.picodiploma.consumerapp.api.Constants;
import com.dicoding.picodiploma.consumerapp.model.Favorite;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.ListViewHolder> {
    private ArrayList<Favorite> favorites;
    private Context context;

    public FavoriteAdapter(Context context, ArrayList<Favorite> favorites) {
        this.favorites = favorites;
        this.context = context;
    }

    @NonNull
    @Override
    public FavoriteAdapter.ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, parent, false);
        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final FavoriteAdapter.ListViewHolder holder, final int position) {
        Favorite favorite = favorites.get(position);

        holder.title.setText(favorite.getTitle());

        SimpleDateFormat input = new SimpleDateFormat("yyyy-mm-dd");
        SimpleDateFormat output = new SimpleDateFormat("dd MMM yyyy");
        try {
            holder.date.setText(output.format(input.parse(favorite.getRelease_date())));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        holder.description.setText(favorite.getOverview());

        Glide.with(context)
                .load(Constants.IMAGE_SOURCE + favorite.getPoster_path())
                .fitCenter()
                .into(holder.image);
        holder.image.setContentDescription(context.getResources().getString(R.string.image_movie) + favorite.getTitle());
    }

    @Override
    public int getItemCount() {
        return favorites.size();
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
}
