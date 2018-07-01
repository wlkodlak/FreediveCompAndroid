package net.wilczak.freedivecomp.android.ui.startinglist;

import android.content.Context;
import android.content.Intent;

import net.wilczak.freedivecomp.android.R;
import net.wilczak.freedivecomp.android.domain.race.Race;
import net.wilczak.freedivecomp.android.domain.startinglanes.StartingLane;
import net.wilczak.freedivecomp.android.ui.application.BaseActivity;

public class StartingListActivity extends BaseActivity<StartingListViewModel> {
    @Override
    protected int getLayoutId() {
        return R.layout.activity_starting_list;
    }

    @Override
    protected Class<StartingListViewModel> getViewModelClass() {
        return StartingListViewModel.class;
    }

    public static Intent createIntent(Context context, Race race, StartingLane lane) {
        return new Intent(context, StartingListActivity.class);
    }
}
