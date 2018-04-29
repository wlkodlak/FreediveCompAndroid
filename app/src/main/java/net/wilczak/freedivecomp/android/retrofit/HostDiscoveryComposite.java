package net.wilczak.freedivecomp.android.retrofit;

import com.annimon.stream.Stream;

import java.util.Collection;

import io.reactivex.Observable;

public class HostDiscoveryComposite implements HostDiscovery {
    public final Observable<String> observable;

    public HostDiscoveryComposite(HostDiscovery... parts) {
        this(Stream.of(parts));
    }

    public HostDiscoveryComposite(Collection<HostDiscovery> parts) {
        this(Stream.of(parts));
    }

    private HostDiscoveryComposite(Stream<HostDiscovery> parts) {
        this.observable = Observable.merge(parts.map(HostDiscovery::getHosts).toList());
    }

    @Override
    public Observable<String> getHosts() {
        return observable;
    }
}
