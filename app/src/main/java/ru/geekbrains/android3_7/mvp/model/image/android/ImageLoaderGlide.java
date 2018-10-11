package ru.geekbrains.android3_7.mvp.model.image.android;

import android.graphics.Bitmap;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import ru.geekbrains.android3_7.NetworkStatus;
import ru.geekbrains.android3_7.mvp.model.cache.ImageCache;
import ru.geekbrains.android3_7.mvp.model.image.ImageLoader;
import timber.log.Timber;

public class ImageLoaderGlide implements ImageLoader<ImageView>
{
    private static final String TAG = "ImageLoaderGlide";

    @Override
    public void loadInto(@Nullable String url, ImageView container)
    {
        if(NetworkStatus.isOnline())
        {
            GlideApp.with(container.getContext()).asBitmap().load(url).listener(new RequestListener<Bitmap>()
            {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource)
                {
                    Timber.e(e,"Image load failed");
                    return false;
                }

                @Override
                public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource)
                {
                    ImageCache.saveImage(url, resource);
                    return false;
                }
            }).into(container);
        }
        else
        {
            if(ImageCache.contains(url))
            {
                GlideApp.with(container.getContext())
                        .load(ImageCache.getFile(url))
                        .into(container);
            }
        }
    }

}
