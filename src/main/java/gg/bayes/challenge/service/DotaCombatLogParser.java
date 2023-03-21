package gg.bayes.challenge.service;

import gg.bayes.challenge.persistence.model.CombatLogEntryEntity;
import gg.bayes.challenge.service.fieldextractor.FieldExtractorEngine;
import gg.bayes.challenge.service.fieldextractor.FieldExtractorEngineFactory;
import gg.bayes.challenge.service.model.FieldExtractorContext;
import gg.bayes.challenge.service.model.TokenizerResponse;
import gg.bayes.challenge.service.tokenizer.CombatLongEntryTokenizer;
import gg.bayes.challenge.service.utils.ContextToModelMapper;
import gg.bayes.challenge.service.validator.ValidationEngine;
import gg.bayes.challenge.service.validator.ValidationEngineFactory;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Parser class for parsing Dota Combat Logs
 */
@Component
public class DotaCombatLogParser {

    private final ValidationEngine<TokenizerResponse> prevalidationEngine;
    private final ValidationEngine<FieldExtractorContext> contextValidationEngine;
    private final FieldExtractorEngine<FieldExtractorContext> fieldExtractorEngine;
    private final CombatLongEntryTokenizer tokenizer;

    public DotaCombatLogParser(CombatLongEntryTokenizer tokenizer){
        this.prevalidationEngine =
                ValidationEngineFactory.createDotaPreValidationEngine();
        this.contextValidationEngine =
                ValidationEngineFactory.createDotaContextValidationEngine();
        this.fieldExtractorEngine =
                FieldExtractorEngineFactory.createDotaFieldExtractorEngine();
        this.tokenizer = tokenizer;
    }

    /**
     * Parses a Dota combat log file and returns the list of entries that are
     * needed to be stored. The rules for storing are determined by the
     * validation engines. Whereas the fields to be extracted are determined
     * by the FieldExtractor engine.
     * @param logFile String that contains the information from the log file
     * @return List of Combat Log entries that are to be used for storage.
     * @throws IOException
     */
    public List<CombatLogEntryEntity> parseLogFile(String logFile) throws IOException {
        List<CombatLogEntryEntity> response = new ArrayList<>();
        // Buffer reader is preferred to splitting string by newline and
        // reading line by line
        try(BufferedReader reader = new BufferedReader(new StringReader(logFile))){
            String line;
            while((line = reader.readLine())!= null){
                // Tokenizer converts the line into tokens which can be used
                // downstream
                TokenizerResponse tokenizerResponse =
                        tokenizer.tokenizeCombatLog(line);
                // Pre-validation engine determines whether there is a need to
                // proceed with field extraction
                if(prevalidationEngine.isValid(tokenizerResponse))
                    continue;
                // Create the context that is to be used for field extraction
                FieldExtractorContext context = new FieldExtractorContext(tokenizerResponse);
                // Extract the relevant fields from the log string
                fieldExtractorEngine.extractFields(context);
                // Validate whether the extracted fields is suitable for storage
                if(contextValidationEngine.isValid(context))
                    continue;
                // Map from entity to storage model
                CombatLogEntryEntity entryEntity =
                        ContextToModelMapper.mapToEntity(context);
                response.add(entryEntity);
            }
        }

        return response;
    }



}
