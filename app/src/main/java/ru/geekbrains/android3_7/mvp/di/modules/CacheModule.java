package ru.geekbrains.android3_7.mvp.di.modules;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import ru.geekbrains.android3_7.mvp.model.cache.AACache;
import ru.geekbrains.android3_7.mvp.model.cache.ICache;
import ru.geekbrains.android3_7.mvp.model.cache.RealmCache;

@Module
public class CacheModule {

    @Named("aa")
    @Provides
    public ICache aaCache(){
        return new AACache();
    }

    @Named("realm")
    @Provides
    public ICache realmCache(){
        return new RealmCache();
    }
}
