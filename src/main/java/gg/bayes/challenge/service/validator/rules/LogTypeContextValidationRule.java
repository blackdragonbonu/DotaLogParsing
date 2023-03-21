package gg.bayes.challenge.service.validator.rules;

import gg.bayes.challenge.service.model.FieldExtractorContext;
import gg.bayes.challenge.service.model.ValidatorResponse;

public class LogTypeContextValidationRule implements ValidationRule<FieldExtractorContext>{

    @Override
    public ValidatorResponse validate(FieldExtractorContext context) {
        if (context == null)
            return ValidatorResponse.INVALID_INPUT;
        if( context.getType() == FieldExtractorContext.Type.UNKNOWN)
            return new ValidatorResponse(false);
        return new ValidatorResponse(true);
    }
}
