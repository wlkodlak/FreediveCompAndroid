package net.wilczak.freedivecomp.android.ui.enterresult;

import android.databinding.Bindable;
import android.databinding.Observable;
import android.view.View;

import net.wilczak.freedivecomp.android.BR;
import net.wilczak.freedivecomp.android.R;
import net.wilczak.freedivecomp.android.domain.race.Race;
import net.wilczak.freedivecomp.android.domain.rules.PerformanceComponent;
import net.wilczak.freedivecomp.android.domain.rules.Rules;
import net.wilczak.freedivecomp.android.domain.rules.RulesPenalization;
import net.wilczak.freedivecomp.android.domain.rules.RulesProvider;
import net.wilczak.freedivecomp.android.domain.start.EnterPerformanceUseCase;
import net.wilczak.freedivecomp.android.domain.start.Start;
import net.wilczak.freedivecomp.android.domain.start.StartReference;
import net.wilczak.freedivecomp.android.remote.messages.CardResultDto;
import net.wilczak.freedivecomp.android.remote.messages.PenalizationDto;
import net.wilczak.freedivecomp.android.remote.messages.PerformanceDto;
import net.wilczak.freedivecomp.android.remote.messages.ReportActualResultDto;
import net.wilczak.freedivecomp.android.ui.application.BaseViewModel;
import net.wilczak.freedivecomp.android.ui.application.Localization;
import net.wilczak.freedivecomp.android.ui.utils.BindableSnackbarMessage;
import net.wilczak.freedivecomp.android.ui.utils.PerformanceFormatter;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

public class EnterResultViewModel extends BaseViewModel implements AddPenalizationDialog.OnPenalizationConfirmed {
    private final EnterPerformanceUseCase enterPerformanceUseCase;
    private final RulesProvider rulesProvider;
    private final Localization localization;
    private final PerformanceFormatter performanceFormatter;
    private Race race;
    private StartReference startReference;
    private CompositeDisposable disposables;
    private Start start;
    private Rules rules;
    private ReportActualResultDto dirtyResult;
    private BindableSnackbarMessage snackbarMessage;
    private EnterResultPerformanceViewModel depthViewModel, distanceViewModel, durationViewModel;
    private boolean ignorePerformanceChanges;
    private boolean showPenalizationSelection;
    private RulesPenalization showPenalizationInput;
    private boolean showPenalizationCustom;
    private InternalView internalView;

