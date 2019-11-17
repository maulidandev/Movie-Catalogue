package com.dicoding.picodiploma.moviecatalogueapi.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.dicoding.picodiploma.moviecatalogueapi.R;
import com.dicoding.picodiploma.moviecatalogueapi.activity.DetailActivity;
import com.dicoding.picodiploma.moviecatalogueapi.adapter.FavoriteAdapter;
import com.dicoding.picodiploma.moviecatalogueapi.model.Favorite;
import com.dicoding.picodiploma.moviecatalogueapi.model.Movie;
import com.dicoding.picodiploma.moviecatalogueapi.viewmodel.MovieFavoriteFragmentViewModel;

import java.util.ArrayList;

import static com.dicoding.picodiploma.moviecatalogueapi.api.Constants.MOVIE;

/**
 * A simple {@link Fragment} subclass.
 */
public class MovieFavoriteFragment extends Fragment {

    private Context context;
    private ProgressBar progressBar;
    private TextView noData;

    public MovieFavoriteFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_movie_favorite, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        progressBar = view.findViewById(R.id.progressBar);
        noData = view.findViewById(R.id.no_data);

        final FavoriteAdapter favoriteAdapter = new FavoriteAdapter(context, new ArrayList<Favorite>());
        favoriteAdapter.notifyDataSetChanged();
        favoriteAdapter.setOnItemClickCallback(new FavoriteAdapter.OnItemClickCallback() {
            @Override
            public void onItemClicked(Favorite data) {
                Movie movie = new Movie();
                movie.setId(data.getId());
                movie.setTitle(data.getTitle());
                movie.setOverview(data.getOverview());
                movie.setRelease_date(data.getRelease_date());
                movie.setPoster_path(data.getPoster_path());

                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra(MOVIE, movie);

                startActivity(intent);
            }
        });

        RecyclerView listMovies = view.findViewById(R.id.list_movies);
        listMovies.setLayoutManager(new LinearLayoutManager(context));
        listMovies.setAdapter(favoriteAdapter);
        listMovies.setHasFixedSize(true);

        final MovieFavoriteFragmentViewModel movieFavoriteFragmentViewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(MovieFavoriteFragmentViewModel.class);
        movieFavoriteFragmentViewModel.setFavorite(context);

        movieFavoriteFragmentViewModel.getFavorite().observe(this, new Observer<ArrayList<Favorite>>() {
            @Override
            public void onChanged(ArrayList<Favorite> favorites) {
                if (favorites != null) {
                    favoriteAdapter.setData(favorites);
                    progressBar.setVisibility(View.GONE);

                    if (favorites.size() == 0)
                        noData.setVisibility(View.VISIBLE);
                }
            }
        });

        final SwipeRefreshLayout pullToRefresh = view.findViewById(R.id.pullToRefresh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                movieFavoriteFragmentViewModel.setFavorite(context);
                pullToRefresh.setRefreshing(false);
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        this.context = context;
    }

}
