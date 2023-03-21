package gg.bayes.challenge.service.fieldextractor.steps;

import gg.bayes.challenge.service.model.FieldExtractorContext;
import gg.bayes.challenge.service.utils.DotaStringConstants;

public class HeroDamageExtractor implements FieldExtractor<FieldExtractorContext> {


    @Override
    public void run(FieldExtractorContext context) {
        if(!context.getTokenizerResponse().isTokenizable() || context.getType() != FieldExtractorContext.Type.DAMAGE_DONE)
            return;
        var logComponents = context.getTokenizerResponse().getLogComponents();
        for(int i =0;i<logComponents.size(); i++ ){
            var currentComponent =  logComponents.get(i);
            if(currentComponent.contains(DotaStringConstants.HERO_NAME_PREFIX)){
                var heroName = currentComponent.replace(DotaStringConstants.HERO_NAME_PREFIX,"");
                if(context.getActor() == null)
                    context.setActor(heroName);
                else
                    context.setTarget(heroName);
            }
            if(currentComponent.equals(DotaStringConstants.DAMAGE_IDENTIFIER) && i>0){
                try {
                    context.setDamage(Integer.parseInt(logComponents.get(i - 1)));
                }
                catch (Exception ignored){}
                break;
            }
        }
    }
}
