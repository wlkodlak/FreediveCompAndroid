package net.wilczak.freedivecomp.android.remote.remoteservice;

public interface RemoteServiceProvider {
    RemoteService getService(String uri);
}
