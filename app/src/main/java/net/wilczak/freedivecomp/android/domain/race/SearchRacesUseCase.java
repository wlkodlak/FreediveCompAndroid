package net.wilczak.freedivecomp.android.domain.race;

import java.util.List;

import io.reactivex.Observable;

interface SearchRacesUseCase {
    Observable<List<Race>> search(String query);
}
