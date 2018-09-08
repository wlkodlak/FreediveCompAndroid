package net.wilczak.freedivecomp.android.ui.enterresult;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import net.wilczak.freedivecomp.android.R;
import net.wilczak.freedivecomp.android.BR;
import net.wilczak.freedivecomp.android.domain.rules.PerformanceComponent;
import net.wilczak.freedivecomp.android.remote.messages.PerformanceDto;
import net.wilczak.freedivecomp.android.ui.application.Localization;
import net.wilczak.freedivecomp.android.ui.utils.PerformanceFormatter;

public class EnterResultPerformanceViewModel extends BaseObservable {
    private final Localization localization;
    private final PerformanceFormatter performanceFormatter;
    private final PerformanceComponent component;
    private PerformanceDto announcedPerformance;
    private PerformanceDto realizedPerformance;
    private String performanceValue;
    private boolean performanceValid;

    public EnterResultPerformanceViewModel(Localization localization, PerformanceComponent component) {
        this.localization = localization;
        this.component = component;
        this.performanceFormatter = new PerformanceFormatter(localization);
    }

    @Bindable
    public String getComponentName() {
        if (component.equals(PerformanceComponent.Duration)) {
            return localization.getString(R.string.performance_duration);
        } else if (component.equals(PerformanceComponent.Depth)) {
            return localization.getString(R.string.performance_depth);
        } else if (component.equals(PerformanceComponent.Distance)) {
            return localization.getString(R.string.performance_distance);
        } else {
            return null;
        }
    }

    @Bindable
    public String getComponentUnit() {
        if (component.equals(PerformanceComponent.Duration)) {
            return localization.getString(R.string.unit_second);
        } else if (component.equals(PerformanceComponent.Depth)) {
            return localization.getString(R.string.unit_meter);
        } else if (component.equals(PerformanceComponent.Distance)) {
            return localization.getString(R.string.unit_meter);
        } else {
            return null;
        }
    }

    @Bindable
    public String getAnnouncementFormatted() {
        return performanceFormatter.formatEditable(component, announcedPerformance, true);
    }

    @Bindable
    public String getPerformanceValue() {
        return performanceValue;
    }

    public void setPerformanceValue(String performanceValue) {
        this.performanceValue = performanceValue;
        notifyPropertyChanged(BR.performanceValue);
        try {
            performanceFormatter.parseEditable(component, performanceValue, realizedPerformance);
            notifyPropertyChanged(BR.realizedPerformance);
            setPerformanceValid(true);
        } catch (NumberFormatException e) {
            setPerformanceValid(false);
        }
    }

    @Bindable
    public PerformanceComponent getComponent() {
        return component;
    }

    @Bindable
    public PerformanceDto getAnnouncedPerformance() {
        return announcedPerformance;
    }

    @Bindable
    public PerformanceDto getRealizedPerformance() {
        return realizedPerformance;
    }

    @Bindable
    public boolean getPerformanceValid() {
        return performanceValid;
    }

    public void setAnnouncedPerformance(PerformanceDto announcedPerformance) {
        this.announcedPerformance = announcedPerformance;
        notifyPropertyChanged(BR.announcedPerformance);
        notifyPropertyChanged(BR.announcementFormatted);
    }

    public void setRealizedPerformance(PerformanceDto realizedPerformance) {
        this.realizedPerformance = realizedPerformance;
        this.performanceValue = performanceFormatter.formatEditable(component, realizedPerformance, false);
        this.performanceValid = true;
        notifyPropertyChanged(BR.realizedPerformance);
        notifyPropertyChanged(BR.performanceValue);
        notifyPropertyChanged(BR.performanceValid);
    }

    public void setPerformanceValid(boolean performanceValid) {
        this.performanceValid = performanceValid;
        notifyPropertyChanged(BR.performanceValid);
    }
}
