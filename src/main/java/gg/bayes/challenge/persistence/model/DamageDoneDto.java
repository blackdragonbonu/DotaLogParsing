package gg.bayes.challenge.persistence.model;


import lombok.Value;

@Value
public class DamageDoneDto {
    String target;
     Long numHits;
     Long totalDamage;
}
