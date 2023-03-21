package gg.bayes.challenge.service.model;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
public class FieldExtractorContext {

    private final TokenizerResponse tokenizerResponse;

    @NotNull
    @Setter
    private Long timestamp;

    @NotNull
    @Setter
    private Type type;

    @Setter

    private String actor;
    @Setter
    private String target;

    @Setter

    private String ability;

    @Setter
    private Integer abilityLevel;

    @Setter
    private String item;

    @Setter
    private Integer damage;

    public FieldExtractorContext(TokenizerResponse tokenizerResponse) {
        this.tokenizerResponse = tokenizerResponse;
    }

    public enum Type {
        UNKNOWN,
        ITEM_PURCHASED,
        HERO_KILLED,
        SPELL_CAST,
        DAMAGE_DONE
    }
}
