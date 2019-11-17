package com.dicoding.picodiploma.moviecatalogueapi.fragment;


import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dicoding.picodiploma.moviecatalogueapi.R;
import com.dicoding.picodiploma.moviecatalogueapi.activity.DetailActivity;
import com.dicoding.picodiploma.moviecatalogueapi.activity.FavoriteActivity;
import com.dicoding.picodiploma.moviecatalogueapi.adapter.MovieAdapter;
import com.dicoding.picodiploma.moviecatalogueapi.model.Movie;
import com.dicoding.picodiploma.moviecatalogueapi.viewmodel.TVShowFragmentViewModel;

import java.util.ArrayList;

import static com.dicoding.picodiploma.moviecatalogueapi.api.Constants.MOVIE;

/**
 * A simple {@link Fragment} subclass.
 */
public class TVShowFragment extends Fragment {


    private Context context;
    private ProgressBar progressBar;
    private RecyclerView listMovies;
    private TVShowFragmentViewModel tvShowFragmentViewModel;

    public TVShowFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_movie, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        progressBar = view.findViewById(R.id.progressBar);

        final MovieAdapter movieAdapter = new MovieAdapter(context, new ArrayList<Movie>());
        movieAdapter.notifyDataSetChanged();
        movieAdapter.setOnItemClickCallback(new MovieAdapter.OnItemClickCallback() {
            @Override
            public void onItemClicked(Movie data) {
                // set sumber dari tv show
                data.setSource(2);

                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra(MOVIE, data);

                startActivity(intent);
            }
        });

        listMovies = view.findViewById(R.id.list_movies);
        listMovies.setLayoutManager(new LinearLayoutManager(context));
        listMovies.setAdapter(movieAdapter);
        listMovies.setHasFixedSize(true);

        tvShowFragmentViewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(TVShowFragmentViewModel.class);
        tvShowFragmentViewModel.setMovie("");

        tvShowFragmentViewModel.getMovies().observe(this, new Observer<ArrayList<Movie>>() {
            @Override
            public void onChanged(ArrayList<Movie> movies) {
                if (movies != null) {
                    movieAdapter.setData(movies);
                    showProgress(false);
                }
            }
        });
    }

    private void showProgress(boolean show){
        if (show){
            progressBar.setVisibility(View.VISIBLE);
            listMovies.setVisibility(View.GONE);
        }else{
            progressBar.setVisibility(View.GONE);
            listMovies.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        this.context = context;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu, menu);
        super.onCreateOptionsMenu(menu, inflater);

        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        if (searchManager != null) {
            SearchView searchView = (SearchView) (menu.findItem(R.id.search)).getActionView();
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
            searchView.setQueryHint(getResources().getString(R.string.search));
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    showProgress(true);
                    tvShowFragmentViewModel.setMovie(query);
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    showProgress(true);
                    tvShowFragmentViewModel.setMovie(newText);

                    return false;
                }
            });
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_change_settings:
                Intent mIntent = new Intent(Settings.ACTION_LOCALE_SETTINGS);
                startActivity(mIntent);
                break;

            case R.id.favorite:
                startActivity(new Intent(context, FavoriteActivity.class));
                break;
        }

        return super.onOptionsItemSelected(item);
    }

}
