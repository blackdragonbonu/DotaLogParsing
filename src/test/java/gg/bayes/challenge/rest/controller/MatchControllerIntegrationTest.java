package gg.bayes.challenge.rest.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import gg.bayes.challenge.rest.model.HeroDamage;
import gg.bayes.challenge.rest.model.HeroItem;
import gg.bayes.challenge.rest.model.HeroKills;
import gg.bayes.challenge.rest.model.HeroSpells;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

/*
 * Integration test template to get you started. Add tests and make modifications as you see fit.
 */
@Slf4j
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
class MatchControllerIntegrationTest {

    private static final String COMBATLOG_FILE_1 = "/data/combatlog_1.log.txt";
    private static final String COMBATLOG_FILE_2 = "/data/combatlog_2.log.txt";

    @Autowired
    private MockMvc mvc;

    private Map<String, Long> matchIds;

    @BeforeAll
    void setup() throws Exception {
        // Populate the database with all events from both sample data files and store the returned
        // match IDS.
        matchIds = Map.of(
                COMBATLOG_FILE_1, ingestMatch(COMBATLOG_FILE_1),
                COMBATLOG_FILE_2, ingestMatch(COMBATLOG_FILE_2));
    }

    //TODO Modify happy path test to assert that JSON matches expected
    // output. To be added once we have a response that has been verified to
    // match expectation
    @Test
    void shouldReturnBadRequestWhenIngestingEmptyFile() throws Exception {
        String fileContent = "";
        MockHttpServletResponse response = mvc.perform(post("/api/match")
                        .contentType(MediaType.TEXT_PLAIN)
                        .content(fileContent))
                .andReturn()
                .getResponse();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void shouldReturnBadRequestWhenIngestingInvalidFile() throws Exception {
        String fileContent = "GUGUGAGA";
        MockHttpServletResponse response = mvc.perform(post("/api/match")
                        .contentType(MediaType.TEXT_PLAIN)
                        .content(fileContent))
                .andReturn()
                .getResponse();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void shouldReturnMatchDetailsForValidMatchId() throws Exception {
        Long matchId = matchIds.get(COMBATLOG_FILE_1);
        MockHttpServletResponse response = mvc.perform(get("/api/match/" + matchId))
                .andReturn()
                .getResponse();
        String expectedResponse = "[{\"hero\":\"abyssal_underlord\"," +
                "\"kills\":6},{\"hero\":\"bane\",\"kills\":2},{\"hero\":\"bloodseeker\"," +
                "\"kills\":11},{\"hero\":\"death_prophet\",\"kills\":9},{\"hero\":\"dragon_knight\"," +
                "\"kills\":3},{\"hero\":\"mars\",\"kills\":6},{\"hero\":\"pangolier\"," +
                "\"kills\":5},{\"hero\":\"puck\",\"kills\":7},{\"hero\":\"rubick\",\"kills\":4}," +
                "{\"hero\":\"snapfire\",\"kills\":2}]";
        List<HeroKills> expectedHeroKills = deserializeList(expectedResponse,
                HeroKills.class);
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        String responseString = response.getContentAsString();
        assertThat(responseString).isNotEmpty();
        List<HeroKills> heroKills = deserializeList(responseString,
                HeroKills.class);
        assertThat(heroKills).hasSize(expectedHeroKills.size()).containsAll(expectedHeroKills);
    }

    @Test
    void shouldReturnNotFoundForInvalidMatchId() throws Exception {
        long invalidMatchId = 123456L;
        MockHttpServletResponse response = mvc.perform(get("/api/match/" + invalidMatchId))
                .andReturn()
                .getResponse();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void shouldReturnNotFoundWhenRetrievingItemPurchasesForInvalidMatchId() throws Exception {
        long invalidMatchId = 123456L;
        String heroName = "bane";
        MockHttpServletResponse response =
                mvc.perform(get("/api/match/" + invalidMatchId+"/"+heroName+
                                "/items"))
                .andReturn()
                .getResponse();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void shouldReturnNotFoundWhenRetrievingItemPurchasesForInvalidHeroName() throws Exception {
        long matchId = 1L;
        String heroName = "masthishkathilorushasthrakriya";
        MockHttpServletResponse response =
                mvc.perform(get("/api/match/" + matchId+"/"+heroName+
                                "/items"))
                        .andReturn()
                        .getResponse();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void shouldReturnValidDetailsWhenRetrievingItemPurchasesForHeroName() throws Exception {
        long matchId = 1L;
        String heroName = "bane";
        MockHttpServletResponse response =
                mvc.perform(get("/api/match/" + matchId+"/"+heroName+
                                "/items"))
                        .andReturn()
                        .getResponse();
        String expectedResponse = "[{\"item\":\"ward_observer\",\"timestamp\":825420}," +
                "{\"item\":\"magic_stick\",\"timestamp\":828452},{\"item\":\"recipe_magic_wand\"," +
                "\"timestamp\":933560},{\"item\":\"magic_wand\",\"timestamp\":934626}," +
                "{\"item\":\"ward_dispenser\",\"timestamp\":936059},{\"item\":\"ward_sentry\"," +
                "\"timestamp\":936059},{\"item\":\"ward_dispenser\",\"timestamp\":937426}," +
                "{\"item\":\"ward_observer\",\"timestamp\":937426},{\"item\":\"boots\",\"timestamp\":1070260}," +
                "{\"item\":\"ward_observer\",\"timestamp\":1098686},{\"item\":\"ward_dispenser\",\"timestamp\":1098919}," +
                "{\"item\":\"ward_sentry\",\"timestamp\":1098919},{\"item\":\"clarity\",\"timestamp\":1099319}," +
                "{\"item\":\"tpscroll\",\"timestamp\":1100852},{\"item\":\"tome_of_knowledge\",\"timestamp\":1214558}," +
                "{\"item\":\"ward_observer\",\"timestamp\":1215091},{\"item\":\"ward_dispenser\",\"timestamp\":1242818}," +
                "{\"item\":\"ward_observer\",\"timestamp\":1356190},{\"item\":\"clarity\",\"timestamp\":1356523}," +
                "{\"item\":\"ward_dispenser\",\"timestamp\":1378951},{\"item\":\"ward_observer\",\"timestamp\":1486492}," +
                "{\"item\":\"ward_dispenser\",\"timestamp\":1487091},{\"item\":\"ward_sentry\",\"timestamp\":1487091}," +
                "{\"item\":\"arcane_boots\",\"timestamp\":1493523},{\"item\":\"energy_booster\",\"timestamp\":1493523}," +
                "{\"item\":\"ward_dispenser\",\"timestamp\":1606462},{\"item\":\"ward_observer\",\"timestamp\":1606462}," +
                "{\"item\":\"tpscroll\",\"timestamp\":1634122},{\"item\":\"tpscroll\",\"timestamp\":1634322}," +
                "{\"item\":\"circlet\",\"timestamp\":1637021},{\"item\":\"gauntlets\",\"timestamp\":1637055}," +
                "{\"item\":\"bracer\",\"timestamp\":1637088},{\"item\":\"recipe_bracer\",\"timestamp\":1637088}," +
                "{\"item\":\"ward_observer\",\"timestamp\":1785452},{\"item\":\"ward_dispenser\",\"timestamp\":1785985}," +
                "{\"item\":\"ward_sentry\",\"timestamp\":1785985},{\"item\":\"circlet\",\"timestamp\":1790551}," +
                "{\"item\":\"gauntlets\",\"timestamp\":1790584},{\"item\":\"bracer\",\"timestamp\":1790617}," +
                "{\"item\":\"recipe_bracer\",\"timestamp\":1790617},{\"item\":\"tome_of_knowledge\",\"timestamp\":1891826}," +
                "{\"item\":\"ward_dispenser\",\"timestamp\":1892659},{\"item\":\"ward_observer\",\"timestamp\":1892659}," +
                "{\"item\":\"ward_dispenser\",\"timestamp\":1892992},{\"item\":\"ward_sentry\",\"timestamp\":1892992}," +
                "{\"item\":\"tpscroll\",\"timestamp\":1976938},{\"item\":\"tpscroll\",\"timestamp\":1977072}," +
                "{\"item\":\"tpscroll\",\"timestamp\":2177061},{\"item\":\"tpscroll\",\"timestamp\":2177194}," +
                "{\"item\":\"recipe_aether_lens\",\"timestamp\":2236430}]";

        List<HeroItem>expectedItems = deserializeList(expectedResponse,
                HeroItem.class);

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        String stringResponse = response.getContentAsString();
        assertThat(stringResponse).isNotEmpty();
        List<HeroItem> itemsFromResponse =
                deserializeList(stringResponse,
                HeroItem.class);
        assertThat(itemsFromResponse).hasSize(expectedItems.size()).containsAll(expectedItems);
    }

    @Test
    void shouldReturnNotFoundWhenRetrievingSpellCastsForInvalidMatchId() throws Exception {
        long invalidMatchId = 123456L;
        String heroName = "bane";
        MockHttpServletResponse response =
                mvc.perform(get("/api/match/" + invalidMatchId+"/"+heroName+
                                "/spells"))
                        .andReturn()
                        .getResponse();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void shouldReturnNotFoundWhenRetrievingSpellCastsForInvalidHeroName() throws Exception {
        long matchId = 1L;
        String heroName = "masthishkathilorushasthrakriya";
        MockHttpServletResponse response =
                mvc.perform(get("/api/match/" + matchId+"/"+heroName+
                                "/spells"))
                        .andReturn()
                        .getResponse();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void shouldReturnValidDetailsWhenRetrievingSpellCastsForHeroName() throws Exception {
        long matchId = 1L;
        String heroName = "bane";
        MockHttpServletResponse response =
                mvc.perform(get("/api/match/" + matchId+"/"+heroName+
                                "/spells"))
                        .andReturn()
                        .getResponse();
        String expectedResponse = "[{\"spell\":\"brain_sap\",\"casts\":24}," +
                "{\"spell\":\"fiends_grip\",\"casts\":5},{\"spell\":\"nightmare\",\"casts\":22}]";
        List<HeroSpells> expectedHeroSpells =
                deserializeList(expectedResponse, HeroSpells.class);

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        String responseString = response.getContentAsString();
        assertThat(responseString).isNotEmpty();
        List<HeroSpells> heroSpells = deserializeList(responseString,
                HeroSpells.class);
        assertThat(heroSpells).hasSize(expectedHeroSpells.size()).containsAll(expectedHeroSpells);
    }

    @Test
    void shouldReturnNotFoundWhenRetrievingHeroDamageForInvalidMatchId() throws Exception {
        long invalidMatchId = 123456L;
        String heroName = "bane";
        MockHttpServletResponse response =
                mvc.perform(get("/api/match/" + invalidMatchId+"/"+heroName+
                                "/damage"))
                        .andReturn()
                        .getResponse();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void shouldReturnNotFoundWhenRetrievingHeroDamageForInvalidHeroName() throws Exception {
        long matchId = 1L;
        String heroName = "masthishkathilorushasthrakriya";
        MockHttpServletResponse response =
                mvc.perform(get("/api/match/" + matchId+"/"+heroName+
                                "/damage"))
                        .andReturn()
                        .getResponse();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void shouldReturnValidDetailsWhenRetrievingHeroDamageForHeroName() throws Exception {
        long matchId = 1L;
        String heroName = "bane";
        MockHttpServletResponse response =
                mvc.perform(get("/api/match/" + matchId+"/"+heroName+
                                "/damage"))
                        .andReturn()
                        .getResponse();
        String expectedResponse = "[{\"target" +
                "\":\"abyssal_underlord\",\"damage_instances\":38,\"total_damage\":1712}," +
                "{\"target\":\"dragon_knight\",\"damage_instances\":27,\"total_damage\":1809}," +
                "{\"target\":\"pangolier\",\"damage_instances\":24,\"total_damage\":1164}," +
                "{\"target\":\"puck\",\"damage_instances\":32,\"total_damage\":2761}," +
                "{\"target\":\"snapfire\",\"damage_instances\":3," +
                "\"total_damage\":373}]";
        List<HeroDamage> expectedHeroDamage =
                deserializeList(expectedResponse, HeroDamage.class);
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        String responseString  = response.getContentAsString();
        assertThat(responseString).isNotEmpty();
        List<HeroDamage> heroDamage = deserializeList(responseString,
                HeroDamage.class);
        assertThat(heroDamage).hasSize(expectedHeroDamage.size()).containsAll(expectedHeroDamage);
    }
    /**
     * Helper method that ingests a combat log file and returns the match id associated with all parsed events.
     *
     * @param file file path as a classpath resource, e.g.: /data/combatlog_1.log.txt.
     * @return the id of the match associated with the events parsed from the given file
     * @throws Exception if an error happens when reading or ingesting the file
     */
    private Long ingestMatch(String file) throws Exception {
        String fileContent = IOUtils.resourceToString(file, StandardCharsets.UTF_8);

        return Long.parseLong(mvc.perform(post("/api/match")
                                         .contentType(MediaType.TEXT_PLAIN)
                                         .content(fileContent))
                                 .andReturn()
                                 .getResponse()
                                 .getContentAsString());
    }

    /**
     * Helper function to deserialize the list recieved from response
     * @param input
     * @param outputClass
     * @return
     * @param <T>
     * @throws JsonProcessingException
     */
    private <T> List<T> deserializeList(String input, Class<T> outputClass) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(input, new TypeReference<List<T>>() {});
    }
}
