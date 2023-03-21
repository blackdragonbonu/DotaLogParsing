package gg.bayes.challenge.service.utils;

import gg.bayes.challenge.persistence.model.CombatLogEntryEntity;
import gg.bayes.challenge.service.model.FieldExtractorContext;

public class ContextToModelMapper {

    public static CombatLogEntryEntity mapToEntity(FieldExtractorContext context) {
        if (context == null)
            return null;
        CombatLogEntryEntity response = new CombatLogEntryEntity();
        response.setTimestamp(context.getTimestamp());
        response.setActor(context.getActor());
        response.setItem(context.getItem());
        response.setAbility(context.getAbility());
        context.setAbilityLevel(context.getAbilityLevel());
        response.setDamage(context.getDamage());
        response.setTarget(context.getTarget());
        try {
            response.setType(mapEnum(context.getType()));
        } catch (Exception ignored) {
        }
        return response;
    }

    public static CombatLogEntryEntity.Type mapEnum(FieldExtractorContext.Type enumInput) throws Exception {
        switch (enumInput) {
            case DAMAGE_DONE:
                return CombatLogEntryEntity.Type.DAMAGE_DONE;
            case HERO_KILLED:
                return CombatLogEntryEntity.Type.HERO_KILLED;
            case ITEM_PURCHASED:
                return CombatLogEntryEntity.Type.ITEM_PURCHASED;
            case SPELL_CAST:
                return CombatLogEntryEntity.Type.SPELL_CAST;
            default:
                throw new Exception("Invalid Arguement Passed");
        }
    }
}
