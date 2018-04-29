package net.wilczak.freedivecomp.android.ui.application;

import net.wilczak.freedivecomp.android.ui.enterresult.EnterResultActivity;
import net.wilczak.freedivecomp.android.ui.findrace.FindRaceActivity;
import net.wilczak.freedivecomp.android.ui.pairing.PairingActivity;
import net.wilczak.freedivecomp.android.ui.splash.SplashActivity;
import net.wilczak.freedivecomp.android.ui.startinglanes.StartingLanesActivity;
import net.wilczak.freedivecomp.android.ui.startinglist.StartingListActivity;

import dagger.Component;

@Component(modules = ActivityModule.class, dependencies = ApplicationComponent.class)
@PerActivity
public interface ActivityComponent extends ApplicationComponent {
    ViewModelFactory getViewModelFactory();

    void inject(EnterResultActivity activity);
    void inject(FindRaceActivity activity);
    void inject(PairingActivity activity);
    void inject(SplashActivity activity);
    void inject(StartingLanesActivity activity);
    void inject(StartingListActivity activity);
}
