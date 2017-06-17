package id.dekz.popularmovies.features.main;
import android.os.Parcelable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RelativeLayout;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.dekz.popularmovies.Constant;
import id.dekz.popularmovies.R;
import id.dekz.popularmovies.adapter.MovieListAdapter;
import id.dekz.popularmovies.model.apiresponse.MovieItem;
import id.dekz.popularmovies.util.SnackBarBuilder;

public class MainActivity extends AppCompatActivity implements MainView, SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.toolbar)Toolbar toolbar;
    @BindView(R.id.swipe_refresh)SwipeRefreshLayout swipeRefresh;
    @BindView(R.id.rv)RecyclerView rv;
    @BindView(R.id.parent_main)RelativeLayout parentView;

    private MainPresenter presenter;
    private MovieListAdapter adapter;
    private GridLayoutManager gridLayoutManager;
    private Parcelable layoutManagerSavedState;

    private String selectedSort;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        gridLayoutManager = new GridLayoutManager(MainActivity.this, 2);

        if (savedInstanceState != null) {
            selectedSort = savedInstanceState.getString(Constant.KEY_SELECTED_CATEGORY);
            layoutManagerSavedState = savedInstanceState.getParcelable(Constant.LAYOUT_MANAGER);
        }else{
            selectedSort = Constant.CATEGORY_MOST_POPULAR; //default
        }

        onAttachView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_high_rated:
                //do something
                return true;
            case R.id.menu_most_popular:
                //do
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(Constant.KEY_SELECTED_CATEGORY, selectedSort);
        outState.putParcelable(Constant.LAYOUT_MANAGER, rv.getLayoutManager().onSaveInstanceState());
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

        setupRecyclerView();
        presenter.loadData(selectedSort);
    }

    @Override
    public void onDetachView() {
        presenter.onDetach();
    }

    void setupRecyclerView(){
        adapter = new MovieListAdapter();
        rv.setLayoutManager(gridLayoutManager);
        rv.setHasFixedSize(true);
        rv.setAdapter(adapter);

        setupScrollListener();
        swipeRefresh.setOnRefreshListener(this);
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
    public void onDataReceived(List<MovieItem> data, int page) {
        if(swipeRefresh.isRefreshing()) swipeRefresh.setRefreshing(false);
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

    @Override
    public void onLoadingData() {
        swipeRefresh.setRefreshing(true);
    }

    @Override
    public void onFailedReceivedData() {
        if(swipeRefresh.isRefreshing()) swipeRefresh.setRefreshing(false);
        SnackBarBuilder.showMessage(
                parentView,
                getResources().getString(R.string.failed_load_data)
        );
    }

    @Override
    public void onRefresh() {
        presenter.resetPage();
        presenter.loadData(selectedSort);
    }
}
