package net.wilczak.freedivecomp.android.retrofit;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RetrofitService {
    @GET("api-1.0/global/search")
    Single<List<RaceSearchResultDto>> getGlobalSearch(@Query("query") String query);

    @GET("api-1.0/global/rules")
    Single<List<RulesDto>> getRules();

    @GET("api-1.0/{raceId}/setup")
    Single<RaceSetupDto> getRaceSetup(@Path("raceId") String raceId);

    @POST("api-1.0/{raceId}/auth/authenticate")
    Single<AuthenticateResponseDto> postAuthenticate(@Path("raceId") String raceId, @Body AuthenticateRequestDto request);

    @GET("api-1.0/{raceId}/reports/start/{startingLaneId}")
    Single<StartingListReportDto> getReportStartingLane(@Path("raceId") String raceId, @Path("startingLaneId") String startingLaneId);

    @POST("api-1.0/{raceId}/athletes/{athleteId}/results")
    Completable postAthleteResult(@Path("raceId") String raceId, @Path("athleteId") String athleteId, @Header("X-Authentication-Token") String authenticationToken, @Body ActualResultDto request);
}
