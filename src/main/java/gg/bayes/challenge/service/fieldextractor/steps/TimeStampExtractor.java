package gg.bayes.challenge.service.fieldextractor.steps;

import gg.bayes.challenge.service.model.FieldExtractorContext;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Objects;

public class TimeStampExtractor implements FieldExtractor<FieldExtractorContext> {
    private static final String defaultStringFormat = "[HH:mm:ss.SSS]";
    private final SimpleDateFormat format;

    public TimeStampExtractor() {
        this(defaultStringFormat);
    }

    public TimeStampExtractor(String timeStampFormat) {
        this.format = new SimpleDateFormat(timeStampFormat);
    }

    @Override
    public void run(FieldExtractorContext context) {
        if (!context.getTokenizerResponse().isTokenizable())
            return;
        var tokenizedString = Objects.requireNonNull(context.getTokenizerResponse().getLogComponents());
        var timeStampString = tokenizedString.get(0);
        try {
            Calendar date = Calendar.getInstance();
            date.setTime(format.parse(timeStampString));
            long timeStampInMilliseconds = date.get(Calendar.HOUR_OF_DAY) * (60 * 60 * 1000) +
                    date.get(Calendar.MINUTE) * (60 * 1000) + date.get(Calendar.SECOND) * 1000 +
                    date.get(Calendar.MILLISECOND);
            context.setTimestamp(timeStampInMilliseconds);
        } catch (ParseException ignored) {
        }
    }
}
