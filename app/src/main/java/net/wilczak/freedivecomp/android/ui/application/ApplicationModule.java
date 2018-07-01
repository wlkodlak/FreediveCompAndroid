package net.wilczak.freedivecomp.android.ui.application;

import android.arch.persistence.room.Room;
import android.content.Context;

import net.wilczak.freedivecomp.android.domain.authentication.AuthenticateUseCase;
import net.wilczak.freedivecomp.android.domain.authentication.AuthenticateUseCaseImpl;
import net.wilczak.freedivecomp.android.domain.authentication.DeviceIdSource;
import net.wilczak.freedivecomp.android.domain.authentication.DeviceIdSourceCachedRandom;
import net.wilczak.freedivecomp.android.domain.race.RaceLocalRepository;
import net.wilczak.freedivecomp.android.domain.race.RaceRepository;
import net.wilczak.freedivecomp.android.domain.race.SearchRacesUseCase;
import net.wilczak.freedivecomp.android.domain.race.SearchRacesUseCaseImpl;
import net.wilczak.freedivecomp.android.domain.race.SelectRaceUseCase;
import net.wilczak.freedivecomp.android.domain.race.SelectRaceUseCaseImpl;
import net.wilczak.freedivecomp.android.domain.rules.DisciplinesDtoRepository;
import net.wilczak.freedivecomp.android.domain.rules.DisciplinesLocalDtoRepository;
import net.wilczak.freedivecomp.android.domain.rules.RulesDtoRepository;
import net.wilczak.freedivecomp.android.domain.rules.RulesLocalDtoRepository;
import net.wilczak.freedivecomp.android.domain.rules.RulesProvider;
import net.wilczak.freedivecomp.android.domain.rules.RulesProviderImpl;
import net.wilczak.freedivecomp.android.domain.start.EnterPerformanceUseCase;
import net.wilczak.freedivecomp.android.domain.start.EnterPerformanceUseCaseImpl;
import net.wilczak.freedivecomp.android.domain.start.ShowStartsUseCase;
import net.wilczak.freedivecomp.android.domain.start.ShowStartsUseCaseImpl;
import net.wilczak.freedivecomp.android.domain.start.StartsLocalRepository;
import net.wilczak.freedivecomp.android.domain.start.StartsRepository;
import net.wilczak.freedivecomp.android.domain.start.SynchronizeStartsUseCase;
import net.wilczak.freedivecomp.android.domain.start.SynchronizeStartsUseCaseImpl;
import net.wilczak.freedivecomp.android.domain.startinglanes.StartingLanesDtoRepository;
import net.wilczak.freedivecomp.android.domain.startinglanes.StartingLanesLocalDtoRepository;
import net.wilczak.freedivecomp.android.domain.startinglanes.StartingLanesRepository;
import net.wilczak.freedivecomp.android.domain.startinglanes.StartingLanesRepositoryImpl;
import net.wilczak.freedivecomp.android.remote.discovery.HostDiscovery;
import net.wilczak.freedivecomp.android.remote.discovery.HostDiscoveryComposite;
import net.wilczak.freedivecomp.android.remote.discovery.HostDiscoveryGlobal;
import net.wilczak.freedivecomp.android.remote.discovery.HostDiscoveryNativeServiceDiscovery;
import net.wilczak.freedivecomp.android.remote.discovery.HostDiscoveryUdp;
import net.wilczak.freedivecomp.android.remote.remoteservice.RemoteServiceProvider;
import net.wilczak.freedivecomp.android.remote.remoteservice.RemoteServiceRetrofitProvider;
import net.wilczak.freedivecomp.android.room.AppDatabase;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@Module(includes = ViewModelsModule.class)
public class ApplicationModule {
    private final Context context;

    public ApplicationModule(Context context) {
        this.context = context;
    }

    @Provides
    @Singleton
    @ApplicationContext
    public Context getApplicationContext() {
        return context;
    }

    @Provides
    @AndroidScheduler
    public Scheduler getAndroidScheduler() {
        return AndroidSchedulers.mainThread();
    }

    @Provides
    @BackgroundScheduler
    public Scheduler getBackgroundScheduler() {
        return Schedulers.io();
    }

    @Provides
    @Singleton
    public AppDatabase getAppDatabase() {
        return Room
                .databaseBuilder(context, AppDatabase.class, "freedivecomp")
                .fallbackToDestructiveMigration()
                .build();
    }

    @Provides
    @Singleton
    public RemoteServiceProvider getRemoteServiceProvider(RemoteServiceRetrofitProvider impl) {
        return impl;
    }

    @Provides
    @Singleton
    public HostDiscovery getHostDiscovery() {
        return new HostDiscoveryComposite(
                new HostDiscoveryGlobal(),
                new HostDiscoveryUdp(),
                new HostDiscoveryNativeServiceDiscovery(context)
        );
    }

    @Provides
    @Singleton
    public AuthenticateUseCase bindAuthenticate(DeviceIdSource deviceIdSource, RaceRepository raceRepository, RemoteServiceProvider remoteServiceProvider) {
        String deviceId = deviceIdSource.getDeviceId();
        int delay = 5;  // 5 seconds interval between attempts
        return new AuthenticateUseCaseImpl(deviceId, raceRepository, remoteServiceProvider, delay);
    }

    @Provides
    @Singleton
    public DeviceIdSource bindDeviceIdSource(DeviceIdSourceCachedRandom impl) {
        return impl;
    }

    @Provides
    @Singleton
    public RaceRepository bindRaceRepository(RaceLocalRepository impl) {
        return impl;
    }

    @Provides
    @Singleton
    public SearchRacesUseCase bindSearchRaces(SearchRacesUseCaseImpl impl) {
        return impl;
    }

    @Provides
    @Singleton
    public SelectRaceUseCase bindSelectRace(SelectRaceUseCaseImpl impl) {
        return impl;
    }

    @Provides
    @Singleton
    public DisciplinesDtoRepository bindDisciplinesRepository(DisciplinesLocalDtoRepository impl) {
        return impl;
    }

    @Provides
    @Singleton
    public RulesDtoRepository bindRulesRepository(RulesLocalDtoRepository impl) {
        return impl;
    }

    @Provides
    @Singleton
    public RulesProvider bindRulesProvider(RulesProviderImpl impl) {
        return impl;
    }

    @Provides
    @Singleton
    public EnterPerformanceUseCase bindEnterPerformance(EnterPerformanceUseCaseImpl impl) {
        return impl;
    }

    @Provides
    @Singleton
    public ShowStartsUseCase bindShowStarts(ShowStartsUseCaseImpl impl) {
        return impl;
    }

    @Provides
    @Singleton
    public StartsRepository bindStartsRepository(StartsLocalRepository impl) {
        return impl;
    }

    @Provides
    @Singleton
    public SynchronizeStartsUseCase bindStarts(SynchronizeStartsUseCaseImpl impl) {
        return impl;
    }

    @Provides
    @Singleton
    public StartingLanesRepository bindStartingLanesRepository(StartingLanesRepositoryImpl impl) {
        return impl;
    }

    @Provides
    @Singleton
    public StartingLanesDtoRepository bindStartingLanesDtoRepository(StartingLanesLocalDtoRepository impl) {
        return impl;
    }

    @Provides
    @Singleton
    public Localization bindLocalization(LocalizationNative impl) {
        return impl;
    }
}
