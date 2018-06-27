package net.wilczak.freedivecomp.android.ui.startinglanes;

import android.content.Context;
import android.content.Intent;

import net.wilczak.freedivecomp.android.R;
import net.wilczak.freedivecomp.android.domain.race.Race;
import net.wilczak.freedivecomp.android.ui.application.BaseActivity;
import net.wilczak.freedivecomp.android.ui.pairing.PairingActivity;

public class StartingLanesActivity extends BaseActivity<StartingLanesViewModel> {
    private static final String EXTRA_RACE = "race";

    @Override
    protected int getLayoutId() {
        return R.layout.activity_starting_lanes;
    }

    @Override
    protected Class<StartingLanesViewModel> getViewModelClass() {
        return StartingLanesViewModel.class;
    }

    public static Intent createIntent(Context context, Race race) {
        return new Intent(context, StartingLanesActivity.class)
                .putExtra(EXTRA_RACE, race);
    }
}
