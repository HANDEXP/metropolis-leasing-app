package org.hand.mas.metropolisleasing.application;

import android.app.Application;

import com.littlemvc.model.request.AsHttpRequestModel;
import com.littlemvc.utl.AsNetWorkUtl;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.utils.StorageUtils;

import org.hand.mas.utl.ConstantUrl;

import java.io.File;

/**
 * Created by gonglixuan on 15/3/11.
 */
public class MSApplication extends Application{
    public static MSApplication application;

    public static MSApplication getApplication(){
        return application;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        application = this;

        AsNetWorkUtl.application = this;
        AsHttpRequestModel.utl = AsNetWorkUtl.getAsNetWorkUtl(ConstantUrl.basicUrl);

        File cacheDir = StorageUtils.getOwnCacheDirectory(getApplicationContext(), "imageloader/Cache");
        ImageLoaderConfiguration config = new ImageLoaderConfiguration
                .Builder(getApplicationContext())
                .memoryCacheExtraOptions(480,800)
                .threadPoolSize(3)
                .denyCacheImageMultipleSizesInMemory()
                .memoryCache(new UsingFreqLimitedMemoryCache(2*1024*1024))
                .memoryCacheSize(2*1024*1024)
                .diskCache(new UnlimitedDiscCache(cacheDir))
                .diskCacheFileCount(100)
                .writeDebugLogs()
                .build();
        ImageLoader.getInstance().init(config);
    }
}
