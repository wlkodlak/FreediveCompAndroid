package net.wilczak.freedivecomp.android.ui.enterresult;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import net.wilczak.freedivecomp.android.R;
import net.wilczak.freedivecomp.android.domain.rules.PerformanceComponent;
import net.wilczak.freedivecomp.android.remote.messages.PerformanceDto;
import net.wilczak.freedivecomp.android.ui.application.Localization;

public class EnterResultPerformanceViewModel extends BaseObservable {
    private final Localization localization;
    private final PerformanceComponent component;
    private final PerformanceDto announcedPerformance;
    private PerformanceDto realizedPerformance;

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
            return localization.getString(R.string.performance_duration);
        } else if (component.equals(PerformanceComponent.Depth)) {
            return localization.getString(R.string.performance_depth);
        } else if (component.equals(PerformanceComponent.Distance)) {
            return localization.getString(R.string.performance_distance);
        } else {
            return null;
        }
    }
}
