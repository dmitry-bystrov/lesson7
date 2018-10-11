package ru.geekbrains.android3_7.mvp.model.entity.realm;

import io.realm.RealmObject;

public class CachedImage extends RealmObject
{
    String url;
    String path;

    public void setUrl(String url)
    {
        this.url = url;
    }

    public void setPath(String path)
    {
        this.path = path;
    }

    public String getPath()
    {
        return path;
    }
}
