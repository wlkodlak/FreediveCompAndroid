package net.wilczak.freedivecomp.android.ui.findrace;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.view.View;

import net.wilczak.freedivecomp.android.domain.race.Race;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class SelectableRaceViewModel extends BaseObservable {
    private final Race race;
    private OnClickListener clickListener;

    public SelectableRaceViewModel(Race race) {
        this.race = race;
    }

    @Bindable
    public String getName() {
        return race.getName();
    }

    @Bindable
    public String getFormattedDateRange() {
        DateTime sinceDate = race.getSince();
        DateTime untilDate = race.getUntil();
        boolean sameDay = sinceDate.getYear() == untilDate.getYear() && sinceDate.getDayOfYear() == untilDate.getDayOfYear();

        DateTimeFormatter shortDate = DateTimeFormat.shortDate();
        String sinceString = shortDate.print(sinceDate);
        String untilString = shortDate.print(untilDate);

        if (sameDay) {
            return sinceString;
        } else {
            return sinceString + " - " + untilString;
        }
    }

    @Bindable
    public int getFavoritesVisibility() {
        boolean visible = race.isSaved() && race.isAuthenticated();
        return visible ? View.VISIBLE : View.GONE;
    }

    public void setClickListener(OnClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public void onClick(View v) {
        if (clickListener != null) {
            clickListener.onRaceClicked(race);
        }
    }

    public interface OnClickListener {
        void onRaceClicked(Race race);
    }
}
