package net.wilczak.freedivecomp.android.application;

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
        return component;
    }
}
