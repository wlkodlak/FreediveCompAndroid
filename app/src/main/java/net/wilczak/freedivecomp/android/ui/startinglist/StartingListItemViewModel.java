package net.wilczak.freedivecomp.android.ui.startinglist;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.view.View;

import net.wilczak.freedivecomp.android.domain.race.Race;
import net.wilczak.freedivecomp.android.domain.start.Start;
import net.wilczak.freedivecomp.android.remote.messages.CardResultDto;
import net.wilczak.freedivecomp.android.remote.messages.PerformanceDto;
import net.wilczak.freedivecomp.android.remote.messages.ReportActualResultDto;
import net.wilczak.freedivecomp.android.ui.application.Localization;
import net.wilczak.freedivecomp.android.ui.utils.PerformanceFormatter;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

public class StartingListItemViewModel extends BaseObservable {
    private final Start start;
    private final PerformanceFormatter performanceFormatter;
    private OnClickListener clickListener;

    public StartingListItemViewModel(Race race, Start start, Localization localization) {
        this.start = start;
        this.performanceFormatter = new PerformanceFormatter(localization);
    }

    public Start getStart() {
        return start;
    }

    @Bindable
    public String getAthleteName() {
        return start.getAthlete().getFirstName() + " " + start.getAthlete().getSurname();
    }

    @Bindable
    public String getOfficialTop() {
        DateTime officialTopTime = start.getStartTimes().getOfficialTop();
        return DateTimeFormat.shortTime().print(officialTopTime);
    }

    @Bindable
    public String getAnnouncedPerformance() {
        return performanceFormatter.formatPerformance(start.getAnnouncement());
    }

    @Bindable
    public String getRealizedPerformance() {
        return performanceFormatter.formatPerformance(getRealizedPerformanceUnformatted());
    }


    private ReportActualResultDto getResult() {
        if (start.getLocalResult() != null) {
            return start.getLocalResult();
        } else if (start.getConfirmedResult() != null) {
            return start.getConfirmedResult();
        } else {
            return null;
        }
    }

    private PerformanceDto getRealizedPerformanceUnformatted() {
        ReportActualResultDto result = getResult();
        return result == null ? null : result.getFinalPerformance();
    }

    public int getRealizedCardLevel() {
        ReportActualResultDto result = getResult();
        if (result == null) return 99;
        switch (result.getCardResult()) {
            case CardResultDto.WHITE:
                return 0;
            case CardResultDto.YELLOW:
                return 1;
            case CardResultDto.RED:
                return 2;
            default:
                return 99;
        }
    }

    @Bindable
    public int getRealizedVisibility() {
        ReportActualResultDto result = getResult();
        if (result == null) return View.GONE;
        if (result.getCardResult() == null) return View.GONE;
        if (CardResultDto.DNS.equals(result.getCardResult())) return View.GONE;
        return View.VISIBLE;
    }

    @Bindable
    public int getStatusLevel() {
        switch (start.getState()) {
            case REJECTED:
                return 3;
            case PENDING:
                return 2;
            case READY:
                return 1;
            default:
                return 0;
        }
    }

    public void setClickListener(OnClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public void onClick(View v) {
        if (clickListener != null) {
            clickListener.onStartClick(start);
        }
    }

    public interface OnClickListener {
        void onStartClick(Start start);
    }
}
