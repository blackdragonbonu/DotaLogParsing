package gg.bayes.challenge.service.utils;

import gg.bayes.challenge.persistence.model.DamageDoneDto;
import gg.bayes.challenge.persistence.model.SpellCastDto;
import gg.bayes.challenge.rest.model.HeroDamage;
import gg.bayes.challenge.rest.model.HeroSpells;

public class DtoToResponseModelMapper {

    public static HeroSpells map(SpellCastDto dto){
        if(dto == null)
            return null;
        return new HeroSpells(
                dto.getAbility(),
                dto.getCount().intValue()
        );
    }

    public static HeroDamage map(DamageDoneDto dto){
        if(dto == null)
            return null;
        return new HeroDamage(
          dto.getTarget(), dto.getNumHits().intValue(),
          dto.getTotalDamage().intValue()
        );
    }
}
