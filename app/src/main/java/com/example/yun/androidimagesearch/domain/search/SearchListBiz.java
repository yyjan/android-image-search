package com.example.yun.androidimagesearch.domain.search;

import com.example.yun.androidimagesearch.domain.model.ResponseData;

import io.reactivex.Observable;

public class SearchListBiz {

    private SearchDataRepositoryImpl repository;

    public SearchListBiz(SearchDataRepositoryImpl repository) {
        this.repository = repository;
    }

    public Observable<ResponseData> getItems(String query) {
        return repository.getSearchData(query);
    }

    public String getQuery() {
        return repository.getQuery();
    }

    public boolean isLoading() {
        return repository.isLoading();
    }

    public void setLoadState(boolean isLoading){
        repository.setLoadState(isLoading);
    }

    public void clearRepository() {
        repository.clearData();
    }

    public boolean isLoadMore(){
        return repository.isLoadMore();
    }

    public void setResponseData(ResponseData response){
        repository.setSearchData(response);
    }
}
