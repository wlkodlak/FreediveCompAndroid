package net.wilczak.freedivecomp.android.domain.authentication;

import android.content.Context;
import android.content.SharedPreferences;

import net.wilczak.freedivecomp.android.ui.application.ApplicationContext;

import java.util.UUID;

import javax.inject.Inject;

public class DeviceIdSourceCachedRandom implements DeviceIdSource {
    private static final String SHARED_PREFERENCES_NAME = "CachedDeviceId";
    private static final String DEVICE_ID_KEY = "DeviceId";
    private final Context context;

    @Inject
    public DeviceIdSourceCachedRandom(@ApplicationContext Context context) {
        this.context = context;
    }

    @Override
    public String getDeviceId() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        String deviceId = sharedPreferences.getString(DEVICE_ID_KEY, null);
        if (deviceId != null) return deviceId;
        deviceId = UUID.randomUUID().toString();
        sharedPreferences.edit().putString(DEVICE_ID_KEY, deviceId).apply();
        return deviceId;
    }
}
