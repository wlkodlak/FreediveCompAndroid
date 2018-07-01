package net.wilczak.freedivecomp.android.ui.application;

import android.support.annotation.StringRes;

import java.util.Locale;

public interface Localization {
    Locale getLocale();
    String getString(@StringRes int stringId);
}
