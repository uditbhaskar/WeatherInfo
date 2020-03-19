package com.example.weatherinfo.services.repository;

import com.example.weatherinfo.services.model.DataModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface API {
    String BASE_URL="https://api.openweathermap.org/data/2.5/";
   // String KEY="4a7c67224a312ffae9171bc97f274f47";

            @GET("weather?appid=4a7c67224a312ffae9171bc97f274f47")
            Call<DataModel> getWeatherInfo(@Query(value = "q") String cityName);
}
