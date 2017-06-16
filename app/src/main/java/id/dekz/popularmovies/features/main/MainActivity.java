package id.dekz.popularmovies.features.main;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import id.dekz.popularmovies.R;
import id.dekz.popularmovies.adapter.TabsAdapter;
import id.dekz.popularmovies.features.highrated.HighRatedFragment;
import id.dekz.popularmovies.features.mostpopular.MostPopularFragment;

public class MainActivity extends AppCompatActivity implements MainView {

    @BindView(R.id.toolbar)Toolbar toolbar;
    @BindView(R.id.container)ViewPager container;
    @BindView(R.id.tabs)TabLayout tabLayout;
    @BindString(R.string.most_popular)String mostPopularString;
    @BindString(R.string.highest_rated)String highRatedString;

    private MainPresenter presenter;
    private TabsAdapter tabsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        onAttachView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        onDetachView();
    }

    @Override
    public void onAttachView() {
        presenter = new MainPresenter();
        presenter.onAttach(this);

        setupTabs();
    }

    @Override
    public void onDetachView() {
        presenter.onDetach();
    }

    private void setupTabs(){
        tabsAdapter = new TabsAdapter(getSupportFragmentManager());
        tabsAdapter.addFragment(new MostPopularFragment(), mostPopularString);
        tabsAdapter.addFragment(new HighRatedFragment(), highRatedString);
        container.setAdapter(tabsAdapter);
        tabLayout.setupWithViewPager(container);
    }
}
