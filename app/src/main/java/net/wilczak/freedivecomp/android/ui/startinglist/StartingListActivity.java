package net.wilczak.freedivecomp.android.ui.startinglist;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.MenuItem;

import net.wilczak.freedivecomp.android.BR;
import net.wilczak.freedivecomp.android.R;
import net.wilczak.freedivecomp.android.domain.race.Race;
import net.wilczak.freedivecomp.android.domain.start.StartReference;
import net.wilczak.freedivecomp.android.ui.application.BaseActivity;
import net.wilczak.freedivecomp.android.ui.application.DirtyProperties;
import net.wilczak.freedivecomp.android.ui.enterresult.EnterResultActivity;

public class StartingListActivity extends BaseActivity<StartingListViewModel> implements StartingListViewModel.InternalView {
    private static final String EXTRA_RACE = "race";
    private static final String EXTRA_LANE = "lane";

    @Override
    protected int getLayoutId() {
        return R.layout.activity_starting_list;
    }

    @Override
    protected Class<StartingListViewModel> getViewModelClass() {
        return StartingListViewModel.class;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getViewModel().attachView(this);
        getViewModel().setRace((Race) getIntent().getSerializableExtra(EXTRA_RACE));
        getViewModel().setStartingLaneId(getIntent().getStringExtra(EXTRA_LANE));
        setTitle(getViewModel().getTitle());
    }

    @Override
    protected void onStart() {
        super.onStart();
        getViewModel().onStart();
    }

    @Override
    protected void onViewModelChanged(DirtyProperties dirtyProperties) {
        super.onViewModelChanged(dirtyProperties);
        if (dirtyProperties.contains(BR.title)) {
            setTitle(getViewModel().getTitle());
        }
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

    public static Intent createIntent(Context context, Race race, String startingLaneId) {
        return new Intent(context, StartingListActivity.class)
                .putExtra(EXTRA_RACE, race)
                .putExtra(EXTRA_LANE, startingLaneId);
    }

    @Override
    public void openEnterPerformance(Race race, StartReference startReference) {
        startActivity(EnterResultActivity.createIntent(this, race, startReference));
    }
}
