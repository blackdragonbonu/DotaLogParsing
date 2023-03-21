package gg.bayes.challenge.persistence.model;

import lombok.Value;

@Value
public class HeroKillsDto {
    String heroName;
    Long numKills;
}
