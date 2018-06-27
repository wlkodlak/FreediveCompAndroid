package net.wilczak.freedivecomp.android.ui.pairing;

import android.content.Context;
import android.content.Intent;

import net.wilczak.freedivecomp.android.R;
import net.wilczak.freedivecomp.android.domain.race.Race;
import net.wilczak.freedivecomp.android.ui.application.BaseActivity;

public class PairingActivity extends BaseActivity<PairingViewModel> {
    private static final String EXTRA_RACE = "race";

    @Override
    protected int getLayoutId() {
        return R.layout.activity_pairing;
    }

    @Override
    protected Class<PairingViewModel> getViewModelClass() {
        return PairingViewModel.class;
    }

    public static Intent createIntent(Context context, Race race) {
        return new Intent(context, PairingActivity.class)
                .putExtra(EXTRA_RACE, race);
    }
}
