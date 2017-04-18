package com.moviefinder;

import android.app.SearchManager;
import android.content.Context;
import android.graphics.Color;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.moviefinder.Adapter.MovieCollectionAdapter;
import com.moviefinder.Model.MovieModel;
import com.moviefinder.Model.RatingModel;
import com.moviefinder.Utilities.APIManager;
import com.moviefinder.Utilities.GlobalConfig;
import com.moviefinder.Utilities.RequestCallback;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    private int DEFAULT_PAGE_NUMBER = 1;
    private MenuItem item;
    private SearchView sv;
    private CoordinatorLayout coordinatorLayout;
    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private MovieCollectionAdapter movieCollectionAdapter;
    private Context context;
    private ArrayList<MovieModel> movieArrayList;
    private String queryString = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinator_layout_activity_home);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar_movie_collection);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_movie_collection);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout_movie_collection);
        context = HomeActivity.this;
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                refreshView();
            }
        });
        movieArrayList = new ArrayList<MovieModel>();
        movieCollectionAdapter = new MovieCollectionAdapter(context, movieArrayList);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(context);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        recyclerView.setAdapter(movieCollectionAdapter);
    }

    private void refreshView() {

        if (movieArrayList.size() > 0) {
            movieArrayList.clear();
        }
        fireSearchAPICall(queryString);

        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        item = menu.findItem(R.id.search_group);
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        sv = (SearchView) item.getActionView();

        sv.setQueryHint("Movie Name...");
        sv.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));
        sv.setOnQueryTextListener(this);
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        if (movieArrayList.size() > 0) {
            movieArrayList.clear();
        }
        queryString = query;
        fireSearchAPICall(queryString);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    private void fireSearchAPICall(String query) {

        if (GlobalConfig.checkConnection(context, coordinatorLayout)) {

            if (progressBar.getVisibility() == View.GONE)
                progressBar.setVisibility(View.VISIBLE);

            query = query.replaceAll(" ", "%20");
            String url = GlobalConfig.BASE_URL + "?t=" + query + "&plot=short";

            JSONObject jsonObject = new JSONObject();

            APIManager.jsonObjectVolleyRequest(context, url, "GET", jsonObject);
            APIManager.setOnAPICallbackListener(new RequestCallback.APIRequestCallback() {
                @Override
                public void onSuccessResponse(String response) {
                }

                @Override
                public void onSuccessJSONResponse(JSONObject response) {
                    if (progressBar.getVisibility() == View.VISIBLE)
                        progressBar.setVisibility(View.GONE);

                    if (response != null) {
                        setMovieObjectToView(response);
                    } else {
                        GlobalConfig.showSnackBar("Not available", coordinatorLayout);
                    }
                }

                @Override
                public void onSuccessJSONArrayResponse(JSONArray response) {

                }

                @Override
                public void onErrorResponse(VolleyError error) {
                    if (progressBar.getVisibility() == View.VISIBLE)
                        progressBar.setVisibility(View.GONE);
                    APIManager.handleErrorResponseOfRequest(context, error, "Server Error", "SearchMovies");
                }

                @Override
                public void statusCode(int statusCode) {
                }
            });
        }
    }

    private void setMovieObjectToView(JSONObject jsonObject) {
        String blank = "";

        try {

            MovieModel movieModel = new MovieModel();

            if (jsonObject.has("Title") && !jsonObject.isNull("Title")) {
                movieModel.setTitle(jsonObject.getString("Title"));
            } else
                movieModel.setTitle(blank);

            if (jsonObject.has("Genre") && !jsonObject.isNull("Genre")) {
                movieModel.setGenre(jsonObject.getString("Genre"));
            } else
                movieModel.setGenre(blank);

            if (jsonObject.has("Released") && !jsonObject.isNull("Released")) {
                movieModel.setReleaseDate(jsonObject.getString("Released"));
            } else
                movieModel.setReleaseDate(blank);

            if (jsonObject.has("Plot") && !jsonObject.isNull("Plot")) {
                movieModel.setPlot(jsonObject.getString("Plot"));
            } else
                movieModel.setPlot(blank);

            if (jsonObject.has("Ratings") && !jsonObject.isNull("Ratings")) {
                JSONArray jsonArray = jsonObject.getJSONArray("Ratings");
                movieModel.setRatings(simplifyJSONArray(jsonArray));
            } else
                movieModel.setRatings(null);

            if (jsonObject.has("Poster") && !jsonObject.isNull("Poster")) {
                movieModel.setThumbnailUrl(jsonObject.getString("Poster"));
            } else
                movieModel.setThumbnailUrl(blank);

            movieArrayList.add(movieModel);
            movieCollectionAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private ArrayList<RatingModel> simplifyJSONArray(JSONArray jsonArray) {
        String blank = "";
        ArrayList<RatingModel> ratingModelArrayList = new ArrayList<RatingModel>();
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                RatingModel ratingModel = new RatingModel();
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                if (jsonObject.has("Source") && !jsonObject.isNull("Source")) {
                    ratingModel.setSource(jsonObject.getString("Source"));
                } else
                    ratingModel.setSource(blank);

                if (jsonObject.has("Value") && !jsonObject.isNull("Value")) {
                    ratingModel.setValue(jsonObject.getString("Value"));
                } else
                    ratingModel.setValue(blank);

                ratingModelArrayList.add(ratingModel);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ratingModelArrayList;
    }
}
