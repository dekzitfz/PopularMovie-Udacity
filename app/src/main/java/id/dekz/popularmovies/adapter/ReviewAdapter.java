package id.dekz.popularmovies.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import id.dekz.popularmovies.R;
import id.dekz.popularmovies.adapter.viewholder.ReviewViewHolder;
import id.dekz.popularmovies.model.apiresponse.ReviewItem;

/**
 * Created by DEKZ on 6/20/2017.
 */

public class ReviewAdapter extends RecyclerView.Adapter<ReviewViewHolder> {

    private List<ReviewItem> list = new ArrayList<>();

    public ReviewAdapter() {
    }

    public void replaceAll(List<ReviewItem> list){
        this.list.clear();
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public ReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View vh = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_review, parent, false);
        return new ReviewViewHolder(vh);
    }

    @Override
    public void onBindViewHolder(ReviewViewHolder holder, int position) {
        holder.bind(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
