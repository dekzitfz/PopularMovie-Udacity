package id.dekz.popularmovies.features.detail;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.dekz.popularmovies.Constant;
import id.dekz.popularmovies.R;
import id.dekz.popularmovies.adapter.ReviewAdapter;
import id.dekz.popularmovies.adapter.TrailerAdapter;
import id.dekz.popularmovies.model.apiresponse.MovieItem;
import id.dekz.popularmovies.model.apiresponse.ReviewItem;
import id.dekz.popularmovies.model.apiresponse.TrailerItem;
import id.dekz.popularmovies.util.DateFormatter;
import id.dekz.popularmovies.util.ImageURLBuilder;

/**
 * Created by DEKZ on 6/17/2017.
 */

public class DetailActivity extends AppCompatActivity implements DetailView {

    @BindView(R.id.toolbar)Toolbar toolbar;
    @BindView(R.id.expandedImage)ImageView backdrop;
    @BindView(R.id.poster)ImageView poster;
    @BindView(R.id.releaseDate)TextView releaseDate;
    @BindView(R.id.rating)TextView rating;
    @BindView(R.id.synopsis)TextView synopsis;
    @BindView(R.id.parentDetail)CoordinatorLayout parentView;
    @BindView(R.id.fab)FloatingActionButton fabFavorite;
    @BindView(R.id.rv_trailers)RecyclerView rvTrailers;
    @BindView(R.id.rv_reviews)RecyclerView rvReviews;
    @BindView(R.id.nested_scroll)NestedScrollView nestedScroll;

    private DetailPresenter presenter;
    private TrailerAdapter trailerAdapter;
    private ReviewAdapter reviewAdapter;
    private int[] nestedScrollPosition = new int[2];
    private ArrayList<TrailerItem> trailers = new ArrayList<>();
    private ArrayList<ReviewItem> reviews = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        if(savedInstanceState != null){
            nestedScrollPosition = savedInstanceState.getIntArray(Constant.KEY_SCROLL_POSITION);
            trailers = savedInstanceState.getParcelableArrayList(Constant.KEY_STATE_TRAILER);
            reviews = savedInstanceState.getParcelableArrayList(Constant.KEY_STATE_REVIEW);
        }

        onAttachView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.restartLoader(getSupportLoaderManager());
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(
                Constant.KEY_MOVIE,
                getIntent().getStringExtra(Constant.KEY_MOVIE)
        );

        outState.putIntArray(
                Constant.KEY_SCROLL_POSITION,
                new int[]{nestedScrollPosition[0], nestedScrollPosition[1]}
        );

        outState.putParcelableArrayList(Constant.KEY_STATE_TRAILER, trailers);
        outState.putParcelableArrayList(Constant.KEY_STATE_REVIEW, reviews);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        DetailActivity.this.finish();
    }

    @Override
    public void onAttachView() {
        presenter = new DetailPresenter();
        presenter.onAttach(this);

        setupRVTrailers();
        setupRVReviews();
        setupNestedScrollListener();

        if(getIntent()!=null){
            final String json = getIntent().getStringExtra(Constant.KEY_MOVIE);
            presenter.getTrailers(presenter.getMovie(json).getId());
            presenter.getReviews(presenter.getMovie(json).getId());

            presenter.getData(json);

            presenter.setupLoader(
                    this,
                    getContentResolver(),
                    presenter.getMovie(json).getId()
            );
            presenter.initLoader(getSupportLoaderManager());

            fabFavorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if((Integer) fabFavorite.getTag() == R.drawable.ic_star_selected){
                        presenter.unsetAsFavorite(
                                getContentResolver(),
                                presenter.getMovie(json)
                        );
                    }else{
                        presenter.saveAsFavorite(
                                getContentResolver(),
                                presenter.getMovie(json)
                        );
                        presenter.restartLoader(getSupportLoaderManager());
                    }
                }
            });
        }
    }

    @Override
    public void onDetachView() {
        presenter.onDetach();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        onDetachView();
    }

    @Override
    public void onDataReceived(MovieItem movie) {
        parentView.setVisibility(View.VISIBLE);
        getSupportActionBar().setTitle(movie.getOriginalTitle());
        Picasso.with(DetailActivity.this)
                .load(ImageURLBuilder.getBackdropURL(movie.getBackdropPath()))
                .into(backdrop);

        Picasso.with(DetailActivity.this)
                .load(ImageURLBuilder.getPosterURL(movie.getPosterPath()))
                .into(poster);

        releaseDate.setText(DateFormatter.getReadableDate(movie.getReleaseDate()));
        rating.setText(String.valueOf(movie.getVoteAverage()));
        synopsis.setText(movie.getOverview());
    }

    @Override
    public void onFailedReceiveData() {
        if(trailers != null){
            List<TrailerItem> listTrailer = new ArrayList<>();
            for(TrailerItem t : trailers){listTrailer.add(t);}
            trailerAdapter.replaceAll(listTrailer);
        }

        if(reviews != null){
            List<ReviewItem> listReview = new ArrayList<>();
            for(ReviewItem r : reviews){listReview.add(r);}
            reviewAdapter.replaceAll(listReview);
        }

        restoreScrollPosition();
    }

    @Override
    public void onStatusReceived(boolean isFavorite) {
        if(isFavorite){
            fabFavorite.setImageResource(R.drawable.ic_star_selected);
            fabFavorite.setTag(R.drawable.ic_star_selected);
        }else{
            fabFavorite.setImageResource(R.drawable.ic_star_unselected);
            fabFavorite.setTag(R.drawable.ic_star_unselected);
        }
    }

    @Override
    public void onRefreshLoader() {
        presenter.restartLoader(getSupportLoaderManager());
    }

    @Override
    public void onTrailerDataReceived(List<TrailerItem> data) {
        trailers.clear();
        for(TrailerItem trailer : data){
            trailers.add(trailer);
        }
        trailerAdapter.replaceAll(trailers);
    }

    @Override
    public void onReviewDataReceived(List<ReviewItem> data) {
        reviews.clear();
        for(ReviewItem r : data){reviews.add(r);}
        reviewAdapter.replaceAll(reviews);
        restoreScrollPosition();
    }

    private void setupRVTrailers(){
        trailerAdapter = new TrailerAdapter();
        rvTrailers.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        );
        rvTrailers.setAdapter(trailerAdapter);
    }

    private void setupRVReviews(){
        reviewAdapter = new ReviewAdapter();
        rvReviews.setLayoutManager(new LinearLayoutManager(this));
        rvReviews.setAdapter(reviewAdapter);
        rvReviews.setNestedScrollingEnabled(false);
    }

    private void setupNestedScrollListener(){
        nestedScroll.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                nestedScrollPosition[0] = scrollX;
                nestedScrollPosition[1] = scrollY;

                if (scrollY > oldScrollY) {
                    fabFavorite.hide();
                } else {
                    fabFavorite.show();
                }
            }
        });
    }

    private void restoreScrollPosition(){
        if(nestedScrollPosition != null){
            nestedScroll.post(new Runnable() {
                @Override
                public void run() {
                    nestedScroll.scrollTo(nestedScrollPosition[0], nestedScrollPosition[1]);
                }
            });
        }
    }
}
