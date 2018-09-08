package net.wilczak.freedivecomp.android.ui.enterresult;

import android.databinding.Bindable;
import android.view.View;

import net.wilczak.freedivecomp.android.BR;
import net.wilczak.freedivecomp.android.domain.race.Race;
import net.wilczak.freedivecomp.android.domain.rules.Rules;
import net.wilczak.freedivecomp.android.domain.start.EnterPerformanceUseCase;
import net.wilczak.freedivecomp.android.domain.start.Start;
import net.wilczak.freedivecomp.android.domain.start.StartReference;
import net.wilczak.freedivecomp.android.remote.messages.CardResultDto;
import net.wilczak.freedivecomp.android.remote.messages.ReportActualResultDto;
import net.wilczak.freedivecomp.android.ui.application.BaseViewModel;
import net.wilczak.freedivecomp.android.ui.application.Localization;
import net.wilczak.freedivecomp.android.ui.utils.PerformanceFormatter;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;

public class EnterResultViewModel extends BaseViewModel {
    private final EnterPerformanceUseCase enterPerformanceUseCase;
    private final Localization localization;
    private final PerformanceFormatter performanceFormatter;
    private Race race;
    private StartReference startReference;
    private Disposable getStartDisposable;
    private Start start;
    private Rules rules;
    private ReportActualResultDto dirtyResult;
    private List<EnterResultPenaltyViewModel> penalties;

    @Inject
    public EnterResultViewModel(EnterPerformanceUseCase enterPerformanceUseCase, Localization localization) {
        this.enterPerformanceUseCase = enterPerformanceUseCase;
        this.localization = localization;
        this.performanceFormatter = new PerformanceFormatter(localization);
    }

    public Race getRace() {
        return race;
    }

    public void setRace(Race race) {
        this.race = race;
    }

    public StartReference getStartReference() {
        return startReference;
    }

    public void setStartReference(StartReference startReference) {
        this.startReference = startReference;
    }

    public void getData() {
        if (getStartDisposable == null) {
            getStartDisposable = enterPerformanceUseCase
                    .getStart(race, startReference)
                    .subscribe(this::setStart);
        }
    }

    private void setStart(Start start) {
        this.start = start;
        notifyPropertyChanged(BR._all);
    }

    @Bindable
    public String getAthleteName() {
        if (start == null) return null;
        return start.getAthlete().getFirstName() + " " + start.getAthlete().getSurname();
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

    @Bindable
    public String getStartingLane() {
        if (start == null) return null;
        return start.getStartTimes().getStartingLaneLongName();
    }

    @Bindable
    public String getOfficialTop() {
        DateTime officialTopTime = start.getStartTimes().getOfficialTop();
        return DateTimeFormat.shortTime().print(officialTopTime);
    }

    @Bindable
    public String getDisciplineName() {
        if (start == null) return null;
        return start.getDisciplineName();
    }

    @Bindable
    public EnterResultPerformanceViewModel getPrimaryComponentViewModel() {
        return null;
    }

    @Bindable
    public EnterResultPerformanceViewModel getSecondaryDurationViewModel() {
        return null;
    }

    public void onWhiteCardClick(View v) {

    }

    public void onYellowCardClick(View v) {

    }

    public void onRedCardClick(View v) {

    }

    @Bindable
    public float getWhiteCardAlpha() {
        return getCardAlpha(CardResultDto.WHITE);
    }

    @Bindable
    public float getYellowCardAlpha() {
        return getCardAlpha(CardResultDto.YELLOW);
    }

    @Bindable
    public float getRedCardAlpha() {
        return getCardAlpha(CardResultDto.RED, CardResultDto.DNS);
    }

    private float getCardAlpha(String... forCard) {
        String cardResult = dirtyResult == null ? null : dirtyResult.getCardResult();
        boolean fullAlpha = cardResult == null || matchesCard(cardResult, forCard);
        return fullAlpha ? 1.0f : 0.7f;
    }

    private boolean matchesCard(String actualCard, String... matchedCards) {
        for (String matchedCard : matchedCards) {
            if (matchedCard.equals(actualCard)) return true;
        }
        return false;
    }

    @Bindable
    public List<EnterResultPenaltyViewModel> getPenalties() {
        return penalties;
    }

    public void onAddPenaltyClick(View v) {
        
    }

    @Bindable
    public String getFinalPerformanceFormatted() {
        if (dirtyResult == null || rules == null) return null;
        return performanceFormatter.formatEditable(rules.getPenalizationComponent(), dirtyResult.getFinalPerformance(), true);
    }

    public void onConfirmClick(View v) {

    }

    public void onDiscardClick(View v) {

    }

    @Bindable
    public int getDiscardVisibility() {
        if (start == null) return View.GONE;
        switch (start.getState()) {
            case READY:
            case MISSING:
                return View.GONE;
            case REJECTED:
            case PENDING:
            case GONE:
            default:
                return View.VISIBLE;
        }
    }
}
