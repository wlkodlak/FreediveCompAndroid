package net.wilczak.freedivecomp.android.ui.application;

import android.content.Context;

import javax.inject.Inject;

public class LocalizationNative implements Localization {
    private final Context context;

    @Inject
    public LocalizationNative(@ApplicationContext Context context) {
        this.context = context;
    }

    @Override
    public String getString(int stringId) {
        return context.getString(stringId);
    }
}
