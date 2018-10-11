package ru.geekbrains.android3_7.mvp.presenter;

import android.annotation.SuppressLint;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import javax.inject.Inject;

import io.reactivex.Scheduler;
import ru.geekbrains.android3_7.mvp.model.api.IUserRepo;
import ru.geekbrains.android3_7.mvp.model.entity.User;
import ru.geekbrains.android3_7.mvp.view.MainView;
import ru.geekbrains.android3_7.mvp.view.RepoRowView;
import timber.log.Timber;

@InjectViewState
public class MainPresenter extends MvpPresenter<MainView> {
    class RepoListPresenter implements IRepoListPresenter {

        @Override
        public void bindRepoListRow(int pos, RepoRowView rowView) {
            if (user != null) {
                rowView.setTitle(user.getRepos().get(pos).getName());
            }
        }

        @Override
        public int getRepoCount() {
            return user == null ? 0 : user.getRepos().size();
        }
    }

    private Scheduler scheduler;
    @Inject
    IUserRepo userRepo;

    private IRepoListPresenter repoListPresenter = new RepoListPresenter();

    private User user;

    public MainPresenter(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        Timber.d("onFirstViewAttach");
        loadInfo();
    }

    @SuppressLint("CheckResult")
    public void loadInfo() {
        userRepo.getUser("googlesamples")
                .observeOn(scheduler)
                .subscribe(user -> {
                    this.user = user;
                    getViewState().hideLoading();
                    getViewState().showAvatar(user.getAvatarUrl());
                    getViewState().setUsername(user.getLogin());
                    userRepo.getUserRepos(user)
                            .observeOn(scheduler)
                            .subscribe(userRepositories -> {
                                this.user.setRepos(userRepositories);
                                getViewState().updateRepoList();
                            }, throwable -> {
                                getViewState().showError(throwable.getMessage());
                                getViewState().hideLoading();
                            });
                }, throwable -> {
                    Timber.e(throwable);
                    getViewState().showError(throwable.getMessage());
                    getViewState().hideLoading();
                });

    }

    public IRepoListPresenter getRepoListPresenter() {
        return repoListPresenter;
    }
}
