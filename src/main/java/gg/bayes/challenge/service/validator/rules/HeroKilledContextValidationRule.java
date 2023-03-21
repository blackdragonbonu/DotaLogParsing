package gg.bayes.challenge.service.validator.rules;

import gg.bayes.challenge.service.model.FieldExtractorContext;
import gg.bayes.challenge.service.model.ValidatorResponse;

public class HeroKilledContextValidationRule implements ValidationRule<FieldExtractorContext> {
    @Override
    public ValidatorResponse validate(FieldExtractorContext context) {
        if (context == null)
            return ValidatorResponse.INVALID_INPUT;
        if(context.getType() != FieldExtractorContext.Type.HERO_KILLED)
            return new ValidatorResponse(true);

        boolean isValid = context.getTimestamp() > 0 && context.getActor() != null && !context.getActor().isEmpty()
                && context.getTarget() != null && !context.getTarget().isEmpty();
        return new ValidatorResponse(isValid);
    }
}
