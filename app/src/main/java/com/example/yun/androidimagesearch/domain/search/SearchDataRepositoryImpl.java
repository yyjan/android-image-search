package com.example.yun.androidimagesearch.domain.search;

import com.example.yun.androidimagesearch.domain.model.ResponseData;

import io.reactivex.Observable;

public interface SearchDataRepositoryImpl {

    Observable<ResponseData> getSearchData(String query);

    void setSearchData(ResponseData responseData);

    void clearData();

    String getQuery();

    boolean isLoadMore();

    boolean isLoading();

    void setLoadState(boolean isLoading);

}
