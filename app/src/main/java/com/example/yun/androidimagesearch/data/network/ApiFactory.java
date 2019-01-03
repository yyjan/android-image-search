
package com.example.yun.androidimagesearch.data.network;

import com.example.yun.androidimagesearch.BuildConfig;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiFactory {

    public static ApiInterface createSearchService() {
        return getClient().create(ApiInterface.class);
    }

    private static Retrofit getClient() {
        return new Retrofit.Builder()
                .baseUrl(ApiConstants.API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(makeOkHttpClient())
                .build();
    }

    private static OkHttpClient makeOkHttpClient() {
        return new OkHttpClient.Builder()
                .addInterceptor(makeLoggingInterceptor())
                .addInterceptor(makeHeaderInterceptor())
                .build();
    }

    private static Interceptor makeHeaderInterceptor() {
        return chain -> {
            Request original = chain.request();

            Request request = original.newBuilder()
                    .header(ApiConstants.API_HEADER_AUTHORIZATION, ApiConstants.API_KEY)
                    .method(original.method(), original.body())
                    .build();
            return chain.proceed(request);
        };
    }

    private static HttpLoggingInterceptor makeLoggingInterceptor() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();

        // enable logging
        boolean isDebug = BuildConfig.DEBUG;
        loggingInterceptor.setLevel(isDebug ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE);

        return loggingInterceptor;
    }

}
