package gg.bayes.challenge.service.validator.rules;

import gg.bayes.challenge.service.model.FieldExtractorContext;
import gg.bayes.challenge.service.model.ValidatorResponse;

public class SpellCastContextValidationRule implements ValidationRule<FieldExtractorContext> {
    @Override
    public ValidatorResponse validate(FieldExtractorContext context) {
        if (context == null)
            return ValidatorResponse.INVALID_INPUT;
        if(context.getType() != FieldExtractorContext.Type.SPELL_CAST)
            return new ValidatorResponse(true);

        boolean isValid = context.getTimestamp() > 0 && context.getActor() != null && !context.getActor().isEmpty()
                && context.getAbility() != null && !context.getAbility().isEmpty() && context.getAbilityLevel() > 0;
        return new ValidatorResponse(isValid);
    }
}
