package net.wilczak.freedivecomp.android.ui.application;

import android.arch.lifecycle.ViewModel;
import android.databinding.Observable;
import android.databinding.PropertyChangeRegistry;

public class BaseViewModel extends ViewModel implements Observable {
    private final PropertyChangeRegistry changeRegistry;

    public BaseViewModel() {
        this.changeRegistry = new PropertyChangeRegistry();
    }

    @Override
    public void addOnPropertyChangedCallback(OnPropertyChangedCallback callback) {
        changeRegistry.add(callback);
    }

    @Override
    public void removeOnPropertyChangedCallback(OnPropertyChangedCallback callback) {
        changeRegistry.remove(callback);
    }

    public void notifyPropertyChanged(int fieldId) {
        changeRegistry.notifyChange(this, fieldId);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        changeRegistry.clear();
    }
}
