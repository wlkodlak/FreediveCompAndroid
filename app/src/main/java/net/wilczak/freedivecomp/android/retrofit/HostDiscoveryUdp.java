package net.wilczak.freedivecomp.android.retrofit;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.nio.charset.Charset;
import java.util.Collections;

import io.reactivex.Observable;

public class HostDiscoveryUdp implements HostDiscovery {
    private static final int DISCOVERY_PORT = 51693;
    private static final String REQUEST_STRING = "FreediveComp.Discover";
    private static final String RESPONSE_PREFIX = "FreediveComp.Response:";
    private static final byte[] REQUEST_BYTES = REQUEST_STRING.getBytes(Charset.forName("UTF-8"));
    public static final int RESPONSE_BUFFER_SIZE = 512;

    @Override
    public Observable<String> getHosts() {
        return Observable.create(emitter -> {
            if (emitter.isDisposed()) return;
            DatagramSocket udp = new DatagramSocket();
            for (NetworkInterface network : Collections.list(NetworkInterface.getNetworkInterfaces())) {
                if (network.isLoopback() || !network.isUp()) continue;
                for (InterfaceAddress address : network.getInterfaceAddresses()) {
                    InetAddress broadcast = address.getBroadcast();
                    if (broadcast == null) continue;
                    DatagramPacket request = new DatagramPacket(REQUEST_BYTES, REQUEST_BYTES.length, broadcast, DISCOVERY_PORT);
                    udp.send(request);
                }
            }
            emitter.setCancellable(() -> {
                if (!udp.isClosed()) {
                    udp.close();
                }
            });
            byte[] responseBuffer = new byte[RESPONSE_BUFFER_SIZE];
            DatagramPacket responsePacket = new DatagramPacket(responseBuffer, RESPONSE_BUFFER_SIZE);
            while (!emitter.isDisposed()) {
                try {
                    udp.receive(responsePacket);
                    String response = new String(responsePacket.getData(), 0, responsePacket.getLength(), Charset.forName("UTF-8"));
                    if (response.startsWith(RESPONSE_PREFIX)) {
                        String uri = response.substring(RESPONSE_PREFIX.length());
                        emitter.onNext(uri);
                    }
                } catch (IOException e) {
                    emitter.tryOnError(e);
                    break;
                }
            }
        });
    }
}
