package com.assigment.newsapp.api;

import com.assigment.newsapp.api.data.News;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {

    String BASE_URL = "http://newsapi.org/v2/";

    @GET("everything")
    Call<News> getNews(@Query("q") String q,
                       @Query("from") String from,
                       @Query("sortBy") String sortBy,
                       @Query("apiKey") String apiKey
    );
}
