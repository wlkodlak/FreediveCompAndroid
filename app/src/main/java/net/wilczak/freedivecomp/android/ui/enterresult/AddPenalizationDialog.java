package net.wilczak.freedivecomp.android.ui.enterresult;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import net.wilczak.freedivecomp.android.R;
import net.wilczak.freedivecomp.android.domain.rules.RulesPenalization;

import java.util.List;

public class AddPenalizationDialog {
    public static Dialog buildSelectionDialog(Context context, List<RulesPenalization> options, OnPenalizationConfirmed callback) {
        CharSequence[] reasons = new CharSequence[options.size() + 1];
        for (int i = 0; i < options.size(); i++) {
            reasons[i] = options.get(i).getReason();
        }
        reasons[options.size()] = context.getString(R.string.popup_custom);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(context.getString(R.string.popup_pick_penalization));
        builder.setCancelable(true);
        builder.setItems(reasons, (dialog, which) -> {
            RulesPenalization penalization = which >= options.size() ? null : options.get(which);
            callback.onPenalizationSelected(penalization);
        });
        builder.setOnCancelListener(dialog -> callback.onDismissed());
        return builder.create();
    }

    public static Dialog buildInputDialog(Context context, RulesPenalization specification, OnPenalizationConfirmed callback) {
        View contentView = LayoutInflater.from(context).inflate(R.layout.dialog_penalization_input, null, false);
        TextView unitText = contentView.findViewById(R.id.input_unit);
        unitText.setText(specification.getInputUnit());
        EditText inputView = contentView.findViewById(R.id.input_value);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(true);
        builder.setTitle(specification.getReason());
        builder.setView(contentView);
        builder.setNegativeButton(context.getString(R.string.popup_cancel), (dialog, which) -> callback.onDismissed());
        builder.setPositiveButton(context.getString(R.string.popup_confirm), (dialogInterface, which) -> {
            String inputString = inputView.getText().toString();
            Double input = TextUtils.isEmpty(inputString) ? null : Double.parseDouble(inputString);
            callback.onPenalizationConfirmed(specification, input);
        });
        builder.setOnCancelListener(dialog -> callback.onDismissed());
        return builder.create();
    }

    public static Dialog buildCustomDialog(Context context, String unit, OnPenalizationConfirmed callback) {
        View contentView = LayoutInflater.from(context).inflate(R.layout.dialog_penalization_input, null, false);
        EditText reasonView = contentView.findViewById(R.id.reason_value);
        TextView unitText = contentView.findViewById(R.id.input_unit);
        if (unit == null) {
            unitText.setVisibility(View.GONE);
        } else {
            unitText.setText(unit);
        }
        EditText inputView = contentView.findViewById(R.id.input_value);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(true);
        builder.setTitle(context.getString(R.string.popup_custom));
        builder.setView(contentView);
        builder.setNegativeButton(context.getString(R.string.popup_cancel), (dialog, which) -> callback.onDismissed());
        builder.setPositiveButton(context.getString(R.string.popup_confirm), (dialogInterface, which) -> {
            String reason = reasonView.getText().toString();
            String inputString = inputView.getText().toString();
            Double input = TextUtils.isEmpty(inputString) ? null : Double.parseDouble(inputString);
            callback.onCustomPenalizationConfirmed(reason, input);
        });
        builder.setOnCancelListener(dialog -> callback.onDismissed());
        return builder.create();
    }

    public interface OnPenalizationConfirmed {
        void onPenalizationSelected(RulesPenalization specification);
        void onPenalizationConfirmed(RulesPenalization specification, Double input);
        void onCustomPenalizationConfirmed(String reason, Double input);
        void onDismissed();
    }
}
