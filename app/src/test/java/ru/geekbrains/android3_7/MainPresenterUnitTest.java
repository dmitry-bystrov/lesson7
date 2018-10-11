package ru.geekbrains.android3_7;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.schedulers.TestScheduler;
import ru.geekbrains.android3_7.di.DaggerTestComponent;
import ru.geekbrains.android3_7.di.TestComponent;
import ru.geekbrains.android3_7.di.modules.TestRepoModule;
import ru.geekbrains.android3_7.mvp.model.api.IUserRepo;
import ru.geekbrains.android3_7.mvp.model.entity.User;
import ru.geekbrains.android3_7.mvp.presenter.MainPresenter;
import ru.geekbrains.android3_7.mvp.view.MainView;

public class MainPresenterUnitTest {

    private MainPresenter presenter;
    private TestScheduler testScheduler;

    @Mock
    MainView mainView;

    @BeforeClass
    public static void setupClass() {
        //Timber.plant(new Timber.DebugTree());
        //Timber.d("setup class");
    }

    @AfterClass
    public static void tearDown() {
//        Timber.d("tearDown class");
    }

    @Before
    public void setup() {
//        Timber.d("setup");
        MockitoAnnotations.initMocks(this);
        testScheduler = new TestScheduler();
        presenter = Mockito.spy(new MainPresenter(testScheduler));
    }

    @After
    public void after() {
        //Timber.d("tearDown");
    }

    @Test
    public void loadInfoSuccess() {
        User user = new User("googlesamples", "avatarUrl");
        TestComponent component = DaggerTestComponent.builder()
                .testRepoModule(new TestRepoModule() {
                    @Override
                    public IUserRepo usersRepo() {
                        IUserRepo repo = super.usersRepo();
                        Mockito.when(repo.getUser("googlesamples")).thenReturn(Observable.just(user));
                        Mockito.when(repo.getUserRepos(user)).thenReturn(Observable.just(new ArrayList<>()));
                        return repo;
                    }
                }).build();

        component.inject(presenter);
        presenter.attachView(mainView);
        Mockito.verify(presenter).loadInfo();
        testScheduler.advanceTimeBy(1, TimeUnit.SECONDS);

        Mockito.verify(mainView).hideLoading();
        Mockito.verify(mainView).showAvatar(user.getAvatarUrl());
        Mockito.verify(mainView).setUsername(user.getLogin());
        Mockito.verify(mainView).updateRepoList();
    }


    @Test
    public void loadInfoFailureUser() {
        Throwable throwable = new RuntimeException("Test message");
        TestComponent component = DaggerTestComponent.builder()
                .testRepoModule(new TestRepoModule() {
                    @Override
                    public IUserRepo usersRepo() {
                        IUserRepo repo = super.usersRepo();
                        Mockito.when(repo.getUser("googlesamples"))
                                .thenReturn(Observable.create(emitter -> emitter.onError(throwable)));
                        return repo;
                    }
                }).build();

        component.inject(presenter);
        presenter.attachView(mainView);
        Mockito.verify(presenter).loadInfo();
        testScheduler.advanceTimeBy(1, TimeUnit.SECONDS);

        Mockito.verify(mainView).showError(throwable.getMessage());
        Mockito.verify(mainView).hideLoading();
    }

    @Test
    public void loadInfoFailureUserRepo() {
        User user = new User("googlesamples", "avatarUrl");
        Throwable throwable = new RuntimeException("Test message");
        TestComponent component = DaggerTestComponent.builder()
                .testRepoModule(new TestRepoModule() {
                    @Override
                    public IUserRepo usersRepo() {
                        IUserRepo repo = super.usersRepo();
                        Mockito.when(repo.getUser("googlesamples")).thenReturn(Observable.just(user));
                        Mockito.when(repo.getUserRepos(user))
                                .thenReturn(Observable.create(emitter -> emitter.onError(throwable)));
                        return repo;
                    }
                }).build();

        component.inject(presenter);
        presenter.attachView(mainView);
        Mockito.verify(presenter).loadInfo();
        testScheduler.advanceTimeBy(1, TimeUnit.SECONDS);

        Mockito.verify(mainView, Mockito.times(2)).hideLoading();
        Mockito.verify(mainView).showAvatar(user.getAvatarUrl());
        Mockito.verify(mainView).setUsername(user.getLogin());
        Mockito.verify(mainView).showError(throwable.getMessage());
    }
}
