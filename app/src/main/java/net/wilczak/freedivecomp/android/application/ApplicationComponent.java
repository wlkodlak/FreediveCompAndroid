package net.wilczak.freedivecomp.android.application;

import dagger.Component;

@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {
    void inject(FreediveApplication application);
}
