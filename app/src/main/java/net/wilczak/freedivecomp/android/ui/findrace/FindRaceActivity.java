package net.wilczak.freedivecomp.android.ui.findrace;

import net.wilczak.freedivecomp.android.R;
import net.wilczak.freedivecomp.android.ui.application.BaseActivity;

public class FindRaceActivity extends BaseActivity<FindRaceViewModel> {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_find_race;
    }

    @Override
    protected Class<FindRaceViewModel> getViewModelClass() {
        return FindRaceViewModel.class;
    }
}
