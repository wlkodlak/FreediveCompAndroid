package net.wilczak.freedivecomp.android.remote.remoteservice;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import net.wilczak.freedivecomp.android.remote.messages.ActualResultDto;
import net.wilczak.freedivecomp.android.remote.messages.AuthenticateRequestDto;
import net.wilczak.freedivecomp.android.remote.messages.AuthenticateResponseDto;
import net.wilczak.freedivecomp.android.remote.messages.RaceSearchResultDto;
import net.wilczak.freedivecomp.android.remote.messages.RaceSetupDto;
import net.wilczak.freedivecomp.android.remote.messages.RulesDto;
import net.wilczak.freedivecomp.android.remote.messages.StartingListReportDto;

import org.joda.time.DateTime;
import org.joda.time.Duration;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import io.reactivex.Completable;
import io.reactivex.Single;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RemoteServiceRetrofitProvider implements RemoteServiceProvider {
    private final ConcurrentHashMap<String, RemoteServiceInternal> services;

    public RemoteServiceRetrofitProvider() {
        this.services = new ConcurrentHashMap<>();
    }

    @Override
    public RemoteService getService(String uri) {
        RemoteServiceInternal existing = services.get(uri);
        if (existing != null) return existing;
        RemoteServiceInternal newInstance = new RemoteServiceInternal(uri);
        existing = services.putIfAbsent(uri, newInstance);
        return existing != null ? existing : newInstance;
    }

    private static class RemoteServiceInternal implements RemoteService {
        private final String uri;
        private final RetrofitService retrofitService;
        private String lastSearchQuery;
        private Single<List<RaceSearchResultDto>> lastSearchCall;

        public RemoteServiceInternal(String uri) {
            this.uri = uri;
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                    .build();
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(DateTime.class, new DateTimeAdapter())
                    .registerTypeAdapter(Duration.class, new DurationAdapter())
                    .create();
            this.retrofitService = new Retrofit.Builder()
                    .baseUrl(uri)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build()
                    .create(RetrofitService.class);
        }

        @Override
        public String getUri() {
            return uri;
        }

        @Override
        public Single<List<RaceSearchResultDto>> getGlobalSearch(final String query) {
            synchronized (this) {
                if (lastSearchQuery == null || !lastSearchQuery.equals(query)) {
                    lastSearchQuery = query;
                    lastSearchCall = retrofitService.getGlobalSearch(query).cache();
                }
                return lastSearchCall;
            }
        }

        @Override
        public Single<List<RulesDto>> getRules() {
            return retrofitService.getRules();
        }

        @Override
        public Single<RaceSetupDto> getRaceSetup(String raceId) {
            return retrofitService.getRaceSetup(raceId);
        }

        @Override
        public Single<AuthenticateResponseDto> postAuthenticate(String raceId, AuthenticateRequestDto request) {
            return retrofitService.postAuthenticate(raceId, request);
        }

        @Override
        public Single<StartingListReportDto> getReportStartingLane(String raceId, String startingLaneId) {
            return retrofitService.getReportStartingLane(raceId, startingLaneId);
        }

        @Override
        public Completable postAthleteResult(String raceId, String athleteId, String authenticationToken, ActualResultDto request) {
            return retrofitService.postAthleteResult(raceId, athleteId, authenticationToken, request);
        }
    }

    private static class DurationAdapter extends TypeAdapter<Duration> {
        @Override
        public void write(JsonWriter out, Duration value) throws IOException {
            if (value == null) {
                out.nullValue();
            } else {
                out.value(value.toString());
            }
        }

        @Override
        public Duration read(JsonReader in) throws IOException {
            if (in.peek() == JsonToken.NULL) {
                in.nextNull();
                return null;
            } else {
                return Duration.parse(in.nextString());
            }
        }
    }

    private static class DateTimeAdapter extends TypeAdapter<DateTime> {
        @Override
        public void write(JsonWriter out, DateTime value) throws IOException {
            if (value == null) {
                out.nullValue();
            } else {
                out.value(value.toString());
            }
        }

        @Override
        public DateTime read(JsonReader in) throws IOException {
            if (in.peek() == JsonToken.NULL) {
                in.nextNull();
                return null;
            } else {
                return DateTime.parse(in.nextString());
            }
        }
    }
}
