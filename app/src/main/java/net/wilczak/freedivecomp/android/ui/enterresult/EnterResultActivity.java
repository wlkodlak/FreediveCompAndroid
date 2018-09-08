package net.wilczak.freedivecomp.android.ui.enterresult;

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

public class EnterResultActivity extends BaseActivity<EnterResultViewModel> implements EnterResultViewModel.InternalView {
    private static final String EXTRA_RACE = "race";
    private static final String EXTRA_START = "start";

    @Override
    protected int getLayoutId() {
        return R.layout.activity_enter_result;
    }

    @Override
    protected Class<EnterResultViewModel> getViewModelClass() {
        return EnterResultViewModel.class;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getViewModel().attachView(this);
        getViewModel().setRace((Race) getIntent().getSerializableExtra(EXTRA_RACE));
        getViewModel().setStartReference((StartReference) getIntent().getSerializableExtra(EXTRA_START));
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

    public static Intent createIntent(Context context, Race race, StartReference startReference) {
        return new Intent(context, EnterResultActivity.class).putExtra(EXTRA_RACE, race).putExtra(EXTRA_START, startReference);
    }

    public static StartReference getResultStartReference(Intent resultIntent) {
        return (StartReference) resultIntent.getSerializableExtra(EXTRA_START);
    }

    @Override
    public void showPerformanceSelectionDialog() {
        AddPenalizationDialog
                .buildSelectionDialog(
                        this,
                        getViewModel().getPenalizationOptions(),
                        getViewModel()
                ).show();
    }

    @Override
    public void showPerformanceInputDialog() {
        AddPenalizationDialog
                .buildInputDialog(
                        this,
                        getViewModel().getShowPenalizationInput(),
                        getViewModel()
                ).show();
    }

    @Override
    public void showPerformanceCustomDialog() {
        AddPenalizationDialog
                .buildCustomDialog(
                        this,
                        getViewModel().getCustomPenalizationUnit(),
                        getViewModel()
                ).show();
    }

    @Override
    public void goToNextAthlete() {
        setResult(RESULT_OK, createIntent(this, getViewModel().getRace(), getViewModel().getStartReference()));
        finish();
    }
}
