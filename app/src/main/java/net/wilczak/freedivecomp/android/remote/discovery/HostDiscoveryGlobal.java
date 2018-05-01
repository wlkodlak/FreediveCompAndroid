package net.wilczak.freedivecomp.android.remote.discovery;

import io.reactivex.Observable;

public class HostDiscoveryGlobal implements HostDiscovery {

    public static final String DEFAULT_URI = "https://127.0.0.1/";

    @Override
    public Observable<String> getHosts() {
        return Observable.just(DEFAULT_URI);
    }
}
