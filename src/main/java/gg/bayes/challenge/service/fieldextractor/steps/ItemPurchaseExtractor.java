package gg.bayes.challenge.service.fieldextractor.steps;

import gg.bayes.challenge.service.model.FieldExtractorContext;
import gg.bayes.challenge.service.utils.DotaStringConstants;

public class ItemPurchaseExtractor implements FieldExtractor<FieldExtractorContext> {

    @Override
    public void run(FieldExtractorContext context) {
        if (!context.getTokenizerResponse().isTokenizable() || context.getType() != FieldExtractorContext.Type.ITEM_PURCHASED)
            return;
        var logComponents = context.getTokenizerResponse().getLogComponents();
        boolean isItemPurchase = false;
        for (int i = 0; i < logComponents.size(); i++) {
            var currentComponent = logComponents.get(i);
            if (currentComponent.contains(DotaStringConstants.HERO_NAME_PREFIX)) {
                var heroName = currentComponent.replace(DotaStringConstants.HERO_NAME_PREFIX, "");
                context.setActor(heroName);
            }
            if (currentComponent.equals(DotaStringConstants.ITEM_PURCHASE_VERB) && i > 0)
                isItemPurchase = true;
            if (currentComponent.contains(DotaStringConstants.ITEM_PREFIX) && isItemPurchase) {
                context.setItem(currentComponent.replace(DotaStringConstants.ITEM_PREFIX, ""));
                break;
            }
        }
    }
}
