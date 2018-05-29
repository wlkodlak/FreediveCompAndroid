package net.wilczak.freedivecomp.android.domain.start;

import net.wilczak.freedivecomp.android.domain.race.Race;
import net.wilczak.freedivecomp.android.remote.messages.ReportActualResultDto;

import io.reactivex.Completable;
import io.reactivex.Single;

public interface EnterPerformanceUseCase {
    Single<Start> getStart(Race race, StartReference reference);
    Completable postResult(Race race, StartReference reference, ReportActualResultDto localResult);
}
