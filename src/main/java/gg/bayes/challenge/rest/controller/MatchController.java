package gg.bayes.challenge.rest.controller;

import gg.bayes.challenge.rest.model.HeroDamage;
import gg.bayes.challenge.rest.model.HeroItem;
import gg.bayes.challenge.rest.model.HeroKills;
import gg.bayes.challenge.rest.model.HeroSpells;
import gg.bayes.challenge.service.DotaCombatLogService;
import gg.bayes.challenge.service.InvalidLogFileException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/match")
@Validated
public class MatchController {
    @Autowired
    private DotaCombatLogService combatLogService;

    /**
     * Ingests a DOTA combat log file, parses and persists relevant events data. All events are associated with the same
     * match id.
     *
     * @param combatLog the content of the combat log file
     * @return the match id associated with the parsed events
     */
    @PostMapping(consumes = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<Long> ingestCombatLog(@RequestBody @NotBlank String combatLog) {
        Long matchId;
        try {
            matchId = combatLogService.extractAndStoreLog(combatLog);
        } catch (IOException e) {
            log.error("Exception occurred while parsing file", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(-1L);
        } catch (InvalidLogFileException e) {
            log.warn("Bad File passed for parsing");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(-1L);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(matchId);
    }

    /**
     * Fetches the heroes and their kill counts for the given match.
     *
     * @param matchId the match identifier
     * @return a collection of heroes and their kill counts
     */
    @GetMapping(
            path = "{matchId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<List<HeroKills>> getMatch(@PathVariable("matchId") Long matchId) {
        try {
            List<HeroKills> heroKills = combatLogService.getHeroKills(matchId);
            if (heroKills.size() == 0)
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(heroKills);

            return ResponseEntity.status(HttpStatus.OK).body(heroKills);
        }
        catch (Exception ex){
            log.error("Exception occurred while retrieving kills for "+ matchId, ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * For the given match, fetches the items bought by the named hero.
     *
     * @param matchId  the match identifier
     * @param heroName the hero name
     * @return a collection of items bought by the hero during the match
     */
    @GetMapping(
            path = "{matchId}/{heroName}/items",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<List<HeroItem>> getHeroItems(
            @PathVariable("matchId") Long matchId,
            @PathVariable("heroName") String heroName) {
        try {
            List<HeroItem> heroItems =
                    combatLogService.getHeroItemPurchaseTimeline(matchId, heroName);
            if (heroItems.size() == 0)
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(heroItems);

            return ResponseEntity.status(HttpStatus.OK).body(heroItems);
        }
        catch (Exception ex){
            log.error("Exception while retrieving hero items for Match : " + matchId + " and Hero :" + heroName, ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * For the given match, fetches the spells cast by the named hero.
     *
     * @param matchId  the match identifier
     * @param heroName the hero name
     * @return a collection of spells cast by the hero and how many times they were cast
     */
    @GetMapping(
            path = "{matchId}/{heroName}/spells",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<List<HeroSpells>> getHeroSpells(
            @PathVariable("matchId") Long matchId,
            @PathVariable("heroName") String heroName) {

        try {
            List<HeroSpells> heroSpells =
                    combatLogService.getHeroSpellCasts(matchId, heroName);
            if (heroSpells.size() == 0)
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(heroSpells);
            return ResponseEntity.status(HttpStatus.OK).body(heroSpells);
        }
        catch (Exception ex){
            log.error("Exception while retrieving hero spell casts for Match " +
                    ": " + matchId + " and Hero :" + heroName, ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * For a given match, fetches damage done data for the named hero.
     *
     * @param matchId  the match identifier
     * @param heroName the hero name
     * @return a collection of "damage done" (target, number of times and total damage) elements
     */
    @GetMapping(
            path = "{matchId}/{heroName}/damage",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<List<HeroDamage>> getHeroDamages(
            @PathVariable("matchId") Long matchId,
            @PathVariable("heroName") String heroName) {
        try {
            List<HeroDamage> heroDamages = combatLogService.getHeroDamage(matchId
                    , heroName);
            if (heroDamages.size() == 0)
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(heroDamages);
            return ResponseEntity.status(HttpStatus.OK).body(heroDamages);
        }
        catch (Exception ex){
            log.error("Exception while retrieving hero damage for Match : " + matchId + " and Hero :" + heroName, ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
