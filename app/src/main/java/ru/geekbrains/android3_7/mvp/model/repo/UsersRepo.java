package ru.geekbrains.android3_7.mvp.model.repo;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import ru.geekbrains.android3_7.NetworkStatus;
import ru.geekbrains.android3_7.mvp.model.api.ApiService;
import ru.geekbrains.android3_7.mvp.model.api.IUserRepo;
import ru.geekbrains.android3_7.mvp.model.cache.ICache;
import ru.geekbrains.android3_7.mvp.model.entity.User;
import ru.geekbrains.android3_7.mvp.model.entity.UserRepository;

public class UsersRepo implements IUserRepo {

    private ICache cache;
    private ApiService api;

    public UsersRepo(ICache cache, ApiService api) {
        this.cache = cache;
        this.api = api;
    }

    public Observable<User> getUser(String username) {
        if (NetworkStatus.isOnline()) {
            return api.getUser(username).subscribeOn(Schedulers.io())
                    .map(user -> {
                        cache.putUser(user);
                        return user;
                    });
        } else {
            return cache.getUser(username);
        }
    }

    public Observable<List<UserRepository>> getUserRepos(User user) {
        if (NetworkStatus.isOnline()) {
            return api.getUserRepos(user.getLogin()).subscribeOn(Schedulers.io())
                    .map(repos -> {
                        cache.putUserRepos(user, repos);
                        return repos;
                    });
        } else {
            return cache.getUserRepos(user);
        }
    }
}
