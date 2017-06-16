package id.dekz.popularmovies.features.mostpopular;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import id.dekz.popularmovies.R;
import id.dekz.popularmovies.adapter.MostPopularAdapter;
import id.dekz.popularmovies.model.apiresponse.mostpopular.ResultsItem;

/**
 * Created by DEKZ on 6/16/2017.
 */

public class MostPopularFragment extends Fragment
        implements MostPopularView {

    private Unbinder unbinder;
    private MostPopularPresenter presenter;
    private MostPopularAdapter adapter;
    private GridLayoutManager gridLayoutManager;
    private Parcelable layoutManagerSavedState;

    @BindView(R.id.rv_mostpopular)RecyclerView rv;

    @Override
    public void onAttach(Context context) {
        Log.d("position", "onAttach");
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        unbinder.unbind();
    }

    //----------save instance----------
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d("position", "onSaveInstanceState");
        String now = DateFormat.getDateTimeInstance().format(new Date());
        Log.d("time out", now);
        outState.putString("time", now);

        //save state rv
        outState.putParcelable("LAYOUT_MANAGER", rv.getLayoutManager().onSaveInstanceState());
    }

    //----------restore instance----------
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d("position", "onActivityCreated");
        if(savedInstanceState!=null){
            Log.d("time in", savedInstanceState.getString("time"));
            layoutManagerSavedState = savedInstanceState.getParcelable("LAYOUT_MANAGER");
        }else{
            gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        }

        onAttachView();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d("position", "onCreate");
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("position", "onCreateView");
        View rootView = inflater.inflate(R.layout.fragment_mostpopular, container, false);
        unbinder = ButterKnife.bind(this, rootView);

        gridLayoutManager = new GridLayoutManager(getActivity(), 2);

        return rootView;
    }

    @Override
    public void onAttachView() {
        Log.d("position", "onAttachView");
        presenter = new MostPopularPresenter();
        presenter.onAttach(this);

        setupRecyclerView();
        presenter.loadData();
    }

    @Override
    public void onDetachView() {
        Log.d("position", "onDetachView");
        presenter.onDetach();
    }

    void setupRecyclerView(){
        adapter = new MostPopularAdapter();
        rv.setLayoutManager(gridLayoutManager);
        rv.setHasFixedSize(true);
        rv.setAdapter(adapter);
    }

    @Override
    public void onDataReceived(List<ResultsItem> data) {
        Log.d("position", "onDataReceived");
        adapter.replaceAll(data);

        //retain scroll position
        if(layoutManagerSavedState!=null){
            if(rv!=null){
                rv.getLayoutManager().onRestoreInstanceState(layoutManagerSavedState);
            }else{
                Log.w("msg", "rv is null!");
            }
        }else{
            Log.w("msg", "layoutManagerSavedState is null!");
        }
    }

    @Override
    public void onLoadingData() {

    }

    @Override
    public void onFailedReceivedData() {

    }

    @Override
    public void showMessage(String msg) {

    }
}
