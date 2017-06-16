package id.dekz.popularmovies.features.mostpopular;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

    @BindView(R.id.rv_mostpopular)RecyclerView rv;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_mostpopular, container, false);
        unbinder = ButterKnife.bind(this, rootView);

        onAttachView();

        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
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
        rv.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        rv.setHasFixedSize(true);
        rv.setAdapter(adapter);
    }

    @Override
    public void onDataReceived(List<ResultsItem> data) {
        adapter.replaceAll(data);
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
