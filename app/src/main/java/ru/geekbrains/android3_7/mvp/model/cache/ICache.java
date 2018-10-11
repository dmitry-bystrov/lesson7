package ru.geekbrains.android3_7.mvp.model.cache;

import java.util.List;

import io.reactivex.Observable;
import ru.geekbrains.android3_7.mvp.model.entity.User;
import ru.geekbrains.android3_7.mvp.model.entity.UserRepository;

public interface ICache
{
    void putUser(User user);
    Observable<User> getUser(String username);

    void putUserRepos(User user, List<UserRepository> repositories);
    Observable<List<UserRepository>> getUserRepos(User user);
}
