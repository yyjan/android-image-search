package com.example.yun.androidimagesearch.data.network;

import com.example.yun.androidimagesearch.domain.model.ResponseData;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("v2/search/image?")
    Observable<ResponseData> getSearchImages(
            @Query("query") String query,
            @Query("page") int page,
            @Query("size") int size);

}
