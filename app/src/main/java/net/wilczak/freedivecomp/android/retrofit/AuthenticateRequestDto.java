package net.wilczak.freedivecomp.android.retrofit;

import com.google.gson.annotations.SerializedName;

public class AuthenticateRequestDto {
    @SerializedName("DeviceId")
    private String deviceId;
    @SerializedName("ConnectCode")
    private String connectCode;

    public String getDeviceId() {
        return deviceId;
    }

    public AuthenticateRequestDto setDeviceId(String deviceId) {
        this.deviceId = deviceId;
        return this;
    }

    public String getConnectCode() {
        return connectCode;
    }

    public AuthenticateRequestDto setConnectCode(String connectCode) {
        this.connectCode = connectCode;
        return this;
    }
}
