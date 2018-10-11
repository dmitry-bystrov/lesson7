package ru.geekbrains.android3_7.mvp.di.modules;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import ru.geekbrains.android3_7.mvp.model.api.ApiService;
import ru.geekbrains.android3_7.mvp.model.api.IUserRepo;
import ru.geekbrains.android3_7.mvp.model.cache.ICache;
import ru.geekbrains.android3_7.mvp.model.repo.UsersRepo;

@Module(includes = {ApiModule.class, CacheModule.class})
public class RepoModule {

    @Provides
    public IUserRepo usersRepo(@Named("realm") ICache cache, ApiService api){
        return new UsersRepo(cache, api);
    }
}
