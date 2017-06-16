package id.dekz.popularmovies.features.mostpopular;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import id.dekz.popularmovies.Constant;
import id.dekz.popularmovies.R;
import id.dekz.popularmovies.adapter.MostPopularAdapter;
import id.dekz.popularmovies.model.apiresponse.mostpopular.ResultsItem;
import id.dekz.popularmovies.util.SnackBarBuilder;

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
    @BindView(R.id.parent_layout)RelativeLayout parentView;

    @Override
    public void onDetach() {
        super.onDetach();
        unbinder.unbind();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //save state rv
        outState.putParcelable(Constant.LAYOUT_MANAGER, rv.getLayoutManager().onSaveInstanceState());
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(savedInstanceState != null){
            layoutManagerSavedState = savedInstanceState.getParcelable(Constant.LAYOUT_MANAGER);
        }

        onAttachView();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_mostpopular, container, false);
        unbinder = ButterKnife.bind(this, rootView);

        gridLayoutManager = new GridLayoutManager(getActivity(), 2);

        return rootView;
    }

    @Override
    public void onAttachView() {
        presenter = new MostPopularPresenter();
        presenter.onAttach(this);

        setupRecyclerView();
        presenter.loadData();
    }

    @Override
    public void onDetachView() {
        presenter.onDetach();
    }

    void setupRecyclerView(){
        adapter = new MostPopularAdapter();
        rv.setLayoutManager(gridLayoutManager);
        rv.setHasFixedSize(true);
        rv.setAdapter(adapter);

        setupScrollListener();
    }

    @Override
    public void onDataReceived(List<ResultsItem> data, int page) {
        if(page>1){
            //update data
            adapter.updateData(data);
        }else{
            //new data
            adapter.replaceAll(data);
        }

        //retain scroll position
        if(layoutManagerSavedState!=null){
            rv.getLayoutManager().onRestoreInstanceState(layoutManagerSavedState);
        }


    }

    private void setupScrollListener(){
        rv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                presenter.checkWhenScrolled(gridLayoutManager, dy);
            }
        });
    }

    @Override
    public void onLoadingData() {

    }

    @Override
    public void onFailedReceivedData() {
        SnackBarBuilder.showMessage(
                parentView,
                getResources().getString(R.string.failed_load_data)
        );
    }

    @Override
    public void showMessage(String msg) {
    }
}
