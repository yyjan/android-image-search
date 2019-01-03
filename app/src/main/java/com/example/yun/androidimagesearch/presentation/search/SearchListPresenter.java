package com.example.yun.androidimagesearch.presentation.search;

import android.text.TextUtils;
import android.util.Log;

import com.example.yun.androidimagesearch.data.SearchDataRepository;
import com.example.yun.androidimagesearch.domain.model.ResponseData;
import com.example.yun.androidimagesearch.domain.search.SearchListBiz;
import com.jakewharton.rxbinding2.support.v7.widget.RecyclerViewScrollEvent;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class SearchListPresenter implements SearchListContract.Presenter {

    private SearchListContract.View view;

    private SearchListBiz searchListBiz;

    private CompositeDisposable compositeDisposable;

    public SearchListPresenter(SearchListContract.View view) {
        this.view = view;
        this.searchListBiz = new SearchListBiz(SearchDataRepository.getInstance());
    }

    @Override
    public void onCreate() {
        if (compositeDisposable == null) {
            compositeDisposable = new CompositeDisposable();
        }
    }

    @Override
    public void onDestroy() {
        if (compositeDisposable != null && !compositeDisposable.isDisposed()) {
            compositeDisposable.dispose();
        }
    }

    @Override
    public void observeSearchView(Observable<CharSequence> observable) {
        compositeDisposable.add(observable
                .debounce(1000, TimeUnit.MILLISECONDS)
                .filter(charSequence -> !TextUtils.isEmpty((CharSequence) charSequence))
                .distinctUntilChanged()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(charSequence -> {
                    view.clearFocus();
                    view.hideKeyboard();
                    onSubmitQuery(charSequence.toString());
                }));
    }

    @Override
    public void observeRecyclerViewScroll(Observable<RecyclerViewScrollEvent> observable) {
        compositeDisposable.add(observable
                .observeOn(AndroidSchedulers.mainThread())
                .filter(s -> {
                    // check loading state, list end
                    return view.scrolledToBottom() && !searchListBiz.isLoading() && searchListBiz.isLoadMore();
                })
                .subscribe(s -> loadNextPage()));
    }

    private void onSubmitQuery(String query) {

        // check query change
        if (!TextUtils.isEmpty(searchListBiz.getQuery()) && !query.equals(searchListBiz.getQuery())) {
            view.clearAdapter();
            searchListBiz.clearRepository();
        }

        // search service
        Observable<ResponseData> observable = searchListBiz.getItems(query);
        compositeDisposable.add(observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> {
                    view.showProgress();
                    searchListBiz.setLoadState(true);
                })
                .doOnTerminate(() -> {
                    view.hideProgress();
                    searchListBiz.setLoadState(false);
                })
                .subscribe(responseData -> {
                            searchListBiz.setResponseData(responseData);
                            loadImageItems(responseData);
                        }
                        , throwable -> {
                            throwable.printStackTrace();
                            view.showError();
                        }));
    }

    private void loadNextPage() {
        String query = searchListBiz.getQuery();
        onSubmitQuery(query);
    }

    private void loadImageItems(ResponseData responseData) {
        if (responseData == null || responseData.getDocuments() == null || responseData.getDocuments().size() == 0) {
            view.showNoResults();
        } else {
            view.showItems(responseData.getDocuments());
        }
    }

}
