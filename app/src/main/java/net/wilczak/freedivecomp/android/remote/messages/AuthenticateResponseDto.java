package net.wilczak.freedivecomp.android.remote.messages;

import com.google.gson.annotations.SerializedName;

public class AuthenticateResponseDto {
    @SerializedName("DeviceId")
    private String deviceId;
    @SerializedName("ConnectCode")
    private String connectCode;
    @SerializedName("AuthenticationToken")
    private String authenticationToken;
    @SerializedName("JudgeId")
    private String judgeId;
    @SerializedName("JudgeName")
    private String judgeName;

    public String getDeviceId() {
        return deviceId;
    }

    public AuthenticateResponseDto setDeviceId(String deviceId) {
        this.deviceId = deviceId;
        return this;
    }

    public String getConnectCode() {
        return connectCode;
    }

    public AuthenticateResponseDto setConnectCode(String connectCode) {
        this.connectCode = connectCode;
        return this;
    }

    public String getAuthenticationToken() {
        return authenticationToken;
    }

    public AuthenticateResponseDto setAuthenticationToken(String authenticationToken) {
        this.authenticationToken = authenticationToken;
        return this;
    }

    public String getJudgeId() {
        return judgeId;
    }

    public AuthenticateResponseDto setJudgeId(String judgeId) {
        this.judgeId = judgeId;
        return this;
    }

    public String getJudgeName() {
        return judgeName;
    }

    public AuthenticateResponseDto setJudgeName(String judgeName) {
        this.judgeName = judgeName;
        return this;
    }
}
