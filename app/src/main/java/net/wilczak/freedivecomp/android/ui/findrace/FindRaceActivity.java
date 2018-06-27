package net.wilczak.freedivecomp.android.ui.findrace;

import android.content.Context;
import android.content.Intent;

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

    public static Intent createIntent(Context context) {
        return new Intent(context, FindRaceActivity.class);
    }
}
