package net.wilczak.freedivecomp.android.ui.startinglanes;

import net.wilczak.freedivecomp.android.R;
import net.wilczak.freedivecomp.android.ui.application.BaseActivity;

public class StartingLanesActivity extends BaseActivity<StartingLanesViewModel> {
    @Override
    protected int getLayoutId() {
        return R.layout.activity_starting_lanes;
    }

    @Override
    protected Class<StartingLanesViewModel> getViewModelClass() {
        return StartingLanesViewModel.class;
    }
}