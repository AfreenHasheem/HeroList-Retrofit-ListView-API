package com.example.retrofitapi;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface Api {

    //Retrofit divides into root URL and actual name of the API
    String BASE_URL = "https://simplifiedcoding.net/demos/";

    @GET("marvel")
    Call<List<HeroModel>> getHeroes();
}
