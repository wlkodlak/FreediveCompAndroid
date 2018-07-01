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

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

public class StartingListItemViewModel extends BaseObservable {
    private final Start start;
    private final Localization localization;
    private final PeriodFormatter durationFormatter;
    private OnClickListener clickListener;

    public StartingListItemViewModel(Race race, Start start, Localization localization) {
        this.start = start;
        this.localization = localization;
        this.durationFormatter = new PeriodFormatterBuilder()
                .minimumPrintedDigits(1)
                .appendMinutes()
                .appendSeparator(":")
                .minimumPrintedDigits(2)
                .appendSeconds()
                .toFormatter();
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
        return formatPerformance(start.getAnnouncement());
    }

    @Bindable
    public String getRealizedPerformance() {
        return formatPerformance(getRealizedPerformanceUnformatted());
    }

    private String formatPerformance(PerformanceDto performance) {
        if (performance == null) {
            return null;
        } else if (performance.getDepth() != null) {
            return String.format(localization.getLocale(), "%fm", performance.getDepth());
        } else if (performance.getDistance() != null) {
            return String.format(localization.getLocale(), "%fm", performance.getDistance());
        } else if (performance.getDuration() != null) {
            return durationFormatter.print(performance.getDuration().toPeriod());
        } else {
            return null;
        }
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
                return 2;
            case PENDING:
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
