package net.wilczak.freedivecomp.android.ui.application;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import net.wilczak.freedivecomp.android.BR;

public abstract class BaseActivity<TViewModel extends BaseViewModel> extends AppCompatActivity {

    private ActivityComponent component;
    private TViewModel viewModel;

    protected abstract int getLayoutId();
    protected abstract Class<TViewModel> getViewModelClass();

    protected TViewModel getViewModel() {
        return viewModel;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = ViewModelProviders.of(this, getComponent().getViewModelFactory()).get(getViewModelClass());
        ViewDataBinding binding = DataBindingUtil.setContentView(this, getLayoutId());
        binding.setVariable(BR.viewModel, viewModel);
        binding.setLifecycleOwner(this);
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
}
