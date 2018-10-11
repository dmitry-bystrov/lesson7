package ru.geekbrains.android3_7.mvp.presenter;

import ru.geekbrains.android3_7.mvp.view.RepoRowView;

/**
 * Created by stanislav on 3/15/2018.
 */

public interface IRepoListPresenter
{
    void bindRepoListRow(int pos, RepoRowView rowView);
    int getRepoCount();
}