    @Inject
    public EnterResultViewModel(EnterPerformanceUseCase enterPerformanceUseCase, RulesProvider rulesProvider, Localization localization) {
        this.enterPerformanceUseCase = enterPerformanceUseCase;
        this.rulesProvider = rulesProvider;
        this.localization = localization;
        this.performanceFormatter = new PerformanceFormatter(localization);
        this.disposables = new CompositeDisposable();

        OnPropertyChangedCallback onPerformanceChangedCallback = new OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                if (ignorePerformanceChanges) return;
                boolean validityChanged = propertyId == BR._all || propertyId == BR.performanceValid;
                boolean valueChanged = propertyId == BR._all || propertyId == BR.realizedPerformance;
                if (validityChanged) onPerformanceValiditiesChanged();
                if (valueChanged) onPerformanceValuesChanged();
            }
        };
        this.depthViewModel = new EnterResultPerformanceViewModel(localization, PerformanceComponent.Depth);
        this.depthViewModel.addOnPropertyChangedCallback(onPerformanceChangedCallback);
        this.distanceViewModel = new EnterResultPerformanceViewModel(localization, PerformanceComponent.Distance);
        this.distanceViewModel.addOnPropertyChangedCallback(onPerformanceChangedCallback);
        this.durationViewModel = new EnterResultPerformanceViewModel(localization, PerformanceComponent.Duration);
        this.durationViewModel.addOnPropertyChangedCallback(onPerformanceChangedCallback);
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

    public void attachView(InternalView internalView) {
        this.internalView = internalView;
    }

    public void onStart() {
        if (start == null) {
            disposables.add(enterPerformanceUseCase
                    .getStart(race, startReference)
                    .subscribe(this::setStart));
        }
        if (rules == null) {
            disposables.add(rulesProvider
                    .getByDiscipline(race, startReference.getDisciplineId())
                    .subscribe(this::setRules));
        }
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposables.dispose();
    }

    private void setStart(Start start) {
        this.start = start;
        this.dirtyResult = null;

        ensureDirtyResult();

        ignorePerformanceChanges = true;
        distanceViewModel.setAnnouncedPerformance(start.getAnnouncement());
        depthViewModel.setAnnouncedPerformance(start.getAnnouncement());
        durationViewModel.setAnnouncedPerformance(start.getAnnouncement());
        distanceViewModel.setRealizedPerformance(dirtyResult.getPerformance());
        depthViewModel.setRealizedPerformance(dirtyResult.getPerformance());
        durationViewModel.setRealizedPerformance(dirtyResult.getPerformance());
        ignorePerformanceChanges = false;

        recalculateResult(false);
        notifyPropertyChanged(BR._all);
    }

    private void setRules(Rules rules) {
        this.rules = rules;
        recalculateResult(false);
        notifyPropertyChanged(BR._all);
    }

    private void ensureDirtyResult() {
        if (start == null) return;

        if (dirtyResult == null) {
            dirtyResult = start.getLocalResult();
            if (dirtyResult == null) dirtyResult = start.getConfirmedResult();
            if (dirtyResult == null) dirtyResult = new ReportActualResultDto();
            dirtyResult = dirtyResult.clone();
        }

    }

    private void recalculateResult(boolean regenerateShortPenalization) {
        if (start == null || rules == null || dirtyResult == null) return;

        if (dirtyResult.getPerformance() == null) {
            dirtyResult.setPerformance(new PerformanceDto());
        }
        if (rules.getHasPoints()) {
            dirtyResult.getPerformance().setPoints(rules.calculatePoints(dirtyResult.getPerformance()));
        }
        if (dirtyResult.getPenalizations() == null) {
            dirtyResult.setPenalizations(new ArrayList<>());
        }
        if (regenerateShortPenalization) {
            PenalizationDto shortPenalizationDto = rules.buildShortPenalization(start.getAnnouncement(), dirtyResult.getPerformance());
            replaceShortPenalization(dirtyResult.getPenalizations(), shortPenalizationDto);
        }
        dirtyResult.setFinalPerformance(calculateFinalPerformance(dirtyResult.getPerformance(), dirtyResult.getPenalizations()));
    }

    private void replaceShortPenalization(List<PenalizationDto> penalizations, PenalizationDto shortPenalizationDto) {
        int shortIndex = -1;
        for (int i = 0, penalizationsSize = penalizations.size(); i < penalizationsSize; i++) {
            PenalizationDto penalizationDto = penalizations.get(i);
            if (penalizationDto.getShortPerformance() != null && penalizationDto.getShortPerformance()) {
                shortIndex = i;
            }
        }

        if (shortIndex == -1) {
            if (shortPenalizationDto != null) {
                penalizations.add(0, shortPenalizationDto);
            }
        } else {
            if (shortPenalizationDto == null) {
                penalizations.remove(shortIndex);
            } else {
                penalizations.set(shortIndex, shortPenalizationDto);
            }
        }
    }

    private PerformanceDto calculateFinalPerformance(PerformanceDto realized, List<PenalizationDto> penalizations) {
        PerformanceDto finalPerformance = new PerformanceDto();
        finalPerformance.setDuration(realized.getDuration());
        finalPerformance.setDistance(realized.getDistance());
        finalPerformance.setDepth(realized.getDepth());
        finalPerformance.setPoints(realized.getPoints());

        PerformanceComponent penalizationComponent = rules.getPenalizationComponent();
        Double penalizableValue = penalizationComponent.get(finalPerformance);

        if (penalizableValue != null) {
            for (PenalizationDto penalization : penalizations) {
                Double penalizationValue = penalizationComponent.get(penalization.getPerformance());
                if (penalizationValue != null) {
                    penalizableValue = penalizableValue - penalizationValue;
                }
            }
            penalizationComponent.set(finalPerformance, penalizableValue);
        }
        return finalPerformance;
    }

    private void onPerformanceValiditiesChanged() {
        // nothing yet, but we might enable/disable Confirm button
    }

    private void onPerformanceValuesChanged() {
        recalculateResult(true);
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
        if (rules == null) return null;
        if (rules.getPrimaryComponent() == PerformanceComponent.Duration) {
            return durationViewModel;
        } else if (rules.getPrimaryComponent() == PerformanceComponent.Distance) {
            return distanceViewModel;
        } else if (rules.getPrimaryComponent() == PerformanceComponent.Depth) {
            return depthViewModel;
        } else {
            return null;
        }
    }

    @Bindable
    public EnterResultPerformanceViewModel getSecondaryDurationViewModel() {
        if (rules == null || !rules.getHasDuration() || rules.getPrimaryComponent() == PerformanceComponent.Duration) return null;
        return distanceViewModel;
    }

    public void onWhiteCardClick(View v) {
        if (dirtyResult.getPenalizations().size() == 0) {
            // all is OK
            dirtyResult.setCardResult(CardResultDto.WHITE);
        } else if (dirtyResult.getCardResult() != null) {
            // the judge really wants the white card, enable override
            dirtyResult.setCardResult(CardResultDto.WHITE);
            dirtyResult.getPenalizations().clear();
        } else {
            // judge probably misclicked, convert to yellow
            dirtyResult.setCardResult(CardResultDto.YELLOW);
            setSnackbarMessage(new BindableSnackbarMessage(localization.getString(R.string.popup_white_when_penalized)));
        }
        notifyPropertyChanged(BR._all);
    }

    public void onYellowCardClick(View v) {
        dirtyResult.setCardResult(CardResultDto.YELLOW);
        notifyPropertyChanged(BR._all);
        if (dirtyResult.getPenalizations().size() == 0) {
            showAddPenalization();
        }
    }

    public void onRedCardClick(View v) {
        dirtyResult.setCardResult(CardResultDto.RED);
        notifyPropertyChanged(BR._all);
        if (dirtyResult.getPenalizations().size() == 0) {
            showAddPenalization();
        }
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
        if (dirtyResult == null || rules == null) return null;
        List<EnterResultPenaltyViewModel> viewModels = new ArrayList<>();
        for (PenalizationDto penalizationDto : dirtyResult.getPenalizations()) {
            EnterResultPenaltyViewModel.DiscardListener discardListener = new EnterResultPenaltyViewModel.DiscardListener() {
                @Override
                public void onPenaltyDiscarded(EnterResultPenaltyViewModel penalty) {
                    dirtyResult.getPenalizations().remove(penalty.getPenalization());
                    recalculateResult(false);
                }
            };
            viewModels.add(new EnterResultPenaltyViewModel(
                    penalizationDto, rules.getPenalizationComponent(), performanceFormatter, discardListener));
        }
        return viewModels;
    }

    public void onAddPenaltyClick(View v) {
        showAddPenalization();
    }

    @Bindable
    public List<RulesPenalization> getPenalizationOptions() {
        if (rules == null || !showPenalizationSelection) return null;
        return rules.getPenalizations();
    }

    @Bindable
    public String getCustomPenalizationUnit() {
        if (rules == null) return null;
        PerformanceComponent component = rules.getPenalizationComponent();
        return performanceFormatter.getUnit(component);
    }

    @Bindable
    public RulesPenalization getShowPenalizationInput() {
        return showPenalizationInput;
    }

    @Bindable
    public boolean getShowPenalizationCustom() {
        return showPenalizationCustom;
    }

    private void showAddPenalization() {
        notifyPropertyChanged(BR.penalizationOptions);
        internalView.showPerformanceSelectionDialog();
    }

    @Override
    public void onPenalizationSelected(RulesPenalization specification) {
        showPenalizationSelection = false;
        notifyPropertyChanged(BR.penalizationOptions);

        if (specification == null) {
            showPenalizationCustom = true;
            notifyPropertyChanged(BR.showPenalizationCustom);
            internalView.showPerformanceCustomDialog();
        } else if (specification.getHasInput()) {
            showPenalizationInput = specification;
            notifyPropertyChanged(BR.showPenalizationInput);
            internalView.showPerformanceInputDialog();
        } else {
            PenalizationDto penalizationDto = specification.buildPenalization(0, dirtyResult.getPerformance());
            addPenalization(penalizationDto);
        }
    }

    @Override
    public void onPenalizationConfirmed(RulesPenalization specification, Double input) {
        showPenalizationInput = null;
        notifyPropertyChanged(BR.showPenalizationInput);

        PenalizationDto penalizationDto = specification.buildPenalization(input == null ? 0 : (double) input, dirtyResult.getPerformance());
        addPenalization(penalizationDto);
    }

    @Override
    public void onCustomPenalizationConfirmed(String reason, Double input) {
        PenalizationDto penalizationDto = new PenalizationDto();
        penalizationDto.setReason(reason).setShortReason(reason);
        if (input != null) {
            penalizationDto.setPerformance(new PerformanceDto());
            penalizationDto.setRuleInput(input);
            rules.getPenalizationComponent().set(penalizationDto.getPerformance(), input);
        }
        addPenalization(penalizationDto);
    }

    @Override
    public void onDismissed() {
        showPenalizationSelection = false;
        showPenalizationInput = null;
        showPenalizationCustom = false;
        notifyPropertyChanged(BR.penalizationOptions);
        notifyPropertyChanged(BR.showPenalizationCustom);
        notifyPropertyChanged(BR.showPenalizationInput);
    }

    private void addPenalization(PenalizationDto penalizationDto) {
        dirtyResult.getPenalizations().add(penalizationDto);
        recalculateResult(false);
    }

    @Bindable
    public String getFinalPerformanceFormatted() {
        if (dirtyResult == null || rules == null) return null;
        return performanceFormatter.formatEditable(rules.getPenalizationComponent(), dirtyResult.getFinalPerformance(), true);
    }

    public void onConfirmClick(View v) {
        if (dirtyResult == null) return;
        disposables.add(enterPerformanceUseCase
                .postResult(race, startReference, dirtyResult)
                .subscribe(this::onSaveComplete, this::onSaveError));
    }

    public void onDiscardClick(View v) {
        if (dirtyResult == null) return;
        disposables.add(enterPerformanceUseCase
                .postResult(race, startReference, null)
                .subscribe(this::onDiscardComplete, this::onSaveError));
    }

    private void onSaveComplete() {
        internalView.goToNextAthlete();
    }

    private void onDiscardComplete() {
        start = null;
        onStart();
    }

    private void onSaveError(Throwable error) {
        setSnackbarMessage(new BindableSnackbarMessage(error.getMessage()));
    }

    @Bindable
    public BindableSnackbarMessage getSnackbarMessage() {
        return snackbarMessage;
    }

    private void setSnackbarMessage(BindableSnackbarMessage snackbarMessage) {
        this.snackbarMessage = snackbarMessage;
        notifyPropertyChanged(BR.snackbarMessage);
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

    public String getTitle() {
        return localization.getString(R.string.enterresults_title);
    }

    public interface InternalView {
        void showPerformanceSelectionDialog();
        void showPerformanceInputDialog();
        void showPerformanceCustomDialog();
        void goToNextAthlete();
    }
}
