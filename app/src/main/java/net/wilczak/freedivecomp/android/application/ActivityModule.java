package net.wilczak.freedivecomp.android.application;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ActivityModule {
    private final Context context;

    public ActivityModule(Context context) {
        this.context = context;
    }

    @Provides
    @Singleton
    @PerActivity
    public Context getActivityContext() {
        return context;
    }
}
