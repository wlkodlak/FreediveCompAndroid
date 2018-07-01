package net.wilczak.freedivecomp.android.ui.startinglanes;

import android.view.ViewGroup;

import net.wilczak.freedivecomp.android.R;
import net.wilczak.freedivecomp.android.ui.application.BindableList;

public class StartingLanesTemplateSelector implements BindableList.TemplateSelector {
    private static final int TYPE_GROUP = 1;
    private static final int TYPE_LEAF = 2;

    @Override
    public int getItemType(Object rawViewModel) {
        StartingLanesItemViewModel viewModel = (StartingLanesItemViewModel) rawViewModel;
        return viewModel.isGroup() ? TYPE_GROUP : TYPE_LEAF;
    }

    @Override
    public BindableList.BindableViewHolder createViewHolder(ViewGroup parent, int itemType) {
        return new BindableList.BindableLayoutViewHolder(parent, getLayoutId(itemType));
    }

    private int getLayoutId(int itemType) {
        switch (itemType) {
            case TYPE_GROUP:
                return R.layout.item_startinglane_group;
            case TYPE_LEAF:
                return R.layout.item_startinglane_leaf;
            default:
                return 0;
        }
    }
}
