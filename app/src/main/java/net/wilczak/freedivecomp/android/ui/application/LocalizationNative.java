package net.wilczak.freedivecomp.android.ui.application;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;

import java.util.Locale;

import javax.inject.Inject;

public class LocalizationNative implements Localization {
    private final Context context;

    @Inject
    public LocalizationNative(@ApplicationContext Context context) {
        this.context = context;
    }

    @Override
    public Locale getLocale() {
        Configuration configuration = context.getResources().getConfiguration();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return configuration.getLocales().get(0);
        } else {
            return configuration.locale;
        }
    }

    @Override
    public String getString(int stringId) {
        return context.getString(stringId);
    }
}
