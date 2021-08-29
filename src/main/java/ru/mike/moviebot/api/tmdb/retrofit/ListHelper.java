package ru.mike.moviebot.api.tmdb.retrofit;

import com.uwetrottmann.tmdb2.entities.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ListHelper {

    @GET("list/{list_id}")
    Call<List> summary(@Path("list_id") Integer listId, @Query("api_key") String apiKey, @Query("language") String lang);

}
