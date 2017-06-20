package id.dekz.popularmovies.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import id.dekz.popularmovies.R;
import id.dekz.popularmovies.adapter.viewholder.TrailerViewHolder;
import id.dekz.popularmovies.model.apiresponse.TrailerItem;

/**
 * Created by DEKZ on 6/20/2017.
 */

public class TrailerAdapter extends RecyclerView.Adapter<TrailerViewHolder> {

    List<TrailerItem> list = new ArrayList<>();

    public TrailerAdapter() {
    }

    public void replaceAll(List<TrailerItem> list){
        this.list.clear();
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public TrailerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View vh  = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_trailer, parent, false);
        return new TrailerViewHolder(vh);
    }

    @Override
    public void onBindViewHolder(TrailerViewHolder holder, int position) {
        holder.bind(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
