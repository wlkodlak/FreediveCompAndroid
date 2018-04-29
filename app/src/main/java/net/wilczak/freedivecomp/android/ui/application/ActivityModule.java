package net.wilczak.freedivecomp.android.ui.application;

import android.content.Context;

import dagger.Module;
import dagger.Provides;

@Module(includes = ViewModelsModule.class)
public class ActivityModule {
    private final Context context;

    public ActivityModule(Context context) {
        this.context = context;
    }

    @Provides
    @PerActivity
    @ActivityContext
    public Context getActivityContext() {
        return context;
    }
}
