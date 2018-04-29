package net.wilczak.freedivecomp.android.ui.application;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ApplicationModule {
    private final Context context;

    public ApplicationModule(Context context) {
        this.context = context;
    }

    @Provides
    @Singleton
    @ApplicationContext
    public Context getApplicationContext() {
        return context;
    }
}
