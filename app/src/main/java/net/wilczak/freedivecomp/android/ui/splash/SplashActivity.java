package net.wilczak.freedivecomp.android.ui.splash;

import net.wilczak.freedivecomp.android.R;
import net.wilczak.freedivecomp.android.ui.application.BaseActivity;

public class SplashActivity extends BaseActivity<SplashViewModel> {
    @Override
    protected int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    protected Class<SplashViewModel> getViewModelClass() {
        return SplashViewModel.class;
    }
}
