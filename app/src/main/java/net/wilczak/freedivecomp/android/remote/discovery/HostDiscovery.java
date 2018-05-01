package net.wilczak.freedivecomp.android.remote.discovery;

import io.reactivex.Observable;

public interface HostDiscovery {
    Observable<String> getHosts();
}
