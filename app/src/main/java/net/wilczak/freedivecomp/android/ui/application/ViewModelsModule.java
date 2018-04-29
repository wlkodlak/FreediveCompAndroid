package net.wilczak.freedivecomp.android.ui.application;

import net.wilczak.freedivecomp.android.ui.enterresult.EnterResultViewModel;
import net.wilczak.freedivecomp.android.ui.findrace.FindRaceViewModel;
import net.wilczak.freedivecomp.android.ui.pairing.PairingViewModel;
import net.wilczak.freedivecomp.android.ui.splash.SplashViewModel;
import net.wilczak.freedivecomp.android.ui.startinglanes.StartingLanesViewModel;
import net.wilczak.freedivecomp.android.ui.startinglist.StartingListViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.ClassKey;
import dagger.multibindings.IntoMap;

@Module
public abstract class ViewModelsModule {
    @Binds
    @IntoMap
    @ClassKey(EnterResultViewModel.class)
    public abstract BaseViewModel getEnterResultViewModel(EnterResultViewModel viewModel);

    @Binds
    @IntoMap
    @ClassKey(FindRaceViewModel.class)
    public abstract BaseViewModel getFindRaceViewModel(FindRaceViewModel viewModel);

    @Binds
    @IntoMap
    @ClassKey(PairingViewModel.class)
    public abstract BaseViewModel getPairingViewModel(PairingViewModel viewModel);

    @Binds
    @IntoMap
    @ClassKey(SplashViewModel.class)
    public abstract BaseViewModel getSplashViewModel(SplashViewModel viewModel);

    @Binds
    @IntoMap
    @ClassKey(StartingLanesViewModel.class)
    public abstract BaseViewModel getStartingLanesViewModel(StartingLanesViewModel viewModel);

    @Binds
    @IntoMap
    @ClassKey(StartingListViewModel.class)
    public abstract BaseViewModel getStartingListViewModel(StartingListViewModel viewModel);}
