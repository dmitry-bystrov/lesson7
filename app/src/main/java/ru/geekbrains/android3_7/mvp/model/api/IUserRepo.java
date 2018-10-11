package ru.geekbrains.android3_7.mvp.model.api;

import java.util.List;

import io.reactivex.Observable;
import ru.geekbrains.android3_7.mvp.model.entity.User;
import ru.geekbrains.android3_7.mvp.model.entity.UserRepository;


public interface IUserRepo
{
    Observable<User> getUser(String username);
    Observable<List<UserRepository>> getUserRepos(User user);

}
