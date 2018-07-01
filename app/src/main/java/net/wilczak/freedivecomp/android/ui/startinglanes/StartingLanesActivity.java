package net.wilczak.freedivecomp.android.ui.startinglanes;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.MenuItem;

import net.wilczak.freedivecomp.android.R;
import net.wilczak.freedivecomp.android.domain.race.Race;
import net.wilczak.freedivecomp.android.domain.startinglanes.StartingLane;
import net.wilczak.freedivecomp.android.ui.application.BaseActivity;
import net.wilczak.freedivecomp.android.ui.startinglist.StartingListActivity;

public class StartingLanesActivity extends BaseActivity<StartingLanesViewModel> implements StartingLanesViewModel.InternalView {
    private static final String EXTRA_RACE = "race";

    @Override
    protected int getLayoutId() {
        return R.layout.activity_starting_lanes;
    }

    @Override
    protected Class<StartingLanesViewModel> getViewModelClass() {
        return StartingLanesViewModel.class;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(R.string.startinglanes_title);
        getViewModel().attachView(this);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static Intent createIntent(Context context, Race race) {
        return new Intent(context, StartingLanesActivity.class)
                .putExtra(EXTRA_RACE, race);
    }

    @Override
    public void openStartingList(Race race, StartingLane lane) {
        startActivity(StartingListActivity.createIntent(this, race, lane));
    }
}
