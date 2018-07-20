package net.wilczak.freedivecomp.android.ui.enterresult;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import net.wilczak.freedivecomp.android.domain.start.Start;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

public class EnterResultHeaderViewModel extends BaseObservable {
    private final Start start;

    public EnterResultHeaderViewModel(Start start) {
        this.start = start;
    }

    @Bindable
    public String getAthleteName() {
        return start.getAthlete().getFirstName() + " " + start.getAthlete().getSurname();
    }

    @Bindable
    public String getStartingLaneName() {
        return start.getStartTimes().getStartingLaneLongName();
    }

    @Bindable
    public String getOfficialTop() {
        DateTime officialTopTime = start.getStartTimes().getOfficialTop();
        return DateTimeFormat.shortTime().print(officialTopTime);
    }

    @Bindable
    public String getDisciplineName() {
        return start.getDisciplineName();
    }

    @Bindable
    public int getStatusLevel() {
        switch (start.getState()) {
            case REJECTED:
                return 2;
            case PENDING:
                return 1;
            default:
                return 0;
        }
    }
}
