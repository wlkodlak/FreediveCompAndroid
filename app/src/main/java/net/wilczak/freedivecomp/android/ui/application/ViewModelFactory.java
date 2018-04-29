package net.wilczak.freedivecomp.android.ui.application;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import java.util.Map;

import javax.inject.Inject;
import javax.inject.Provider;

public class ViewModelFactory implements ViewModelProvider.Factory {
    private final Map<Class<?>, Provider<BaseViewModel>> factories;

    @Inject
    public ViewModelFactory(Map<Class<?>, Provider<BaseViewModel>> factories) {
        this.factories = factories;
    }

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        Provider<BaseViewModel> factory = factories.get(modelClass);
        return (T) factory.get();
    }
}
