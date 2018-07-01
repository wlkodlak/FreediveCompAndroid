package net.wilczak.freedivecomp.android.ui.startinglanes;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.view.View;

import net.wilczak.freedivecomp.android.domain.startinglanes.StartingLane;

public class StartingLanesItemViewModel extends BaseObservable {
    private final int level;
    private final StartingLane lane;
    private OnClickListener clickListener;

    public StartingLanesItemViewModel(StartingLane lane) {
        this.lane = lane;
        this.level = calculateLevel(lane);
    }

    private static int calculateLevel(StartingLane lane) {
        int level = 0;
        while (lane.getParent() != null) {
            level++;
            lane = lane.getParent();
        }
        return level;
    }

    @Bindable
    public String getName() {
        return lane.getShortName();
    }

    @Bindable
    public int getLevel() {
        return level;
    }

    @Bindable
    public boolean isGroup() {
        return lane.getChildren().size() > 0;
    }

    public void onClick(View v) {
        if (clickListener != null) {
            clickListener.onLaneClick(lane);
        }
    }

    public void setClickListener(OnClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public interface OnClickListener {
        void onLaneClick(StartingLane lane);
    }
}
