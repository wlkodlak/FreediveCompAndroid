package net.wilczak.freedivecomp.android.ui.enterresult;

import android.content.Intent;

import net.wilczak.freedivecomp.android.R;
import net.wilczak.freedivecomp.android.domain.race.Race;
import net.wilczak.freedivecomp.android.domain.start.StartReference;
import net.wilczak.freedivecomp.android.ui.application.BaseActivity;
import net.wilczak.freedivecomp.android.ui.startinglist.StartingListActivity;

public class EnterResultActivity extends BaseActivity<EnterResultViewModel> {
    @Override
    protected int getLayoutId() {
        return R.layout.activity_enter_result;
    }

    @Override
    protected Class<EnterResultViewModel> getViewModelClass() {
        return EnterResultViewModel.class;
    }

    public static Intent createIntent(StartingListActivity startingListActivity, Race race, StartReference startReference) {
        return null;
    }
}
