package net.wilczak.freedivecomp.android.domain.race;

import org.joda.time.DateTime;

public class Race {
    public static final Race MISSING = new Race((String)null);

    private final String raceId;
    private String name;
    private DateTime since;
    private DateTime until;
    private String connectCode;
    private String authenticationToken;
    private String judgeId;
    private String judgeName;
    private String uri;
    private boolean selected;
    private boolean saved;

    public Race(String raceId) {
        this.raceId = raceId;
    }

    public Race(Race original) {
        this.raceId = original.raceId;
        this.name = original.name;
        this.since = original.since;
        this.until = original.until;
        this.connectCode = original.connectCode;
        this.authenticationToken = original.authenticationToken;
        this.judgeId = original.judgeId;
        this.judgeName = original.judgeName;
        this.uri = original.uri;
        this.selected = original.selected;
        this.saved = original.saved;
    }

    public boolean isMissing() {
        return raceId == null;
    }

    public String getRaceId() {
        return raceId;
    }

    public String getName() {
        return name;
    }

    public DateTime getSince() {
        return since;
    }

    public DateTime getUntil() {
        return until;
    }

    public String getUri() {
        return uri;
    }

    public String getConnectCode() {
        return connectCode;
    }

    public String getAuthenticationToken() {
        return authenticationToken;
    }

    public String getJudgeId() {
        return judgeId;
    }

    public String getJudgeName() {
        return judgeName;
    }

    public boolean isSelected() {
        return selected;
    }

    public boolean isSaved() {
        return saved;
    }

    public Race setName(String name) {
        this.name = name;
        return this;
    }

    public Race setSince(DateTime since) {
        this.since = since;
        return this;
    }

    public Race setUntil(DateTime until) {
        this.until = until;
        return this;
    }

    public Race setUri(String uri) {
        this.uri = uri;
        return this;
    }

    public Race setConnectCode(String connectCode) {
        this.connectCode = connectCode;
        return this;
    }

    public Race setAuthenticationToken(String authenticationToken) {
        this.authenticationToken = authenticationToken;
        return this;
    }

    public Race setJudgeId(String judgeId) {
        this.judgeId = judgeId;
        return this;
    }

    public Race setJudgeName(String judgeName) {
        this.judgeName = judgeName;
        return this;
    }

    public Race setSelected(boolean selected) {
        this.selected = selected;
        return this;
    }

    public Race setSaved(boolean saved) {
        this.saved = saved;
        return this;
    }
}
