package ru.geekbrains.android3_7.mvp.di;

import javax.inject.Singleton;

import dagger.Component;
import ru.geekbrains.android3_7.mvp.di.modules.AppModule;
import ru.geekbrains.android3_7.mvp.di.modules.RepoModule;
import ru.geekbrains.android3_7.mvp.presenter.MainPresenter;

@Singleton
@Component(modules = {
        AppModule.class,
        RepoModule.class
})
public interface AppComponent {
    void inject(MainPresenter presenter);

}
