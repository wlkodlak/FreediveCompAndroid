package net.wilczak.freedivecomp.android.ui.utils;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.view.AbsSavedState;
import android.util.AttributeSet;
import android.view.View;

public class BindableSnackbar extends View {
    private long activeMessage;
    private long dismissedMessage;
    private Snackbar activeSnackbar;

    public BindableSnackbar(Context context) {
        this(context, null, 0);
    }

    public BindableSnackbar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BindableSnackbar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Nullable
    @Override
    protected Parcelable onSaveInstanceState() {
        SavedState savedState = new SavedState(super.onSaveInstanceState());
        savedState.dismissedMessage = dismissedMessage;
        return savedState;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable parcelable) {
        if (parcelable instanceof SavedState) {
            SavedState savedState = (SavedState) parcelable;
            super.onRestoreInstanceState(savedState.getSuperState());
            dismissedMessage = savedState.dismissedMessage;
        } else {
            super.onRestoreInstanceState(parcelable);
        }
    }

    public void setMessage(final BindableSnackbarMessage message) {
        if (message == null) {
            if (activeSnackbar != null) {
                activeSnackbar.dismiss();
                activeSnackbar = null;
                dismissedMessage = activeMessage;
                activeMessage = 0;
            }
        } else {
            if (message.getInstanceId() == activeMessage || message.getInstanceId() == dismissedMessage) return;

            activeMessage = message.getInstanceId();
            activeSnackbar = Snackbar.make(this, message.getMessage(), message.getDuration());
            if (message.getActionText() != null) {
                activeSnackbar.setAction(message.getActionText(), new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        message.getActionRunnable().run();
                    }
                });
            }
            activeSnackbar.addCallback(new Snackbar.Callback() {
                @Override
                public void onDismissed(Snackbar transientBottomBar, int event) {
                    boolean dismissed = event != DISMISS_EVENT_MANUAL;
                    if (dismissed && activeMessage == message.getInstanceId()) {
                        activeMessage = 0;
                        dismissedMessage = message.getInstanceId();
                    }
                }
            });
            activeSnackbar.show();
        }
    }

    public static class SavedState extends AbsSavedState {
        public long dismissedMessage;

        protected SavedState(Parcel in) {
            super(in);
            dismissedMessage = in.readLong();
        }

        public SavedState(@NonNull Parcelable superState) {
            super(superState);
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeLong(dismissedMessage);
        }

        public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {
            @Override
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            @Override
            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }
}
