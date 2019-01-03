package com.example.yun.androidimagesearch.presentation.search;

import com.example.yun.androidimagesearch.domain.model.Document;
import com.example.yun.androidimagesearch.presentation.base.BasePresenter;
import com.example.yun.androidimagesearch.presentation.base.BaseView;
import com.jakewharton.rxbinding2.support.v7.widget.RecyclerViewScrollEvent;

import java.util.List;

import io.reactivex.Observable;

public interface SearchListContract {

    interface View extends BaseView {

        void showError();

        void showNoResults();

        void hideKeyboard();

        void clearFocus();

        void clearAdapter();

        void showItems(List<Document> items);

        boolean scrolledToBottom();
    }

    interface Presenter extends BasePresenter {

        void observeSearchView(Observable<CharSequence> observable);

        void observeRecyclerViewScroll(Observable<RecyclerViewScrollEvent> observable);
    }

}
