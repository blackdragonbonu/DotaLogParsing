package gg.bayes.challenge.service.tokenizer;

import gg.bayes.challenge.service.model.TokenizerResponse;
import org.springframework.stereotype.Component;

/**
 * Class that converts a log entry into constituent tokens
 */
@Component
public class CombatLongEntryTokenizer {
    /**
     * Converts a string into a list of tokens. List of tokens in this case
     * refers to a list of strings. The tokenization uses a simple
     * methodology of splitting a single string by blank spaces
     * @param input A single line from a log file
     * @return Toeknizer response which contains information about whether
     * the line passed was tokenizable along with the list of tokens if
     * tokenization was successful
     */
    public TokenizerResponse tokenizeCombatLog(String input){
        if(input == null || input.isEmpty())
            return TokenizerResponse.NON_TOKENIZABLE_INPUT;
        //Split the string and return the response
        String[] components = input.split(" ");
        return new TokenizerResponse(components, true);
    }
}
