package net.wilczak.freedivecomp.android.domain.start;

import java.io.Serializable;

public class StartReference implements Serializable {
    private final long startId;
    private final String athleteId;
    private final String disciplineId;

    public StartReference(long startId, String athleteId, String disciplineId) {
        this.startId = startId;
        this.athleteId = athleteId;
        this.disciplineId = disciplineId;
    }

    public long getStartId() {
        return startId;
    }

    public String getAthleteId() {
        return athleteId;
    }

    public String getDisciplineId() {
        return disciplineId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StartReference that = (StartReference) o;
        if (athleteId.equals(that.athleteId) && disciplineId.equals(that.disciplineId)) return true;
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 937502734;
        hash = (hash * 31) + (athleteId.hashCode());
        hash = (hash * 31) + (disciplineId.hashCode());
        return hash;
    }

    @Override
    public String toString() {
        return "Athlete " + athleteId + " in " + disciplineId;
    }
}
