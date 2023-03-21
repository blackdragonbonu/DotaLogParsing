package gg.bayes.challenge.service.validator;

import gg.bayes.challenge.service.model.FieldExtractorContext;
import gg.bayes.challenge.service.model.TokenizerResponse;
import gg.bayes.challenge.service.validator.rules.*;

/**
 * Factory class to create {@link ValidationEngine}
 */
public class ValidationEngineFactory {
    public static ValidationEngine<TokenizerResponse> createDotaPreValidationEngine() {
        ValidationEngine.Builder<TokenizerResponse> engineBuilder = new ValidationEngine.Builder<>();
        engineBuilder.addRule(new ComponentSizeRule(5))
                .addRule(new HeroNamePresentRule());
        return engineBuilder.Build();
    }

    public static ValidationEngine<FieldExtractorContext> createDotaContextValidationEngine() {
        ValidationEngine.Builder<FieldExtractorContext> engineBuilder = new ValidationEngine.Builder<>();
        engineBuilder.addRule(new LogTypeContextValidationRule())
                .addRule(new DamageDoneContextValidationRule())
                .addRule(new ItemPurchaseContextValidationRule())
                .addRule(new HeroKilledContextValidationRule())
                .addRule(new SpellCastContextValidationRule());
        return engineBuilder.Build();
    }
}
