package gg.bayes.challenge.service.validator.rules;

import gg.bayes.challenge.service.model.TokenizerResponse;
import gg.bayes.challenge.service.model.ValidatorResponse;
import lombok.Getter;

public class ComponentSizeRule implements ValidationRule<TokenizerResponse> {
    @Getter
    private final int minSize;

    @Getter
    private Integer maxSize;

    public ComponentSizeRule(int minSize, Integer maxSize) {
        this.minSize = minSize;
        this.maxSize = maxSize;
    }

    public ComponentSizeRule(int minSize) {
        this(minSize, null);
    }

    @Override
    public ValidatorResponse validate(TokenizerResponse tokenizerResponse) {
        if (tokenizerResponse == null || tokenizerResponse.equals(TokenizerResponse.NON_TOKENIZABLE_INPUT))
            return ValidatorResponse.INVALID_INPUT;
        int inputComponentSize = tokenizerResponse.getLogComponents().size();
        boolean isValid = inputComponentSize >= minSize && (maxSize == null || inputComponentSize <= maxSize);
        return new ValidatorResponse(isValid);
    }
}
