package gg.bayes.challenge.service.model;

import lombok.Getter;

@Getter
public class ValidatorResponse {
    private final boolean valid;
    private final String failureReason;

    public static final ValidatorResponse INVALID_INPUT = new ValidatorResponse(false, null);

    public ValidatorResponse(boolean valid, String failureReason) {
        this.valid = valid;
        this.failureReason = failureReason;
    }

    public ValidatorResponse(boolean valid) {
        this(valid, null);
    }

}
