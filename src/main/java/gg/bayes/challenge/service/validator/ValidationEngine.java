package gg.bayes.challenge.service.validator;

import gg.bayes.challenge.service.validator.rules.ValidationRule;

import java.util.ArrayList;
import java.util.List;

/**
 * Validation engine is used for validating whether an operation needs to be
 * done. Right now it is being used to check whether fields have to be
 * extracted and to check whether the extracted values need to be stored in
 * the db.
 * @param <T>
 */
public class ValidationEngine<T> {
    private final List<ValidationRule<T>> rules;

    private ValidationEngine(List<ValidationRule<T>> rules) {
        this.rules = rules;
    }

    /**
     * For each rule verify that the input passes
     * @param input Input of type T that is to be validated
     * @return boolean indicating whether true or false
     */
    public boolean isValid(T input) {
        for (ValidationRule<T> rule : rules) {
            try {
                var validationResponse = rule.validate(input);
                if (!validationResponse.isValid())
                    return true;
            }
            catch (Exception ex){
                return false;
            }
        }
        return false;
    }

    public static class Builder<T> {
        private final List<ValidationRule<T>> rules =
                new ArrayList<ValidationRule<T>>();

        public Builder<T> addRule(ValidationRule<T> rule) {
            this.rules.add(rule);
            return this;
        }

        public ValidationEngine<T> Build() {
            if (this.rules.size() == 0)
                return null;
            return new ValidationEngine<>(this.rules);
        }

    }
}
