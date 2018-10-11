package ru.geekbrains.android3_7;

import android.util.Log;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.observers.TestObserver;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import ru.geekbrains.android3_7.di.DaggerTestComponent;
import ru.geekbrains.android3_7.di.TestComponent;
import ru.geekbrains.android3_7.mvp.di.modules.ApiModule;
import ru.geekbrains.android3_7.mvp.model.api.IUserRepo;
import ru.geekbrains.android3_7.mvp.model.entity.User;
import ru.geekbrains.android3_7.mvp.model.entity.UserRepository;

import static  org.junit.Assert.assertEquals;

public class UserRepoInstrumentedTest {

    @Inject
    IUserRepo usersRepo;

    private static MockWebServer mockWebServer;

    @BeforeClass
    public static void setupClass() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
    }

    @AfterClass
    public static void tearDownClass() throws IOException {
        mockWebServer.shutdown();
    }

    @Before
    public void setup() {
        TestComponent component = DaggerTestComponent
                .builder()
                .apiModule(new ApiModule() {
                               @Override
                               public String baseUrl() {
                                   return mockWebServer.url("/").toString();
                               }
                           }
                ).build();
        component.inject(this);
    }

    @Test
    public void getUser(){
        mockWebServer.enqueue(createUserResponse("someuser", "someurl"));
        TestObserver<User> observer = new TestObserver<>();
        usersRepo.getUser("somelogin").subscribe(observer);

        observer.awaitTerminalEvent();

        observer.assertValueCount(1);
        assertEquals(observer.values().get(0).getLogin(), "someuser");
        assertEquals(observer.values().get(0).getAvatarUrl(), "someurl");
    }

    private MockResponse createUserResponse(String login, String avatarUrl){
        String body = "{\"login\":\"" + login + "\", \"avatar_url\":\"" + avatarUrl + "\"}";
        return new MockResponse()
                .setBody(body);
    }

    @Test
    public void getUserRepos(){
        mockWebServer.enqueue(createRepoResponse("some_id", "some_name"));
        TestObserver<List<UserRepository>> observer = new TestObserver<>();
        usersRepo.getUserRepos(new User("some_user", "some_avatar_url"))
                .subscribe(observer);

        observer.awaitTerminalEvent();

        observer.assertValueCount(1);
        assertEquals(observer.values().get(0).get(0).getId(), "some_id");
        assertEquals(observer.values().get(0).get(0).getName(), "some_name");
    }

    private MockResponse createRepoResponse(String id, String name){
        String body = "[{\"id\": \"" + id + "\", \"name\": \"" + name + "\"}]";
        return new MockResponse()
                .setBody(body);
    }
}
