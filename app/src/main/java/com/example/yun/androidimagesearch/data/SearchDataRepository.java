package com.example.yun.androidimagesearch.data;

import com.example.yun.androidimagesearch.data.network.ApiFactory;
import com.example.yun.androidimagesearch.data.network.ApiInterface;
import com.example.yun.androidimagesearch.domain.model.ResponseData;
import com.example.yun.androidimagesearch.domain.search.SearchDataRepositoryImpl;

import io.reactivex.Observable;

public class SearchDataRepository implements SearchDataRepositoryImpl {

    private static final int DEFAULT_ITEM_SIZE = 20;

    private static final String DEFAULT_QUERY = "";

    private ApiInterface searchService;

    private ResponseData searchResponse;

    private String query = DEFAULT_QUERY;

    private int pageCount = 0;

    private boolean isLoading = false;

    private SearchDataRepository() {
        searchService = ApiFactory.createSearchService();
    }

    private static SearchDataRepository instance;

    public static SearchDataRepository getInstance() {
        if (instance == null) {
            synchronized (SearchDataRepository.class) {
                if (instance == null) {
                    instance = new SearchDataRepository();
                }
            }
        }
        return instance;
    }

    @Override
    public Observable<ResponseData> getSearchData(String query) {
        this.query = query;
        this.pageCount = getNextPage();
        return searchService.getSearchImages(query, pageCount, DEFAULT_ITEM_SIZE);
    }

    @Override
    public void setSearchData(ResponseData responseData) {
        searchResponse = responseData;
    }

    @Override
    public void clearData() {
        searchResponse = null;
        query = DEFAULT_QUERY;
        pageCount = 0;
        isLoading = false;
    }

    @Override
    public String getQuery() {
        return query;
    }

    @Override
    public boolean isLoading() {
        return isLoading;
    }

    @Override
    public boolean isLoadMore() {
        return searchResponse != null && !searchResponse.getMetaData().isEnd();
    }

    @Override
    public void setLoadState(boolean isLoading) {
        this.isLoading = isLoading;
    }


    private int getNextPage() {
        return pageCount + 1;
    }

}
