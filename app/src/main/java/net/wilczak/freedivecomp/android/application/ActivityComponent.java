package net.wilczak.freedivecomp.android.application;

import net.wilczak.freedivecomp.android.enterresult.EnterResultActivity;
import net.wilczak.freedivecomp.android.findrace.FindRaceActivity;
import net.wilczak.freedivecomp.android.pairing.PairingActivity;
import net.wilczak.freedivecomp.android.splash.SplashActivity;
import net.wilczak.freedivecomp.android.startinglanes.StartingLanesActivity;
import net.wilczak.freedivecomp.android.startinglist.StartingListActivity;

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
