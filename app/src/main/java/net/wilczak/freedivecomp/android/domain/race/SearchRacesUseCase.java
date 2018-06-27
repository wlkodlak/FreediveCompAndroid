package net.wilczak.freedivecomp.android.domain.race;

import java.util.List;

import io.reactivex.Observable;

public interface SearchRacesUseCase {
    Observable<List<Race>> search(String query);
}
