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
            // If Hero name has not yet been found retrieve it based on the
            // prefix
            if (context.getActor()==null &&
                    currentComponent.contains(DotaStringConstants.HERO_NAME_PREFIX)) {
                var heroName = currentComponent.replace(DotaStringConstants.HERO_NAME_PREFIX, "");
                context.setActor(heroName);
                continue;
            }
            // If we identify that an ability is being cast. Then the ability
            // name should follow that identifier.
            if (currentComponent.contains(DotaStringConstants.ABILITY_IDENTIFIER) && i < logComponents.size() - 1) {
                // Get the ability name
                var abilityName = logComponents.get(i + 1);
                // If we have already found the actor then get the ability
                // from ability toke. Ability is prefixed by the hero name, so
                // we have to sanitize it before storing in context
                if (context.getActor() != null && !context.getActor().isEmpty() && abilityName.contains(context.getActor())) {
                    context.setAbility(abilityName.replace(context.getActor()+"_", ""));
                }
                continue;
            }
            // If ability and actor has been found then extract the level of
            // the ability
            if (context.getActor() != null && !context.getActor().isEmpty() &&
                    currentComponent.equals(DotaStringConstants.ABILITY_LEVEL_MARKER) &&
                    i < logComponents.size() - 1 ) {
                // Level of the ability is logged as (lvl <d>) where <d> is an
                // integer. Since we are splitting on blank spaces the lvl
                // string is split into (lvl and <d>). So we are extracting
                // the one that contains the level of the ability
                var abilityLevel = logComponents.get(i+1).replace(")","");
                try {
                    context.setAbilityLevel(Integer.parseInt(abilityLevel));
                } catch (Exception ignored) {}
                break;
            }
        }
    }
}
