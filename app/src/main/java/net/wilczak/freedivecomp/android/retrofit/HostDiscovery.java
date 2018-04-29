package net.wilczak.freedivecomp.android.retrofit;

import io.reactivex.Observable;

public interface HostDiscovery {
    Observable<String> getHosts();
}
