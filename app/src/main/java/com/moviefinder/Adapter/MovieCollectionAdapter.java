package com.moviefinder.Adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.moviefinder.Model.MovieModel;
import com.moviefinder.Model.RatingModel;
import com.moviefinder.R;

import java.util.ArrayList;

/**
 * Created by training on 18/04/17.
 */

public class MovieCollectionAdapter extends RecyclerView.Adapter<MovieCollectionAdapter.MyViewHolder> implements View.OnClickListener {

    private Context mContext;
    private ArrayList<MovieModel> movieList;
    private String movieLabel = "";

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView thumbnailImage;
        private TextView titleTextView, genreTextView, releaseDateTextView, plotTextView, ratingContentView;
        private ProgressBar progressBar;
        private CardView cardView;
        private LinearLayout linearCardView;


        public MyViewHolder(View view) {
            super(view);

            titleTextView = (TextView) view.findViewById(R.id.text_view_title);
            genreTextView = (TextView) view.findViewById(R.id.text_view_genre);
            releaseDateTextView = (TextView) view.findViewById(R.id.text_view_release_date);
            plotTextView = (TextView) view.findViewById(R.id.text_view_plot);
            ratingContentView = (TextView)view.findViewById(R.id.rating_content);
            thumbnailImage = (ImageView) view.findViewById(R.id.image_view_thumbnail);
            progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
            cardView = (CardView) view.findViewById(R.id.card_view);
            linearCardView = (LinearLayout) view.findViewById(R.id.linear_card_view);
        }
    }


    public MovieCollectionAdapter(Context mContext, ArrayList<MovieModel> movieList) {
        this.mContext = mContext;
        this.movieList = movieList;
    }

    @Override
    public MovieCollectionAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_card_layout, parent, false);

        return new MovieCollectionAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MovieCollectionAdapter.MyViewHolder holder, int position) {
        MovieModel movie = movieList.get(position);
        Glide.with(mContext)
                .load(movie.getThumbnailUrl())
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        holder.progressBar.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        holder.progressBar.setVisibility(View.GONE);
                        return false;
                    }
                })
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.thumbnailImage);

        holder.titleTextView.setText(movie.getTitle());
        holder.genreTextView.setText(movie.getGenre());
        holder.releaseDateTextView.setText(movie.getReleaseDate());
        holder.plotTextView.setText(movie.getPlot());
        String ratingText = "";

        ArrayList<RatingModel> arrayList = new ArrayList<RatingModel>();
        arrayList = movie.getRatings();
        if (arrayList.size() > 0){
            for (int i =0;i<arrayList.size();i++){
                if (i!=0){
                    ratingText = ratingText + "\n";
                }
                String source = arrayList.get(i).getSource();
                String value = arrayList.get(i).getValue();
                ratingText = ratingText + source + ": " + value;
            }
        }
        holder.ratingContentView.setText(ratingText);

//        holder.thumbnail.setOnClickListener(this); //Movie View OnClick
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
//            case R.id.thumbnail:

//                break;
        }
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }
}
