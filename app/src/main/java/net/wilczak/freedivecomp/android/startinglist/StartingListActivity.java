package net.wilczak.freedivecomp.android.startinglist;

import net.wilczak.freedivecomp.android.R;
import net.wilczak.freedivecomp.android.application.BaseActivity;

public class StartingListActivity extends BaseActivity<StartingListViewModel> {
    @Override
    protected int getLayoutId() {
        return R.layout.activity_starting_list;
    }

    @Override
    protected Class<StartingListViewModel> getViewModelClass() {
        return StartingListViewModel.class;
    }
}
