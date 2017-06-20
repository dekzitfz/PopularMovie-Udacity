package id.dekz.popularmovies.features.detail;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.dekz.popularmovies.Constant;
import id.dekz.popularmovies.R;
import id.dekz.popularmovies.model.apiresponse.MovieItem;
import id.dekz.popularmovies.util.DateFormatter;
import id.dekz.popularmovies.util.ImageURLBuilder;
import id.dekz.popularmovies.util.SnackBarBuilder;

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

    private DetailPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
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

        if(getIntent()!=null){
            presenter.getData(
                    getIntent().getStringExtra(Constant.KEY_MOVIE)
            );

            presenter.setupLoader(
                    this,
                    getContentResolver(),
                    presenter.getMovie(getIntent().getStringExtra(Constant.KEY_MOVIE)).getId()
            );
            presenter.initLoader(getSupportLoaderManager());

            fabFavorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if((Integer) fabFavorite.getTag() == R.drawable.ic_star_selected){
                        presenter.unsetAsFavorite(
                                getContentResolver(),
                                presenter.getMovie(getIntent().getStringExtra(Constant.KEY_MOVIE))
                        );
                    }else{
                        presenter.saveAsFavorite(
                                getContentResolver(),
                                presenter.getMovie(getIntent().getStringExtra(Constant.KEY_MOVIE))
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
        parentView.setVisibility(View.GONE);
        SnackBarBuilder.showMessage(parentView, getResources().getString(R.string.failed_load_data));
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
}
