package ru.geekbrains.android3_7.di.modules;

import org.mockito.Mockito;

import dagger.Module;
import dagger.Provides;
import ru.geekbrains.android3_7.mvp.model.repo.UsersRepo;
import ru.geekbrains.android3_7.mvp.model.api.IUserRepo;

@Module
public class TestRepoModule {

    @Provides
    public IUserRepo usersRepo(){
        return Mockito.mock(IUserRepo.class);
    }

}
