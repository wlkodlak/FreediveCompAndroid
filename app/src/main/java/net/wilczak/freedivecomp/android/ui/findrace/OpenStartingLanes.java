package net.wilczak.freedivecomp.android.ui.findrace;

import net.wilczak.freedivecomp.android.domain.race.Race;
import net.wilczak.freedivecomp.android.ui.actions.BaseAction;
import net.wilczak.freedivecomp.android.ui.application.BaseActivity;
import net.wilczak.freedivecomp.android.ui.startinglanes.StartingLanesActivity;

public class OpenStartingLanes extends BaseAction {
    private final Race race;

    public OpenStartingLanes(Race race) {
        this.race = race;
    }

    @Override
    public void execute(BaseActivity activity) {
        activity.startActivity(StartingLanesActivity.createIntent(activity, race));
    }
}
