package gg.bayes.challenge.service.validator.rules;

import gg.bayes.challenge.service.model.FieldExtractorContext;
import gg.bayes.challenge.service.model.ValidatorResponse;

public class DamageDoneContextValidationRule implements ValidationRule<FieldExtractorContext> {
    @Override
    public ValidatorResponse validate(FieldExtractorContext context) {
        if (context == null)
            return ValidatorResponse.INVALID_INPUT;
        if(context.getType() != FieldExtractorContext.Type.DAMAGE_DONE)
            return new ValidatorResponse(true);

        boolean isValid = context.getTimestamp() > 0 && context.getActor() != null && !context.getActor().isEmpty() &&
                context.getDamage() > 0 && context.getTarget() != null && !context.getTarget().isEmpty();
        return new ValidatorResponse(isValid);
    }
}
