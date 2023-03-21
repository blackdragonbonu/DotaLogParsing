package gg.bayes.challenge.service.validator.rules;

import gg.bayes.challenge.service.model.ValidatorResponse;

public interface ValidationRule<T> {
    ValidatorResponse validate(T input);
}
