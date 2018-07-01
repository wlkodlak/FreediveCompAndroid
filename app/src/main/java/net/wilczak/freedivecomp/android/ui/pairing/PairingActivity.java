package net.wilczak.freedivecomp.android.ui.pairing;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.MenuItem;

import net.wilczak.freedivecomp.android.R;
import net.wilczak.freedivecomp.android.domain.race.Race;
import net.wilczak.freedivecomp.android.ui.application.BaseActivity;
import net.wilczak.freedivecomp.android.ui.startinglanes.StartingLanesActivity;

public class PairingActivity extends BaseActivity<PairingViewModel> implements PairingViewModel.InternalView {
    private static final String EXTRA_RACE = "race";

    @Override
    protected int getLayoutId() {
        return R.layout.activity_pairing;
    }

    @Override
    protected Class<PairingViewModel> getViewModelClass() {
        return PairingViewModel.class;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getViewModel().attachView(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(R.string.pairing_title);
    }

    @Override
    protected void onStart() {
        super.onStart();
        getViewModel().onStart();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        getViewModel().attachView(null);
        super.onDestroy();
    }

    public static Intent createIntent(Context context, Race race) {
        return new Intent(context, PairingActivity.class)
                .putExtra(EXTRA_RACE, race);
    }

    @Override
    public void openStartingLanes(Race race) {
        finish();
        startActivity(StartingLanesActivity.createIntent(this, race));
    }
}
