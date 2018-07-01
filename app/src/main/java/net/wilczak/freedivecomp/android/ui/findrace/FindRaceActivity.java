package net.wilczak.freedivecomp.android.ui.findrace;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import net.wilczak.freedivecomp.android.R;
import net.wilczak.freedivecomp.android.domain.race.Race;
import net.wilczak.freedivecomp.android.ui.application.BaseActivity;
import net.wilczak.freedivecomp.android.ui.pairing.PairingActivity;
import net.wilczak.freedivecomp.android.ui.startinglanes.StartingLanesActivity;

public class FindRaceActivity extends BaseActivity<FindRaceViewModel> implements FindRaceViewModel.InternalView {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_find_race;
    }

    @Override
    protected Class<FindRaceViewModel> getViewModelClass() {
        return FindRaceViewModel.class;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getViewModel().attachView(this);
        setTitle(R.string.findrace_title);
    }

    @Override
    protected void onStart() {
        super.onStart();
        getViewModel().onStart();
    }

    @Override
    protected void onDestroy() {
        getViewModel().attachView(null);
        super.onDestroy();
    }

    public static Intent createIntent(Context context) {
        return new Intent(context, FindRaceActivity.class);
    }

    @Override
    public void openPairing(Race race) {
        startActivity(PairingActivity.createIntent(this, race));
    }

    @Override
    public void openStartingLanes(Race race) {
        startActivity(StartingLanesActivity.createIntent(this, race));
    }
}
