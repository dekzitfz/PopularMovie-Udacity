package id.dekz.popularmovies.adapter.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.dekz.popularmovies.R;
import id.dekz.popularmovies.model.apiresponse.ReviewItem;

/**
 * Created by DEKZ on 6/20/2017.
 */

public class ReviewViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.tv_author)TextView author;
    @BindView(R.id.tv_content)TextView content;

    public ReviewViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bind(ReviewItem data){
        author.setText(data.getAuthor());
        content.setText(data.getContent());
    }
}
