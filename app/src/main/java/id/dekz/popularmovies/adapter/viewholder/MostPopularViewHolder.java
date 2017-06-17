package id.dekz.popularmovies.adapter.viewholder;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.dekz.popularmovies.R;
import id.dekz.popularmovies.features.detail.DetailActivity;
import id.dekz.popularmovies.model.apiresponse.MovieItem;
import id.dekz.popularmovies.util.ImageURLBuilder;

/**
 * Created by DEKZ on 6/16/2017.
 */

public class MostPopularViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.img_movie)ImageView poster;

    public MostPopularViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bind(MovieItem data){
        String src = ImageURLBuilder.getPosterURL(data.getPosterPath());
        Picasso.with(itemView.getContext())
                .load(src)
                .into(poster);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemView.getContext()
                        .startActivity(new Intent(itemView.getContext(), DetailActivity.class));
            }
        });
    }
}
