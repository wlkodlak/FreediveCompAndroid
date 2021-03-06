package net.wilczak.freedivecomp.android.ui.application;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.databinding.Observable;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Choreographer;

import net.wilczak.freedivecomp.android.BR;

public abstract class BaseActivity<TViewModel extends BaseViewModel> extends AppCompatActivity {

    private ActivityComponent component;
    private TViewModel viewModel;
    private ViewDataBinding binding;
    private ViewModelPropertyChangedListener viewModelPropertyChangedListener;

    protected abstract int getLayoutId();
    protected abstract Class<TViewModel> getViewModelClass();

    protected TViewModel getViewModel() {
        return viewModel;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = ViewModelProviders.of(this, getComponent().getViewModelFactory()).get(getViewModelClass());
        binding = DataBindingUtil.setContentView(this, getLayoutId());
        binding.setVariable(BR.viewModel, viewModel);
        binding.setLifecycleOwner(this);
        viewModelPropertyChangedListener = new ViewModelPropertyChangedListener();
        viewModel.addOnPropertyChangedCallback(viewModelPropertyChangedListener);
    }

    protected void onViewModelChanged(DirtyProperties dirtyProperties) {
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding.unbind();
        viewModel.removeOnPropertyChangedCallback(viewModelPropertyChangedListener);
        viewModelPropertyChangedListener.onDestroy();
    }

    @SuppressWarnings("unchecked")
    protected <T extends ViewDataBinding> T getBinding() {
        return (T) binding;
    }

    public ActivityComponent getComponent() {
        if (component == null) {
            FreediveApplication application = (FreediveApplication) getApplication();
            component = DaggerActivityComponent.builder()
                    .applicationComponent(application.getComponent())
                    //.activityModule(new ActivityModule(this))
                    .build();
        }
        return component;
    }

    private class ViewModelPropertyChangedListener extends Observable.OnPropertyChangedCallback implements Choreographer.FrameCallback {
        private final Choreographer choreographer;
        private DirtyProperties pendingChanges;
        private boolean refreshPending;

        public ViewModelPropertyChangedListener() {
            this.choreographer = Choreographer.getInstance();
        }

        @Override
        public void onPropertyChanged(Observable sender, int propertyId) {
            if (refreshPending) {
                pendingChanges.add(propertyId);
            } else {
                pendingChanges = new DirtyProperties();
                pendingChanges.add(propertyId);
                refreshPending = true;
                choreographer.postFrameCallback(this);
            }
        }

        public void onDestroy() {
            if (refreshPending) {
                choreographer.removeFrameCallback(this);
            }
        }

        @Override
        public void doFrame(long frameTimeNanos) {
            DirtyProperties frozenChanges = pendingChanges;
            refreshPending = false;
            if (frozenChanges != null) {
                onViewModelChanged(frozenChanges);
            }
        }
    }
}
