package net.wilczak.freedivecomp.android.ui.application;

import android.content.Context;

public class LocalizationNative implements Localization {
    private final Context context;

    public LocalizationNative(@ApplicationContext Context context) {
        this.context = context;
    }

    @Override
    public String getString(int stringId) {
        return context.getString(stringId);
    }
}
