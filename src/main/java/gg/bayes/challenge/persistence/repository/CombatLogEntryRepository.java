package gg.bayes.challenge.persistence.repository;

import gg.bayes.challenge.persistence.model.CombatLogEntryEntity;
import gg.bayes.challenge.persistence.model.DamageDoneDto;
import gg.bayes.challenge.persistence.model.HeroKillsDto;
import gg.bayes.challenge.persistence.model.SpellCastDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CombatLogEntryRepository extends JpaRepository<CombatLogEntryEntity, Long> {
    @Query("SELECT new gg.bayes.challenge.persistence.model.HeroKillsDto(e" +
            ".actor, COUNT(e))  FROM CombatLogEntryEntity e WHERE e.match.id" +
            " = :matchId AND e.type = 'HERO_KILLED' GROUP BY e.actor")
    List<HeroKillsDto> findHeroKillsByMatchId(@Param("matchId") Long matchId);

    @Query("SELECT e FROM CombatLogEntryEntity e WHERE e.match.id = " +
            ":matchId  AND e.actor = :heroName AND e.type = 'ITEM_PURCHASED' " +
            "ORDER BY e.timestamp ASC")
    List<CombatLogEntryEntity> findItemPurchaseByMatchIdAndHeroName(@Param(
            "matchId") long matchId, @Param("heroName") String heroName);

    @Query("SELECT new gg.bayes.challenge.persistence.model.SpellCastDto(e" +
            ".actor, e.ability, COUNT(e)) FROM CombatLogEntryEntity" +
            " e WHERE e.match.id = :matchId AND e.type = 'SPELL_CAST' AND e" +
            ".actor = :heroName  GROUP BY e.ability")
    List<SpellCastDto> findSpellsCastByHeroInMatch(@Param("matchId") Long matchId,
                                                    @Param("heroName") String heroName);


    @Query("SELECT new gg.bayes.challenge.persistence.model.DamageDoneDto(e" +
            ".target, COUNT(e), SUM(e.damage))  FROM CombatLogEntryEntity e  " +
            "WHERE e.match.id = :matchId AND e.actor = :heroName AND e.type =" +
            " 'DAMAGE_DONE' GROUP BY e.target")
    List<DamageDoneDto> findDamageByMatchAndHero(@Param("matchId") Long matchId,
                                             @Param(
            "heroName") String heroName);

}
