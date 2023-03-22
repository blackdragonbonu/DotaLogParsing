package gg.bayes.challenge.service.fieldextractor.steps;

import gg.bayes.challenge.service.model.FieldExtractorContext;
import gg.bayes.challenge.service.utils.DotaStringConstants;

public class HeroKillExtractor implements FieldExtractor<FieldExtractorContext> {
    @Override
    public void run(FieldExtractorContext context) {
        if (!context.getTokenizerResponse().isTokenizable() || context.getType() != FieldExtractorContext.Type.HERO_KILLED)
            return;
        var logComponents = context.getTokenizerResponse().getLogComponents();
        for (int i = 0; i < logComponents.size(); i++) {
            var currentComponent = logComponents.get(i);
            // hero kill is logged as <target> killed by <hero> so we assign
            // target when we encounter hero prefix for the first time and
            // then actor
            if (currentComponent.contains(DotaStringConstants.HERO_NAME_PREFIX)) {
                var heroName = currentComponent.replace(DotaStringConstants.HERO_NAME_PREFIX, "");
                if (context.getTarget() == null)
                    context.setTarget(heroName);
                else {
                    context.setActor(heroName);
                    break;
                }
            }
        }
    }
}
