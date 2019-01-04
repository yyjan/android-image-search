package com.example.yun.androidimagesearch.presentation.search;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.example.yun.androidimagesearch.R;
import com.example.yun.androidimagesearch.domain.model.Document;
import com.jakewharton.rxbinding2.support.v7.widget.RxRecyclerView;
import com.jakewharton.rxbinding2.support.v7.widget.RxSearchView;

import java.util.List;

public class SearchListActivity extends AppCompatActivity implements SearchListContract.View {

    private SearchView searchView;

    private RecyclerView recyclerView;

    private ProgressBar progressBar;

    private LinearLayout messageLayout;

    private LinearLayoutManager linearLayoutManager;

    private SearchImageListAdapter adapter;

    private SearchListContract.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_list);

        // set up the toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // set adapter
        adapter = new SearchImageListAdapter();
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        // set progress
        progressBar = findViewById(R.id.progress);

        // set notification message
        messageLayout = findViewById(R.id.layout_message);

        // create presenter
        if (presenter == null) {
            presenter = new SearchListPresenter(this);
        }
        presenter.onCreate();

        // create observable for scroll events
        presenter.observeRecyclerViewScroll(RxRecyclerView.scrollEvents(recyclerView));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (presenter != null) {
            presenter.onDestroy();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // inflate menu
        getMenuInflater().inflate(R.menu.menu_search, menu);

        // find searchView
        MenuItem searchItem = menu.findItem(R.id.action_search);

        searchView = (SearchView) searchItem.getActionView();
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setQueryHint(getString(R.string.hint_search_query));
        searchView.setIconifiedByDefault(false);

        // create observable for query changes
        presenter.observeSearchView(RxSearchView.queryTextChanges(searchView));

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void showError() {
        makeSnackBar(getString(R.string.message_error));

        // show message layout
        setUpMessageLayout(true);
    }

    @Override
    public void showNoResults() {
        makeSnackBar(getString(R.string.message_no_results));

        // show message layout
        setUpMessageLayout(true);
    }

    @Override
    public void hideKeyboard() {

        // hide keyboard
        View view = getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null && view.getWindowToken() != null) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }

    @Override
    public void clearFocus() {
        searchView.clearFocus();
    }

    @Override
    public void clearAdapter() {
        adapter.clear();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void showItems(List<Document> items) {
        adapter.addItems(items);
        adapter.notifyDataSetChanged();

        // hide message layout
        setUpMessageLayout(false);
    }

    @Override
    public boolean scrolledToBottom() {
        return !recyclerView.canScrollVertically(1);
    }

    private void makeSnackBar(String text) {
        Snackbar snackbar = Snackbar
                .make(findViewById(R.id.container), text, Snackbar.LENGTH_SHORT)
                .setActionTextColor(getResources().getColor(R.color.colorPrimary))
                .setAction(R.string.message_retry, v -> searchView.setIconified(false));
        snackbar.show();
    }

    private void setUpMessageLayout(Boolean isShow) {
        if (isShow) {
            recyclerView.setVisibility(View.GONE);
            messageLayout.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            messageLayout.setVisibility(View.GONE);
        }
    }

}
