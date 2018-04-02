package net.wilczak.freedivecomp.android.enterresult;

import net.wilczak.freedivecomp.android.R;
import net.wilczak.freedivecomp.android.application.BaseActivity;

public class EnterResultActivity extends BaseActivity<EnterResultViewModel> {
    @Override
    protected int getLayoutId() {
        return R.layout.activity_enter_result;
    }

    @Override
    protected Class<EnterResultViewModel> getViewModelClass() {
        return EnterResultViewModel.class;
    }
}
