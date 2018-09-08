package net.wilczak.freedivecomp.android.ui.utils;

import android.support.design.widget.Snackbar;

import java.util.concurrent.atomic.AtomicLong;

public class BindableSnackbarMessage {
    public static final int LENGTH_LONG = Snackbar.LENGTH_LONG;
    public static final int LENGTH_SHORT = Snackbar.LENGTH_SHORT;
    public static final int LENGTH_INDEFINITE = Snackbar.LENGTH_INDEFINITE;
    private static AtomicLong nextInstanceId = new AtomicLong();

    private final long instanceId;
    private final String message;
    private final int duration;
    private final String actionText;
    private final Runnable actionRunnable;

    public BindableSnackbarMessage(String message) {
        this(generateInstanceId(), message, LENGTH_LONG, null, null);
    }

    public static long generateInstanceId() {
        return nextInstanceId.incrementAndGet();
    }

    public BindableSnackbarMessage(long instanceId, String message, int duration, String actionText, Runnable actionRunnable) {
        this.instanceId = instanceId;
        this.message = message;
        this.duration = duration;
        this.actionText = actionText;
        this.actionRunnable = actionRunnable;
    }

    public long getInstanceId() {
        return instanceId;
    }

    public String getMessage() {
        return message;
    }

    public int getDuration() {
        return duration;
    }

    public String getActionText() {
        return actionText;
    }

    public Runnable getActionRunnable() {
        return actionRunnable;
    }
}
