package net.wilczak.freedivecomp.android.retrofit;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;

public interface RemoteService {
    String getUri();
    Single<List<RaceSearchResultDto>> getGlobalSearch(String query);
    Single<List<RulesDto>> getRules();
    Single<RaceSetupDto> getRaceSetup(String raceId);
    Single<AuthenticateResponseDto> postAuthenticate(String raceId, AuthenticateRequestDto request);
    Single<StartingListReportDto> getReportStartingLane(String raceId, String startingLaneId);
    Completable postAthleteResult(String raceId, String athleteId, String authenticationToken, ActualResultDto request);
}
