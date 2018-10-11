package ru.geekbrains.android3_7.mvp.model.api;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import ru.geekbrains.android3_7.mvp.model.entity.User;
import ru.geekbrains.android3_7.mvp.model.entity.UserRepository;


public interface ApiService
{
    @GET("users/{user}")
    Observable<User> getUser(@Path("user") String userName);

    @GET("users/{user}/repos")
    Observable<List<UserRepository>> getUserRepos(@Path("user") String userName);

}
