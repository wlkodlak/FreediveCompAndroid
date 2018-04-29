package net.wilczak.freedivecomp.android.ui.application;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

public class FreediveApplication extends Application {
    private ApplicationComponent component;

    @Override
    public void onCreate() {
        super.onCreate();
        getComponent().inject(this);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);
    }

    public ApplicationComponent getComponent() {
        if (component == null) {
            component = DaggerApplicationComponent.builder().build();
        }
        return component;
    }
}
