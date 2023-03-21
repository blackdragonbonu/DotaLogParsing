package gg.bayes.challenge.service.fieldextractor.steps;

import gg.bayes.challenge.service.model.FieldExtractorContext;
import gg.bayes.challenge.service.utils.DotaStringConstants;

public class CombatLogTypeExtractor implements FieldExtractor<FieldExtractorContext> {
    @Override
    public void run(FieldExtractorContext context) {
        if(!context.getTokenizerResponse().isTokenizable())
            return;
        var logComponents = context.getTokenizerResponse().getLogComponents();
        for(int i=0; i<logComponents.size();i++){
            String currentComponent = logComponents.get(i);
            if(DotaStringConstants.HERO_KILL_VERB.equals(currentComponent)){
                context.setType(FieldExtractorContext.Type.HERO_KILLED);
                return;
            }
            else if (DotaStringConstants.ITEM_PURCHASE_VERB.equals(currentComponent)){
                context.setType(FieldExtractorContext.Type.ITEM_PURCHASED);
                return;
            }
            else if (DotaStringConstants.SPELL_CAST_VERB.equals(currentComponent)){
                context.setType(FieldExtractorContext.Type.SPELL_CAST);
                return;
            }
            else if(DotaStringConstants.DAMAGE_IDENTIFIER.equals(currentComponent)){
                context.setType(FieldExtractorContext.Type.DAMAGE_DONE);
                return;
            }
        }
        context.setType(FieldExtractorContext.Type.UNKNOWN);
    }
}
