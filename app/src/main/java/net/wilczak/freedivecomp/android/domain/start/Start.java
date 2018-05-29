package net.wilczak.freedivecomp.android.domain.start;

import net.wilczak.freedivecomp.android.remote.messages.AthleteProfileDto;
import net.wilczak.freedivecomp.android.remote.messages.PerformanceDto;
import net.wilczak.freedivecomp.android.remote.messages.ReportActualResultDto;
import net.wilczak.freedivecomp.android.remote.messages.ReportStartTimesDto;

public class Start {
    public static final Start MISSING = new Start().setState(StartState.MISSING).freeze();

    private boolean frozen;
    private long internalId;
    private StartState state;
    private String athleteId;
    private String disciplineId;
    private String disciplineName;
    private AthleteProfileDto athlete;
    private ReportStartTimesDto startTimes;
    private PerformanceDto announcement;
    private ReportActualResultDto confirmedResult;
    private ReportActualResultDto localResult;

    public Start() {
    }

    private Start(Start original) {
        this.frozen = false;
        this.state = original.state;
        this.athleteId = original.athleteId;
        this.disciplineId = original.disciplineId;
        this.disciplineName = original.disciplineName;
        this.athlete = original.athlete;
        this.startTimes = original.startTimes;
        this.announcement = original.announcement;
        this.confirmedResult = original.confirmedResult;
        this.localResult = original.localResult;
    }

    public StartReference getReference() {
        return new StartReference(internalId, athleteId, disciplineId);
    }

    public boolean isFrozen() {
        return frozen;
    }

    public long getInternalId() {
        return internalId;
    }

    public StartState getState() {
        return state;
    }

    public String getAthleteId() {
        return athleteId;
    }

    public String getDisciplineId() {
        return disciplineId;
    }

    public String getDisciplineName() {
        return disciplineName;
    }

    public AthleteProfileDto getAthlete() {
        return athlete;
    }

    public ReportStartTimesDto getStartTimes() {
        return startTimes;
    }

    public PerformanceDto getAnnouncement() {
        return announcement;
    }

    public ReportActualResultDto getConfirmedResult() {
        return confirmedResult;
    }

    public ReportActualResultDto getLocalResult() {
        return localResult;
    }

    public Start freeze() {
        this.frozen = true;
        return this;
    }

    public Start mutate() {
        return this.frozen ? new Start(this) : this;
    }

    private void assertMutable() {
        if (this.frozen) {
            throw new IllegalStateException("This start is frozen, use mutate()");
        }
    }

    public Start setInternalId(long internalId) {
        this.internalId = internalId;
        return this;
    }

    public Start setState(StartState state) {
        assertMutable();
        this.state = state;
        return this;
    }

    public Start setAthleteId(String athleteId) {
        assertMutable();
        this.athleteId = athleteId;
        return this;
    }

    public Start setDisciplineId(String disciplineId) {
        assertMutable();
        this.disciplineId = disciplineId;
        return this;
    }

    public Start setDisciplineName(String disciplineName) {
        assertMutable();
        this.disciplineName = disciplineName;
        return this;
    }

    public Start setAthlete(AthleteProfileDto athlete) {
        assertMutable();
        this.athlete = athlete;
        return this;
    }

    public Start setStartTimes(ReportStartTimesDto startTimes) {
        assertMutable();
        this.startTimes = startTimes;
        return this;
    }

    public Start setAnnouncement(PerformanceDto announcement) {
        assertMutable();
        this.announcement = announcement;
        return this;
    }

    public Start setConfirmedResult(ReportActualResultDto confirmedResult) {
        assertMutable();
        this.confirmedResult = confirmedResult;
        return this;
    }

    public Start setLocalResult(ReportActualResultDto localResult) {
        assertMutable();
        this.localResult = localResult;
        return this;
    }

    public boolean exists() {
        return state != StartState.MISSING;
    }

    public enum StartState {
        MISSING,
        READY,
        PENDING,
        REJECTED
    }
}
