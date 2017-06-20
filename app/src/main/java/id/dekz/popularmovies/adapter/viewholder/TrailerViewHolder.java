package id.dekz.popularmovies.adapter.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.dekz.popularmovies.R;
import id.dekz.popularmovies.model.apiresponse.TrailerItem;
import id.dekz.popularmovies.util.TrailerUtil;

/**
 * Created by DEKZ on 6/20/2017.
 */

public class TrailerViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.img_trailer)ImageView img;
    @BindView(R.id.tv_title_trailer)TextView title;
    @BindView(R.id.tv_type_trailer)TextView type;

    public TrailerViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bind(TrailerItem data){
        Picasso.with(itemView.getContext())
                .load(TrailerUtil.getVideoThumbnailURL(data.getKey()))
                .into(img);

        title.setText(data.getName());
        type.setText(data.getType());
    }
}
