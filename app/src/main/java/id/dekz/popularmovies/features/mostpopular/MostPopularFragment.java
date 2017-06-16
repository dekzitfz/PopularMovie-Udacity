package id.dekz.popularmovies.features.mostpopular;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import id.dekz.popularmovies.R;

/**
 * Created by DEKZ on 6/16/2017.
 */

public class MostPopularFragment extends Fragment
        implements MostPopularView {

    private Unbinder unbinder;
    private MostPopularPresenter presenter;

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
    }

    @Override
    public void onDetachView() {
        presenter.onDetach();
    }
}
