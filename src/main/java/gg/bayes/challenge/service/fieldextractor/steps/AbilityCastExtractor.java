package gg.bayes.challenge.service.fieldextractor.steps;

import gg.bayes.challenge.service.model.FieldExtractorContext;
import gg.bayes.challenge.service.utils.DotaStringConstants;

import java.util.regex.Pattern;

public class AbilityCastExtractor implements FieldExtractor<FieldExtractorContext> {
    private final Pattern pattern = Pattern.compile(DotaStringConstants.ABILITY_LVL_REGEX);

    @Override
    public void run(FieldExtractorContext context) {

        if (!context.getTokenizerResponse().isTokenizable() || context.getType() != FieldExtractorContext.Type.SPELL_CAST)
            return;
        var logComponents = context.getTokenizerResponse().getLogComponents();
        for (int i = 0; i < logComponents.size(); i++) {
            var currentComponent = logComponents.get(i);
            if (context.getActor()==null &&
                    currentComponent.contains(DotaStringConstants.HERO_NAME_PREFIX)) {
                var heroName = currentComponent.replace(DotaStringConstants.HERO_NAME_PREFIX, "");
                context.setActor(heroName);
                continue;
            }
            if (currentComponent.contains(DotaStringConstants.ABILITY_IDENTIFIER) && i < logComponents.size() - 1) {
                var abilityName = logComponents.get(i + 1);
                if (context.getActor() != null && !context.getActor().isEmpty() && abilityName.contains(context.getActor())) {
                    context.setAbility(abilityName.replace(context.getActor()+"_", ""));
                }
                continue;
            }
            if (context.getActor() != null && !context.getActor().isEmpty() &&
                    currentComponent.equals(DotaStringConstants.ABILITY_LEVEL_MARKER) &&
                    i < logComponents.size() - 1 ) {
                var abilityLevel = logComponents.get(i+1).replace(")","");
                try {
                    context.setAbilityLevel(Integer.parseInt(abilityLevel));
                } catch (Exception ignored) {}
                break;
            }
        }
    }
}
