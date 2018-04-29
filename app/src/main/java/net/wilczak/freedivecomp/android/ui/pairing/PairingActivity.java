package net.wilczak.freedivecomp.android.ui.pairing;

import net.wilczak.freedivecomp.android.R;
import net.wilczak.freedivecomp.android.ui.application.BaseActivity;

public class PairingActivity extends BaseActivity<PairingViewModel> {
    @Override
    protected int getLayoutId() {
        return R.layout.activity_pairing;
    }

    @Override
    protected Class<PairingViewModel> getViewModelClass() {
        return PairingViewModel.class;
    }
}
