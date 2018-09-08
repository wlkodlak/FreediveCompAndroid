package net.wilczak.freedivecomp.android.ui.enterresult;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.view.View;

import net.wilczak.freedivecomp.android.domain.rules.PerformanceComponent;
import net.wilczak.freedivecomp.android.remote.messages.PenalizationDto;
import net.wilczak.freedivecomp.android.ui.utils.PerformanceFormatter;

public class EnterResultPenaltyViewModel extends BaseObservable {
    private final PenalizationDto penalization;
    private final PerformanceComponent component;
    private final PerformanceFormatter performanceFormatter;
    private final DiscardListener listener;

    public EnterResultPenaltyViewModel(PenalizationDto penalization, PerformanceComponent component, PerformanceFormatter performanceFormatter, DiscardListener listener) {
        this.penalization = penalization;
        this.component = component;
        this.performanceFormatter = performanceFormatter;
        this.listener = listener;
    }

    public PenalizationDto getPenalization() {
        return penalization;
    }

    @Bindable
    public String getName() {
        return penalization.getReason();
    }

    @Bindable
    public String getValue() {
        return performanceFormatter.formatEditable(component, penalization.getPerformance(), true);
    }

    public void onClick(View v) {
        listener.onPenaltyDiscarded(this);
    }

    public interface DiscardListener {
        void onPenaltyDiscarded(EnterResultPenaltyViewModel penalty);
    }
}
