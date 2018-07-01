package net.wilczak.freedivecomp.android.ui.application;

import javax.inject.Singleton;

import dagger.Component;

@Component(modules = ApplicationModule.class)
@Singleton
public interface ApplicationComponent {
    ViewModelFactory getViewModelFactory();
    void inject(FreediveApplication application);
}
