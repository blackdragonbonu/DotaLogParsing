package gg.bayes.challenge.service;

import gg.bayes.challenge.persistence.model.CombatLogEntryEntity;
import gg.bayes.challenge.persistence.model.MatchEntity;
import gg.bayes.challenge.persistence.repository.CombatLogEntryRepository;
import gg.bayes.challenge.persistence.repository.MatchRepository;
import gg.bayes.challenge.rest.model.HeroDamage;
import gg.bayes.challenge.rest.model.HeroItem;
import gg.bayes.challenge.rest.model.HeroKills;
import gg.bayes.challenge.rest.model.HeroSpells;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Class for ingesting Dota combat logs and to retrieve data corresponding to
 * it.
 */
@Component
public class DotaCombatLogService {
    private final DotaCombatLogParser logParser;
    private final MatchRepository matchRepository;
    private final CombatLogEntryRepository combatLogEntryRepository;

    public DotaCombatLogService(DotaCombatLogParser logParser, MatchRepository matchRepository, CombatLogEntryRepository combatLogEntryRepository) {
        this.logParser = logParser;
        this.matchRepository = matchRepository;
        this.combatLogEntryRepository = combatLogEntryRepository;
    }

    /**
     * Parses the log file and stores the parsed information in DB.
     * Returns a matchId corresponding to the match that is associated
     * with the parsed log
     * @param log A log file passed in as a single string. Assumed format of
     *            [timestamp] list of tokens
     * @return Match Id of the newly inserted match into DB
     * @throws IOException
     */
    public Long extractAndStoreLog(String log) throws IOException, InvalidLogFileException {

        // Parse the log file and determine the list of entities to store
        List<CombatLogEntryEntity> parsedEntities =
                logParser.parseLogFile(log);
        if(parsedEntities == null || parsedEntities.size() == 0)
            throw new InvalidLogFileException("File is not Parsable");

        // Once the list of entities has been determined. Check if valid
        // and then store the match and list of entries into db
        MatchEntity matchEntity = matchRepository.save(new MatchEntity());

        // Once the match has been stored we updated the parsed entities
        // to contain the match information
        parsedEntities.forEach(parsedEntity -> parsedEntity.setMatch(matchEntity));

        // Store the entries into db
        combatLogEntryRepository.saveAll(parsedEntities);
        return matchEntity.getId();
    }

    /**
     * Retrieves the Items purchased by a hero for a given matchId. The
     * response is arranged in ascending order of purchase time
     * @param matchId Match Id for which the item purchases have to be
     *                retrieved for
     * @param heroName Hero whose item purchases have to be retrieved
     * @return List of Hero Item purchases with timestamp measured as number
     * of milliseconds elapsed from the begining of the game and the item
     * name of the item that was purchased
     */
    public List<HeroItem> getHeroItemPurchaseTimeline(long matchId,
                                                      String heroName) {
        return combatLogEntryRepository.findItemPurchaseByMatchIdAndHeroName(matchId
                , heroName).stream().map(combatLogEntryEntity -> new HeroItem(combatLogEntryEntity.getItem(),
                combatLogEntryEntity.getTimestamp())).collect(Collectors.toList());
    }

    /**
     * Retrieves the spells cast by the hero during the game and the number
     * of times the spells were caster for a specified match and hero.
     * @param matchId The match for which the information is to be retrieved
     * @param heroName The hero for whom we want to retrieve the values
     * @return List of Spells and their count
     */
    public List<HeroSpells> getHeroSpellCasts(long matchId, String heroName) {
        return combatLogEntryRepository.findSpellsCastByHeroInMatch(matchId,
                heroName).stream().map(entity -> new HeroSpells(entity.getAbility(),
                entity.getCount().intValue())).collect(Collectors.toList());
    }

    /**
     * Retrieves the total damage done by a hero to another heroes along with
     * the number of instances of damage. The damage is listed by the hero
     * that received the damage.
     * number of times the dam
     * @param matchId The match for which the information is to be retrieved
     * @param heroName The hero for whom we want to retrieve the values
     * @return List of Damages done by hero name, the total damage and the
     * count of damage instances
     */
    public List<HeroDamage> getHeroDamage(long matchId, String heroName) {
        return combatLogEntryRepository.findDamageByMatchAndHero(matchId,
                heroName).stream().map(entity -> new HeroDamage(entity.getTarget(),
                entity.getNumHits().intValue(), entity.getTotalDamage().intValue())).collect(Collectors.toList());
    }

    /**
     * Retrieve for a given match the number of kills by each hero
     * @param matchId The match Id for which the information is to be retrieved
     * @return List of hero name and the number of hills by the hero
     */
    public List<HeroKills> getHeroKills(long matchId) {
        return combatLogEntryRepository.findHeroKillsByMatchId(matchId).stream().
                map(entity -> new HeroKills(entity.getHeroName(),
                        entity.getNumKills().intValue())).
                collect(Collectors.toList());
    }
}
