package gg.bayes.challenge.service.fieldextractor.steps;

import gg.bayes.challenge.service.model.FieldExtractorContext;

public interface FieldExtractor<T> {
    public void run(T context);
}
