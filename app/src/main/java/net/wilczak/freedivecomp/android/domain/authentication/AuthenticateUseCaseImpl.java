package net.wilczak.freedivecomp.android.domain.authentication;

import net.wilczak.freedivecomp.android.domain.race.Race;
import net.wilczak.freedivecomp.android.domain.race.RaceRepository;
import net.wilczak.freedivecomp.android.remote.messages.AuthenticateRequestDto;
import net.wilczak.freedivecomp.android.remote.remoteservice.RemoteService;
import net.wilczak.freedivecomp.android.remote.remoteservice.RemoteServiceProvider;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;

public class AuthenticateUseCaseImpl implements AuthenticateUseCase {
    private final String deviceId;
    private final RaceRepository raceRepository;
    private final RemoteServiceProvider remoteServiceProvider;
    private final long delay;

    public AuthenticateUseCaseImpl(String deviceId, RaceRepository raceRepository, RemoteServiceProvider remoteServiceProvider, long delay) {
        this.deviceId = deviceId;
        this.raceRepository = raceRepository;
        this.remoteServiceProvider = remoteServiceProvider;
        this.delay = delay;
    }

    @Override
    public Observable<Status> authenticateRace(Race race) {
        StatusImpl status = StatusImpl.initialize(race);
        if (status.isAuthenticated()) return Observable.just(status);
        return authenticateRaceStep(status).share().map(s -> (Status) s);
    }

    private Observable<StatusImpl> authenticateRaceStep(StatusImpl previousStatus) {
        return Observable
                .defer(() -> {
                    RemoteService remoteService = remoteServiceProvider.getService(previousStatus.getRace().getUri());
                    AuthenticateRequestDto request = new AuthenticateRequestDto().setDeviceId(deviceId).setConnectCode(previousStatus.getConnectCode());
                    return remoteService.postAuthenticate(previousStatus.getRace().getRaceId(), request).toObservable();
                })
                .flatMap(response -> raceRepository.getSavedRace(previousStatus.getRaceId()).take(1).flatMap(loaded -> {
                    Race savedRace = new Race(loaded)
                            .setAuthenticationToken(response.getAuthenticationToken())
                            .setJudgeId(response.getJudgeId())
                            .setJudgeName(response.getJudgeName())
                            .setConnectCode(response.getConnectCode());
                    State newState = response.getAuthenticationToken() == null ? State.BETWEEN_ATTEMPTS : State.AUTHENTICATED;
                    return raceRepository
                            .saveRace(savedRace)
                            .andThen(Observable.just(previousStatus.update(savedRace, newState)))
                            .startWith(previousStatus.busy());
                }))
                .onErrorReturn(previousStatus::fail)
                .startWith(previousStatus.busy())
                .flatMap(status -> {
                    if (status.getState() == State.AUTHENTICATED) {
                        return Observable.just(status);
                    } else if (status.getState() == State.FAILED) {
                        return Observable
                                .timer(delay * status.getFailedAttempts(), TimeUnit.SECONDS)
                                .flatMap(t -> authenticateRaceStep(status));
                    } else {
                        return Observable
                                .timer(delay, TimeUnit.SECONDS)
                                .flatMap(t -> authenticateRaceStep(status));
                    }
                });
    }

    private static class StatusImpl implements Status {
        private final Race race;
        private final State state;
        private final Throwable error;
        private final int failedAttempts;

        public static StatusImpl initialize(Race race) {
            State initialState = race.getAuthenticationToken() == null ? State.STARTING : State.AUTHENTICATED;
            return new StatusImpl(race, initialState, null, 0);
        }

        private StatusImpl(Race race, State state, Throwable error, int failedAttempts) {
            this.race = race;
            this.state = state;
            this.error = error;
            this.failedAttempts = failedAttempts;
        }

        public Race getRace() {
            return race;
        }

        public State getState() {
            return state;
        }

        public Throwable getError() {
            return error;
        }

        public int getFailedAttempts() {
            return failedAttempts;
        }

        public String getRaceId() {
            return race.getRaceId();
        }

        public String getConnectCode() {
            return race.getConnectCode();
        }

        public String getAuthenticationToken() {
            return race.getAuthenticationToken();
        }

        public boolean isAuthenticated() {
            return race.getAuthenticationToken() != null;
        }

        public StatusImpl fail(Throwable t) {
            return new StatusImpl(race, State.FAILED, t, failedAttempts + 1);
        }

        public StatusImpl to(State state) {
            return new StatusImpl(race, state, null, 0);
        }

        public StatusImpl update(Race race, State state) {
            return new StatusImpl(race, state, null, 0);
        }

        public StatusImpl busy() {
            return new StatusImpl(race, State.BUSY, null, 0);
        }
    }
}
