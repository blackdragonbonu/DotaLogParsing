package gg.bayes.challenge.service.fieldextractor;

import gg.bayes.challenge.service.fieldextractor.steps.*;
import gg.bayes.challenge.service.model.FieldExtractorContext;

/**
 * Factory class to create {@link FieldExtractorEngine}
 */
public class FieldExtractorEngineFactory {
    public static FieldExtractorEngine<FieldExtractorContext> createDotaFieldExtractorEngine(){
        FieldExtractorEngine.Builder<FieldExtractorContext> engineBuilder =
                new FieldExtractorEngine.Builder<>();
        engineBuilder.addStep(new TimeStampExtractor())
                .addStep(new CombatLogTypeExtractor())
                .addStep(new HeroKillExtractor())
                .addStep(new HeroDamageExtractor())
                .addStep(new ItemPurchaseExtractor())
                .addStep(new AbilityCastExtractor());
        return engineBuilder.build();
    }
}