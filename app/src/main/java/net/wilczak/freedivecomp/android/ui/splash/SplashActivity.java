package net.wilczak.freedivecomp.android.ui.splash;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import net.wilczak.freedivecomp.android.R;
import net.wilczak.freedivecomp.android.domain.race.Race;
import net.wilczak.freedivecomp.android.ui.application.BaseActivity;
import net.wilczak.freedivecomp.android.ui.findrace.FindRaceActivity;
import net.wilczak.freedivecomp.android.ui.pairing.PairingActivity;
import net.wilczak.freedivecomp.android.ui.startinglanes.StartingLanesActivity;

public class SplashActivity extends BaseActivity<SplashViewModel> implements SplashViewModel.InternalView {
    @Override
    protected int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    protected Class<SplashViewModel> getViewModelClass() {
        return SplashViewModel.class;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getViewModel().setView(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        getViewModel().onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        getViewModel().onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getViewModel().setView(null);
    }

    @Override
    public void openFindRace() {
        Intent findRaceIntent = FindRaceActivity.createIntent(this);
        startActivity(findRaceIntent);
        finish();
    }

    @Override
    public void openPairing(Race race) {
        Intent findRaceIntent = new Intent(this, FindRaceActivity.class);
        Intent pairingIntent = PairingActivity.createIntent(this, race);
        startActivities(new Intent[] { findRaceIntent, pairingIntent });
        finish();
    }

    @Override
    public void openStartingLanes(Race race) {
        Intent findRaceIntent = new Intent(this, FindRaceActivity.class);
        Intent startingLanesIntent = StartingLanesActivity.createIntent(this, race);
        startActivities(new Intent[] { findRaceIntent, startingLanesIntent });
        finish();
    }
}
