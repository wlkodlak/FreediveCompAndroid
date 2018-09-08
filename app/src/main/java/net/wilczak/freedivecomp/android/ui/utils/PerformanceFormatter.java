package net.wilczak.freedivecomp.android.ui.utils;

import net.wilczak.freedivecomp.android.domain.rules.PerformanceComponent;
import net.wilczak.freedivecomp.android.remote.messages.PerformanceDto;
import net.wilczak.freedivecomp.android.ui.application.Localization;

import org.joda.time.Period;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PerformanceFormatter {
    private static final Pattern durationRegex = Pattern.compile("^([0-9]+:)?([0-9]+)$");
    private final Localization localization;
    private final PeriodFormatter durationFormatter;

    public PerformanceFormatter(Localization localization) {
        this.localization = localization;
        this.durationFormatter = new PeriodFormatterBuilder()
                .minimumPrintedDigits(1)
                .appendMinutes()
                .appendSeparator(":")
                .minimumPrintedDigits(2)
                .appendSeconds()
                .toFormatter();
    }

    public String formatPerformance(PerformanceDto performance) {
        PerformanceComponent component = PerformanceComponent.detect(performance);
        return formatEditable(component, performance, true);
    }

    public String formatEditable(PerformanceComponent component, PerformanceDto performance, boolean includeUnit) {
        if (component == PerformanceComponent.Distance || component == PerformanceComponent.Depth) {
            return formatNumber(includeUnit ? "m" : "", component.get(performance));
        } else if (component == PerformanceComponent.Points) {
            return formatNumber(includeUnit ? "p" : "", component.get(performance));
        } else if (component == PerformanceComponent.Duration) {
            return formatDuration(component.get(performance));
        } else {
            return null;
        }
    }

    private String formatNumber(String unit, Double value) {
        if (value == null) return null;
        return String.format(localization.getLocale(), "%f" + unit, value);
    }

    private String formatDuration(Double value) {
        if (value == null) return null;
        long millis = (long) (value * 1000);
        return durationFormatter.print(new Period(millis));
    }

    public void parseEditable(PerformanceComponent component, String input, PerformanceDto target) {
        if (component == PerformanceComponent.Distance || component == PerformanceComponent.Depth) {
            component.set(target, parseLength(input));
        } else if (component == PerformanceComponent.Duration) {
            component.set(target, parseDuration(input));
        }
    }

    private Double parseLength(String input) {
        try {
            if (input == null) return null;
            if (input.endsWith("m")) {
                input = input.substring(0, input.length() - 1);
            }
            Number number = NumberFormat.getInstance(localization.getLocale()).parse(input);
            return number.doubleValue();
        } catch (ParseException e) {
            throw new NumberFormatException(input);
        }
    }

    private Double parseDuration(String input) {
        Matcher matcher = durationRegex.matcher(input);
        if (!matcher.matches()) throw new NumberFormatException(input);

        int colonPosition = input.indexOf(":");
        if (colonPosition < 0) {
            return (double) Integer.parseInt(input);
        } else {
            int minutes = Integer.parseInt(input.substring(0, colonPosition));
            int seconds = Integer.parseInt(input.substring(colonPosition + 1));
            return (double) (minutes * 60 + seconds);
        }
    }
}
