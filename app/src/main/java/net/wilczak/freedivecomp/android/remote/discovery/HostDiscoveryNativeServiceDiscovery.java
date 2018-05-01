package net.wilczak.freedivecomp.android.remote.discovery;

import android.content.Context;
import android.net.nsd.NsdManager;
import android.net.nsd.NsdServiceInfo;
import android.util.Log;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

public class HostDiscoveryNativeServiceDiscovery implements HostDiscovery {
    private static final String SERVICE_TYPE = "_freedivecomp._tcp";
    private final NsdManager nsdManager;

    public HostDiscoveryNativeServiceDiscovery(Context context) {
        this.nsdManager = (NsdManager) context.getSystemService(Context.NSD_SERVICE);
    }

    @Override
    public Observable<String> getHosts() {
        return Observable.create(new HostDiscoverySubscribe(nsdManager));
    }

    private class HostDiscoverySubscribe implements ObservableOnSubscribe<String> {
        private final NsdManager nsdManager;

        public HostDiscoverySubscribe(NsdManager nsdManager) {
            this.nsdManager = nsdManager;
        }

        @Override
        public void subscribe(ObservableEmitter<String> emitter) {
            if (emitter.isDisposed()) return;

            NsdManager.DiscoveryListener listener = new NsdManager.DiscoveryListener() {
                @Override
                public void onStartDiscoveryFailed(String serviceType, int errorCode) {
                    emitter.tryOnError(new IOException("HostDiscovery failed " + errorCode));
                }

                @Override
                public void onStopDiscoveryFailed(String serviceType, int errorCode) {
                }

                @Override
                public void onDiscoveryStarted(String serviceType) {
                }

                @Override
                public void onDiscoveryStopped(String serviceType) {
                }

                @Override
                public void onServiceFound(NsdServiceInfo serviceInfo) {
                    try {
                        URI uri = new URI("https", null, serviceInfo.getHost().getHostAddress(), serviceInfo.getPort(), null, null, null);
                        emitter.onNext(uri.toString());

                    } catch (URISyntaxException e) {
                        Log.w("HostDiscovery", "Could not handle " + serviceInfo.toString(), e);
                    }
                }

                @Override
                public void onServiceLost(NsdServiceInfo serviceInfo) {

                }
            };

            nsdManager.discoverServices(SERVICE_TYPE, NsdManager.PROTOCOL_DNS_SD, listener);
            emitter.setCancellable(() -> nsdManager.stopServiceDiscovery(listener));
        }
    }
}
